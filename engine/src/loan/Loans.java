package loan;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public abstract class Loans {
    private static List<Loan> loans= new ArrayList<>();

    public static void setLoans(List<Loan> loansList) {
        loans=new ArrayList<>(loansList);
    }

    public static ObservableList<Loan> getObservableLoans(){
        return (ObservableList<Loan>) loans;
    }
    public static void addLoan(Loan newLoan) { loans.add(newLoan); }

    public static List<Loan> getLoans() {
        return loans;
    }

    public static void printLoans(){

        for(Loan l: loans){
            if(l.getStatus().equals(Status.NEW)){
                System.out.println("\nLoanID: "+ l.getId() +" | "+ "Status: NEW" + " | "+ "Owner: " + l.getOwner().getName()+ " | "+ "Loan amount: " + l.getLoan());

            }
            else if (l.getStatus().equals(Status.PENDING)){
                System.out.println("\nLoanID: "+ l.getId() +" | "+ "Status: PENDING" + " | "+ "Owner: " + l.getOwner().getName() + " | "+ "Loan amount: " + l.getLoan());
                l.printLoaners();
                System.out.println("Missing till active: " + String.format("%.2f", l.getMissingToActive()) );
                
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

    public static Loan getLoanByID(String ID){
        for (Loan loan : loans) {
            if (loan.getId().equals(ID))
                return loan;
        }
        return null;
    }


    public static void checkForRiskLoans() {
        for (Loan loan : loans){
            loan.checkIfLoanIsInRisk();
        }
    }

    public static void checkForAutoPay() {
        for (Loan loan : loans) {
            loan.shouldAutoPay();
        }
    }
}

