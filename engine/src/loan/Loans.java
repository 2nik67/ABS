package loan;

import java.util.ArrayList;
import java.util.List;

public abstract class Loans {
    private static List<Loan> loans= new ArrayList<>();

    public static void setLoans(List<Loan> loansList) {
        loans=new ArrayList<>(loansList);
    }

    public static List<Loan> getLoans() {
        return loans;
    }

    public static void printLoans(){

        for(Loan l: loans){
            if(l.getStatus().equals(Status.NEW)){
                System.out.println("\nLoanID: "+ l.getId() +" | "+ "Status: NEW" + " | "+ "Owner: " + l.getOwner().getName()+ " | "+ "Loan amount: " + l.getLoan());
                l.printPayments();



            }
            else if (l.getStatus().equals(Status.PENDING)){
                System.out.println("\nLoanID: "+ l.getId() +" | "+ "Status: PENDING" + " | "+ "Owner: " + l.getOwner().getName() + " | "+ "Loan amount: " + l.getLoan());
                l.printLoaners();
                l.printPayments();
            }
            else if(l.getStatus().equals(Status.ACTIVE)){
                System.out.println("\nLoanID: "+ l.getId() +" | "+ "Status: ACTIVE" + " | "+ "Owner: " + l.getOwner().getName()+ " | "+ "Loan amount: " + l.getLoan());
                l.printLoaners();
                l.printPayments();
            }
            else if(l.getStatus().equals(Status.FINISHED)){
                System.out.println("\nLoanID: "+ l.getId() +" | "+ "Status: FINISHED" + " | "+ "Owner: " + l.getOwner().getName()+ " | "+ "Loan amount: " + l.getLoan() + " | " + "Finished in: "  + l.getFinishYaz());

                l.printLoaners();
                l.printPayments();
            }
            else if (l.getStatus().equals(Status.RISK)){
                System.out.println("\nLoanID: "+ l.getId() +" | "+ "Status: RISK" + " | "+ "Owner: " + l.getOwner().getName()+ " | "+ "Loan amount: " + l.getLoan() + " | " + "Finished in: "  + l.getFinishYaz());

                l.printLoaners();
                l.printPayments();
                l.printLatePayments();
            }
        }
    }


    public static void payLoans(){
        for (Loan loan : loans) {
            if (loan.getStatus().equals(Status.ACTIVE) || loan.getStatus().equals(Status.RISK) ) {
                loan.payLoan();
            }
        }
    }


}

