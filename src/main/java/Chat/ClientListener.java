package Chat;

import Interfaces.IService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientListener extends Thread {
    private DataInputStream dataInput;
    private final String NAME;
    private StringBuilder history;

    ClientListener(DataInputStream dataInput, String name, StringBuilder history) {
        super();
        this.dataInput = dataInput;
        this.NAME = name;
        this.history = history;
        super.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = this.dataInput.readUTF();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Message message = gson.fromJson(line, Message.class);
                if (message.header().equals(this.NAME)) {
                    this.history.append("You: ");
                } else {
                    this.history.append(message.header());
                    this.history.append(": ");
                }
                this.history.append(message.body());
                this.history.append("\n\n");

                IService.clearConsole();
                System.out.print(this.history);
                System.out.print("You: ");
            } catch (IOException e) {
                break;
            }
        }
    }
}
