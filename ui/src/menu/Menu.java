package menu;

import java.util.Scanner;
import test.test;

public class Menu {
    public static void main(String[] args) {
        test.print("World");
    }
    void printMenu(){
        System.out.println("1) Load File.\n" +
                "2) Show information about current loans.\n" +
                "3) Show client's status.\n" +
                "4) Deposit money to an account.\n" +
                "5) Withdraw money from an account.\n" +
                "6) Activation of inlay.\n" +
                "7) Promote time and making payments.\n" +
                "8) Exit.\n");
    }
    boolean validityCheck(Integer s){
        if (s<1 || s>8) {
            System.out.println("Please enter a valid option");
            return false;
        }
        return true;
    }
}
