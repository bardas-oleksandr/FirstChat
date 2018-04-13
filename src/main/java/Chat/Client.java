package Chat;

import Interfaces.IService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
    private StringBuilder history;

    Client() {
        super();
        this.history = new StringBuilder();
        super.start();
    }

    @Override
    public void run() {
        IService.clearConsole();
        final String EXIT = "EXIT";
        final int SERVER_PORT = 1901;
        final String address = "127.0.0.1";
        final String ALLOWED = "Access is allowed";
        try {
            //Creating connection
            InetAddress inetAddress = InetAddress.getByName(address);
            Socket socket = new Socket(inetAddress, SERVER_PORT);

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String login, password;
            boolean accessDenied = true;
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            do {
                System.out.print("Your login: ");
                login = keyboard.readLine();
                System.out.print("Your password: ");
                Console console = System.console();
                if (console != null) {
                    password = new String(console.readPassword());  //Allows not showing symbols when typing
                } else {
                    password = keyboard.readLine();
                }
                Message message = new Message(login, password);
                output.writeUTF(gson.toJson(message)); //Sending message
                output.flush();

                String response = input.readUTF();
                if (response.equals(ALLOWED)) {
                    accessDenied = false;
                } else {
                    System.out.println("Login is already used, or password is wrong");
                    IService.pressEnterToContinue();
                }
            } while (accessDenied);

            ClientListener listener = new ClientListener(input, login, this.history);//Creating thread, that will be listening
            //Start chatting
            String body;
            boolean chatting = true;
            System.out.print("Your message:");
            while (chatting) {
                body = keyboard.readLine();  //Typing message
                if(body.equals(EXIT)){
                    socket.close();
                    chatting = false;
                }else{
                    Message message = new Message(login, body);
                    output.writeUTF(gson.toJson(message));//Sending messages
                    output.flush();
                }
            }
            try {
                listener.join();
            } catch (InterruptedException e) {
                System.out.println("Something bad has happenned while waiting for the end of client listening process");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Something bad has happenned while client process");
            System.out.println("Supposedly server became unavailable");
        }
    }
}
