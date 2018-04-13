package Interfaces;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public interface IService {
    static int getInteger(){
        Scanner scan = new Scanner(System.in);
        while(true){
            try{
                return scan.nextInt();
            }catch(InputMismatchException e){
                System.out.print("Incorrect input! Integer is expected. Tru again:");
                scan.nextLine();
            }
        }
    }

    static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static int getIntegerBounded(int min, int max){
        int result;
        do {
            result = getInteger();
            if (result < min || result > max) {
                System.out.print("Incorrect input! Integer from the range " + min + ".." + max + " is expected. Try again:");
            }
        } while (result < min || result > max);
        return result;
    }

    static double getDouble(){
        Scanner scan = new Scanner(System.in);
        while(true){
            try{
                return scan.nextDouble();
            }
            catch(InputMismatchException e){
                System.out.print("Incorrect input! Double value is expected. Try again: ");
                scan.nextLine();
            }
        }
    }

    static void pressEnterToContinue() {
        System.out.println("Press enter to continue");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
