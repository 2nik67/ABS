package loan;

import java.util.ArrayList;
import java.util.List;

public abstract class Loans {
    private static List<Loan> loans= new ArrayList<>();

    public static void setLoans(List<Loan> loansList) {
        loans=new ArrayList<>(loansList);
    }

    /*public static void addLoan(Loan loan){
        loans.add(loan);
    }*/
    public static void printLoans(){

        for(Loan l: loans){
            //List <Pair<Client, Double>> listOfLoaners=l.getLoaners();
            if(l.getStatus().equals(Status.NEW)){
                System.out.println("Status: NEW\n"+"Loaners:");
                l.printLoaners();
                System.out.println("Total paid:" + l.leftForActive().toString() + " | How much left to pay: " + (l.getLoan()-l.leftForActive()));

            }
            else if (l.getStatus().equals(Status.ACTIVE)){
                System.out.println("Status: ACTIVE\n"+"Loaners\n");
                l.printLoaners();
                System.out.println("Loan is active since " + l.getActiveYaz() + " yaz");
                l.printPayments();

            }
        }
    }



    /*public static void resetLoans(){
        loans.clear();
    }*/


}

