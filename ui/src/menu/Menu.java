package menu;
import client.Client;
import client.Clients;
import load.LoadFile;
import loan.Loans;
import time.Yaz;

import java.util.Scanner;


public abstract class Menu {
    private static boolean exit=false;
    private static int userChoice;



    public static void getUsersInput(){
        Integer currentInput = 0;
        Boolean inputFlag = false;
        Scanner scanner = new Scanner(System.in);


        while(!inputFlag) {
            if(scanner.hasNext())
                if(scanner.hasNextInt()) {
                    currentInput = scanner.nextInt();

                    if(validityCheck(1, 8, currentInput))
                        inputFlag = true;
                    else {
                        System.out.println("Option out of range: please enter a number between 1 to 8");
                        scanner.nextLine();
                    }
                }
                else {
                    System.out.println("Wrong input! Please enter a single digit number between 1 to 8.");
                    scanner.nextLine();
                }
        }

        userChoice=currentInput;
    }

    public static void printMenu(){
        System.out.println("Current Yaz:" + Yaz.getYaz()+"\n" +
                "Please choose one option:\n" +
                "1) Load File.\n" +
                "2) Show information about current loans.\n" +
                "3) Show client's status.\n" +
                "4) Deposit money to an account.\n" +
                "5) Withdraw money from an account.\n" +
                "6) Assign loans to client.\n" +
                "7) Advance time and making payments.\n" +
                "8) Exit.\n");
    }

    private static boolean validityCheck(Integer start, Integer end, Integer input){
        if (input<start || input>end) {
            // System.out.println("Please enter a valid option\n");
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
        Integer intInput = 0;
        Double dblInput = 0.0;
        Boolean inputFlag = false;
        Scanner scanner = new Scanner(System.in);
        String path;

        switch (userChoice){
            case 1:
                System.out.println("Please enter full path for XML file:\n");
                path=scanner.nextLine();
                LoadFile.setPath(path);
                LoadFile.readFile();
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
                while(!inputFlag) {
                    if(scanner.hasNext())
                        if(scanner.hasNextInt()) {
                            intInput = scanner.nextInt();

                            if(validityCheck(1, Clients.getClientsSize(), intInput))
                                inputFlag = true;
                            else {
                                System.out.println("Option out of range: please enter a number between 1 to " + Clients.getClientsSize());
                                scanner.nextLine();
                            }
                        }
                        else {
                            System.out.println("Wrong input! Please enter a number between 1 to " + Clients.getClientsSize());
                            scanner.nextLine();
                        }
                }
                scanner.nextLine();
                inputFlag = false;

                System.out.println("Please enter deposit amount.");
                while(!inputFlag) {
                    if(scanner.hasNext())
                        if(scanner.hasNextDouble()) {
                            dblInput = scanner.nextDouble();

                            if(dblInput > 0)
                                inputFlag = true;
                            else {
                                System.out.println("Illegal deposit, please enter a positive number.");
                                scanner.nextLine();
                            }
                        }
                        else {
                            System.out.println("Wrong input! Please enter a positive number.");
                            scanner.nextLine();
                        }
                }
                scanner.nextLine();

                //TODO: move to a dedicated input handling class.

                Clients.addMoneyToAccount(intInput - 1, dblInput);//TODO:fix add money method
                break;
            case 5:
                Clients.PrintList();
                System.out.println("\nPlease choose client by number.");
                while(!inputFlag) {
                    if(scanner.hasNext())
                        if(scanner.hasNextInt()) {
                            intInput = scanner.nextInt();

                            if(validityCheck(1, Clients.getClientsSize(), intInput))
                                inputFlag = true;
                            else {
                                System.out.println("Option out of range: please enter a number between 1 to " + Clients.getClientsSize());
                                scanner.nextLine();
                            }
                        }
                        else {
                            System.out.println("Wrong input! Please enter a number between 1 to " + Clients.getClientsSize());
                            scanner.nextLine();
                        }
                }
                scanner.nextLine();
                inputFlag = false;

                System.out.println("Please enter withdrawal amount.");
                while(!inputFlag) {
                    if(scanner.hasNext())
                        if(scanner.hasNextDouble()) {
                            dblInput = scanner.nextDouble();

                            if(dblInput != 0 && Clients.getBalanceOfClient(intInput - 1) >= Math.abs(dblInput))
                                inputFlag = true;
                            else {
                                System.out.println("Illegal deposit, please enter a number different from 0 and make sure that the client has enough balance in their account.");
                                System.out.println("Current balance in the account: " + Clients.getBalanceOfClient(intInput - 1));
                                scanner.nextLine();
                            }
                        }
                        else {
                            System.out.println("Wrong input! Please enter a number different from 0.");
                            scanner.nextLine();
                        }
                }
                scanner.nextLine();
                //TODO: move to a dedicated input handling class.

                if(dblInput > 0)
                    Clients.addMoneyToAccount(intInput - 1, dblInput*-1);
                else
                    Clients.addMoneyToAccount(intInput - 1, dblInput);
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
