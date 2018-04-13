package Chat;

import Interfaces.IProblem;
import Interfaces.IService;

import java.io.IOException;

public class Main implements IProblem {
    @Override
    public void solve() {
        final int SERVER_MODE = 1;
        final int CLIENT_MODE = 2;
        final int EXIT = 0;
        int choice;
        do {
            IService.clearConsole();
            System.out.println("Select action");
            System.out.println("1 - Start the server");
            System.out.println("2 - Join the chat");
            System.out.println("0 - Exit");
            System.out.print("Your choice: ");
            choice = IService.getIntegerBounded(EXIT, CLIENT_MODE);
            switch (choice) {
                case SERVER_MODE:
                    ServerSingleton server = ServerSingleton.SERVER;    //Сервер стартовать будем только один раз
                    IService.pressEnterToContinue();
                    break;
                case CLIENT_MODE:
                    Client client = new Client();
                    try {
                        client.join();
                    } catch (InterruptedException e) {
                        System.out.println("Something bad has happenned while waiting for the end of client process");
                    }
                    break;
            }
        } while (choice != EXIT);
    }
}
