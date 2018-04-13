import Interfaces.IProblem;
import Interfaces.IService;

public class Main {
    public static void main(String[] args) {
        int choice;
        final int PROBLEMS_COUNT = 2;
        do {
            IService.clearConsole();
            System.out.println("MENU");
            System.out.println("1 - CONNECTING 2 COMPUTERS");
            System.out.println("2 - CHAT");
            System.out.println("0 - EXIT");
            System.out.print("Your choice:");
            choice = IService.getIntegerBounded(0, PROBLEMS_COUNT);
            if (choice != 0) {
                IProblem problem = Interfaces.ICreator.create(choice);
                if (problem != null) {
                    problem.solve();
                }
                IService.pressEnterToContinue();
            }
        } while (choice != 0);
        System.out.println("Buy-buy!");
    }
}
