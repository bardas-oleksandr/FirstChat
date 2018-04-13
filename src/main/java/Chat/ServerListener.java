package Chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Set;

public class ServerListener extends Thread {
    private LinkedHashMap<String, DataOutputStream> map;
    private Socket socket;
    private String name;

    ServerListener(LinkedHashMap<String, DataOutputStream> map, Socket socket, String name) {
        super();
        this.map = map;
        this.socket = socket;
        this.name = name;
        super.setDaemon(true);
        super.start();
    }

    @Override
    public void run() {
        try (DataInputStream dataInput = new DataInputStream(socket.getInputStream())) {
            boolean connected = true;
            while (connected) {
                String line;
                try {
                    line = dataInput.readUTF();
                } catch (SocketException | EOFException e) {
                    Message message = new Message(this.name, "has leaved the conversation");
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    line = gson.toJson(message);
                    connected = false;
                    map.remove(this.name);
                }
                Set<String> keys = map.keySet();
                for (String key : keys) {
                    DataOutputStream stream = map.get(key);
                    stream.writeUTF(line);
                    stream.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Something bad has happenned while server listening process");
        }
    }
}
