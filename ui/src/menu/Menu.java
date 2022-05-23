package menu;
import Investment.Investment;
import Investment.Investments;
import client.Client;
import client.Clients;
import load.LoadFile;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;
import time.Yaz;

import java.util.ArrayList;
import java.util.List;
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
                "3) Show client's information.\n" +
                "4) Deposit money to an account.\n" +
                "5) Withdraw money from an account.\n" +
                "6) Assign loans to client.\n" +
                "7) Advance time and make payments.\n" +
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
        Menu.userChoice = userChoice;
    }

    public static void methodToCall(){
        Boolean fileLoaded = LoadFile.isFileLoaded();
        Integer intInput = 0, sumToInvest, minYaz, minInterest = 0;
        Double dblInput = 0.0;
        Boolean inputFlag = false;
        Scanner scanner = new Scanner(System.in);
        String path;

        if(!fileLoaded && (userChoice > 1 && userChoice != 8)){ // file input check
            System.out.println("File not loaded, you can't choose option " + userChoice + ".\n");
            return;
        }

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
                    Clients.addMoneyToAccount(intInput - 1, (dblInput*-1));
                else
                    Clients.addMoneyToAccount(intInput - 1, (dblInput*1));
                break;
            case 6:
                intInput = -1;
                List<Category> catChosen = new ArrayList<>();
                Clients.PrintList();
                System.out.println("\nPlease choose client by number.");
                intInput = chooseFromRange(Clients.getClientsSize(), 1);
                Client client = Clients.getClientsList().get(intInput - 1);

                System.out.println("Enter amount to invest.");
                sumToInvest = chooseFromRange((int)client.getMoney(), 1);

                System.out.println("Choose categories: \nEnter one at a time and enter 0 to continue, or just enter 0 to choose all");
                while(!inputFlag && intInput != 0) {
                    Categories.printCategories();
                    System.out.println("0) Continue / All");

                    intInput = chooseFromRange(Categories.getNumOfCategories(), 0);
                    if(intInput != 0){
                        if(!catChosen.contains(Categories.getCategoryByIndex(intInput - 1)))
                            catChosen.add(Categories.getCategoryByIndex(intInput - 1));
                        else
                            System.out.println("This category was already selected!! Choose a different one or press 0 to continue:");
                    }
                }
                inputFlag = false;

                if(catChosen.isEmpty())
                    catChosen = Categories.getCategoryList();

                System.out.println("Enter percentage of minimum interest: (0 for no limit)");
                while(!inputFlag) {
                    if(scanner.hasNext())
                        if(scanner.hasNextInt()) {
                            minInterest = scanner.nextInt();

                            if(minInterest <= 100 && minInterest >= 0)
                                inputFlag = true;
                            else {
                                System.out.println("Interest must be a percentage higher than 0 and lower than 100.");
                                System.out.println("Example: for 23% enter '23', for 5% enter '5'.");
                                scanner.nextLine();
                            }
                        }
                        else {
                            System.out.println("Wrong input! Please enter a number higher than 0 and lower than 100.");
                            scanner.nextLine();
                        }
                }

                System.out.println("Enter minimum Yaz for the entire investment, enter 0 to not have such requirement.");
                minYaz = chooseFromRange(999999999, 0);

                List<Loan> possibleLoans = Investment.fillList(Loans.getLoans(), catChosen, minYaz, minInterest, client, 0);
                if(possibleLoans.isEmpty()){
                    System.out.println("No possible loans for given requirements. \n");
                    return;
                }

                List<Loan> chosenLoans = new ArrayList<>();
                intInput = -1;
                inputFlag = false;

                System.out.println("Choose loans: \nEnter one at a time and enter 0 to continue, or just enter 0 to choose all");
                while (intInput != 0){
                    for (int i = 0; i < possibleLoans.size(); i++) {
                        System.out.println((i + 1) + ") " + "LoanID: "+ possibleLoans.get(i).getId() +" | "+ "Status: " + possibleLoans.get(i).getStatus() + " | "+ "Owner: " + possibleLoans.get(i).getOwner().getName()+ " | "+ "Loan amount: " + possibleLoans.get(i).getLoan());
                    }
                    System.out.println("0) Continue / All");

                    intInput = chooseFromRange(possibleLoans.size(), 0);
                    if(intInput != 0){
                        if(!chosenLoans.contains(possibleLoans.get(intInput - 1)))
                            chosenLoans.add(possibleLoans.get(intInput - 1));
                        else
                            System.out.println("This loan was already selected!! Choose a different one or press 0 to continue:");
                    }

                }

                if(chosenLoans.isEmpty())
                    chosenLoans = possibleLoans;

                Investment.investmentAssigning(chosenLoans, sumToInvest);
                break;
            case 7:

                Yaz.advanceYaz(1);
                Loans.payLoans();
                break;
            case 8:
                System.exit(0);


        }
    }

    private static Integer chooseFromRange(Integer endOfRange, Integer startOfRange){
        Integer intInput = 0;
        Boolean inputFlag = false;
        Scanner scanner = new Scanner(System.in);

        while(!inputFlag) {
            if(scanner.hasNext())
                if(scanner.hasNextInt()) {
                    intInput = scanner.nextInt();

                    if(validityCheck(startOfRange, endOfRange, intInput))
                        inputFlag = true;
                    else {
                        System.out.println("Option out of range: please enter a number between " + startOfRange + " to " + endOfRange);
                        scanner.nextLine();
                    }
                }
                else {
                    System.out.println("Wrong input! Please enter a number between " + startOfRange + " to " + endOfRange);
                    scanner.nextLine();
                }
        }
        scanner.nextLine();

        return intInput;
    }
}
