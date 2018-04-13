package Chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

public enum ServerSingleton {
    SERVER;

    ServerSingleton() {
        Server server = new Server();
    }

    class Server extends Thread {

        Server() {
            super.setDaemon(true);  //Turning thread into daemon to make it independent from the rest of the program
            super.start();
        }

        @Override
        public void run() {
            //Карта соответствия логинов и потоков вывода сокетов
            LinkedHashMap<String, DataOutputStream> map = new LinkedHashMap<>();
            final int PORT = 1901;
            final String ALLOWED = "Access is allowed";
            final String TRY_AGAIN = "Try again";
            try {
                //Creating listener
                ServerSocket serverSocket = new ServerSocket(PORT);
                System.out.println("Server is working");

                while (true) {
                    Socket socket = serverSocket.accept();  //Listening
                    DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    Properties passwords = new Properties();
                    String password;
                    Message message = null;

                    boolean newClient = false;
                    try (InputStream inputStream = new FileInputStream("src/main/resources/passwords.properties")) {
                        boolean accessDenied = true;
                        passwords.loadFromXML(inputStream);
                        while (accessDenied) {
                            message = gson.fromJson(dataInput.readUTF(), Message.class);
                            password = passwords.getProperty(message.header());
                            if (password != null) {
                                if (password.equals(message.body())) {//Если в базе есть такой же ник-нейм с таким же паролем
                                    dataOutput.writeUTF(ALLOWED);
                                    dataOutput.flush();
                                    accessDenied = false;
                                } else {//Если в базе есть такой ник-нейм, но пароль не сходится
                                    dataOutput.writeUTF(TRY_AGAIN);
                                    dataOutput.flush();
                                }
                            } else {  //Если такого ник-нейма нет в базе
                                dataOutput.writeUTF(ALLOWED);
                                dataOutput.flush();
                                newClient = true;
                                accessDenied = false;
                            }
                        }
                    } catch (FileNotFoundException e) { //Если не найден файл с паролями
                        dataOutput.writeUTF(ALLOWED);
                        newClient = true;    //Если файла с паролями нет, значит клиент в любом случае новый
                        message = gson.fromJson(dataInput.readUTF(), Message.class);
                    }

                    if (newClient) {
                        try (OutputStream outputStream = new FileOutputStream("src/main/resources/passwords.properties")) {
                            passwords.put(message.header(), message.body());
                            passwords.storeToXML(outputStream, "Passwords database");
                        } catch (IOException e) {
                            System.out.println("Something bad has happenned while saving personal client's information");
                        }
                    }

                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        message.changeBody("is here");
                        DataOutputStream stream = map.get(key);
                        stream.writeUTF(gson.toJson(message));
                        stream.flush();
                    }
                    map.put(message.header(),dataOutput);
                    new ServerListener(map, socket, message.header()); //Creating thread for new client
                }
            } catch(IOException e) {
                System.out.println("Something bad has happenned while trying to start the server");
            }
        }
    }
}