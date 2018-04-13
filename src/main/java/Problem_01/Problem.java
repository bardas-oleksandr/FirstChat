package Problem_01;

import Interfaces.IProblem;
import Interfaces.IService;

import java.util.Scanner;

public class Problem implements IProblem {
    @Override
    public void solve() {
        final int SERVER = 0;
        final int CLIENT = 1;
        System.out.println("Select entrance mode");
        System.out.println("0 - Server");
        System.out.println("1 - Client");
        System.out.print("Your choice:");
        int choice = IService.getIntegerBounded(SERVER, CLIENT);
        Messenger messenger = null;
        switch(choice){
            case SERVER:
                messenger = new Messenger(Messenger::server);
                setIP(messenger);
                break;
            case CLIENT:
                messenger = new Messenger(Messenger::client);
                break;
        }
        messenger.run();
        IService.pressEnterToContinue();
    }

    static void setIP(Messenger messenger){
        final int SET = 0;
        final int DEFAULT = 1;
        final int LOCAL_HOST = 2;
        System.out.println("Select operation");
        System.out.println("0 - Set IP-address to connect");
        System.out.println("1 - Use default IP-address");
        System.out.println("2 - Use local host");
        System.out.print("Your choice:");
        int choice = IService.getIntegerBounded(SET, LOCAL_HOST);
        switch(choice){
            case SET:
                System.out.print("IP: ");
                Scanner scanner = new Scanner(System.in);
                messenger.setIP(scanner.nextLine());
                break;
            case DEFAULT:
                messenger.setIP("192.168.43.124");
                break;
            case LOCAL_HOST:
                messenger.setIP("127.0.0.1");
                break;
        }
    }
}
