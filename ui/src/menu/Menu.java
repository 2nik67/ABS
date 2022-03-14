package menu;

import java.util.Scanner;


public class Menu {

    private Integer userChoice;

    public static void printMenu(){
        System.out.println("Please choose one option:\n" +
                "1) Load File.\n" +
                "2) Show information about current loans.\n" +
                "3) Show client's status.\n" +
                "4) Deposit money to an account.\n" +
                "5) Withdraw money from an account.\n" +
                "6) Activation of inlay.\n" +
                "7) Promote time and making payments.\n" +
                "8) Exit.\n");
    }
    public boolean validityCheck(Integer s){
        if (s<1 || s>8) {
            System.out.println("Please enter a valid option");
            return false;
        }

        return true;
    }

    public Integer getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(Integer userChoice) {
        this.userChoice = userChoice;
    }
}
