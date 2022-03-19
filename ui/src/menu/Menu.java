package menu;

import client.Client;
import client.Clients;
import loan.Loans;
import time.Yaz;

import java.util.Scanner;


public abstract class Menu {
    private static boolean exit=false;
    private static int userChoice;


    public static void getUsersInput(){

        Scanner scanner=new Scanner(System.in);
        while(!scanner.hasNextInt() /*|| !validityCheck(scanner.nextInt())*/){ //TODO: fix loop.
            System.out.println("Please enter a whole number between 1-8");
            scanner=new Scanner(System.in);
        }
        userChoice=scanner.nextInt();
    }
    public static void printMenu(){
        System.out.println("Current Yaz:" + Yaz.getYaz()+"\n" +
                "Please choose one option:\n" +
                "1) Load File.\n" +
                "2) Show information about current loans.\n" +
                "3) Show client's status.\n" +
                "4) Deposit money to an account.\n" +
                "5) Withdraw money from an account.\n" +
                "6) Activation of inlay.\n" +
                "7) Promote time and making payments.\n" +
                "8) Exit.\n");
    }
    private static boolean validityCheck(Integer s){
        if (s<1 || s>8) {
            System.out.println("Please enter a valid option\n");
            return false;
        }

        return true;
    }

    public Integer getUserChoice() {
        return userChoice;
    }

    public static boolean isExit() {
        return exit;
    }

    public void setUserChoice(Integer userChoice) {
        this.userChoice = userChoice;
    }
    public static void methodToCall(){
        switch (userChoice){
            case 1:
                //TODO: Load file.
                break;
            case 2:
                Loans.printLoans();
                break;
            case 3:
                Clients.printPayments();
                break;
            case 4:
                Clients.PrintList();
                System.out.println("\nPlease choose client by number.");
                //TODO: check if the choice is valid, add numbers to the print list function and add scanner.
                Scanner s1 = new Scanner(System.in);
                Scanner s2 = new Scanner(System.in);
                Clients.addMoneyToAccount(s1.nextInt(), s2.nextDouble());//TODO:change index and money
                break;
            case 5:
                Clients.PrintList();
                System.out.println("\nPlease choose client by number.");
                //TODO: check if the choice is valid, add numbers to the print list function and add scanner.
                //Clients.addMoneyToAccount(0, 0*-1);//TODO:change index and money
                break;
            case 6:
                break;//TODO
            case 7:
                System.out.println("Choose a whole and positive number: ");
                //TODO:scanner
                Yaz.advanceYaz(1);//TODO: change 0.
                break;
            case 8:
                System.exit(0);


        }
    }
}
