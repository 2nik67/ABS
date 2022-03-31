package loan;

import client.Client;
import javafx.util.Pair;
import loan.category.Category;
import loan.payment.Payment;
import time.Yaz;

import java.util.ArrayList;
import java.util.List;

public class Loan {
    private final String id; //ID of the loan.
    private final int loan; //Amount of the loan.
    private double loanPaid;//Amount of the loan paid.
    private final int interest;//The interest of the loan.
    private final int interestEveryYaz;
    private final Client owner;//The borrower of the loan.
    private List<Pair<Client,Integer>> loaners;//List of the loaners //TODO: When i'll have the function for paying loans, update this list too.
    private final Category loanCategory;//Category of the loan.
    private Status status;//Status of the loan. Using ENUM.
    private final int totalYaz; //Total Yaz of Payment.
    private int missingToActive;
    private final int periodOfYazToPay; //how many yaz between payments.
    private final int startedYaz; //Yaz of the loan when started.
    private List<Payment> paymentInfo;//Every payment made and it's yaz.//TODO: When i'll have the function for paying loans, update this list too.
    private int activeYaz;//Yaz when the loan started to be active
    private int finishYaz;//Yaz when the loan finished.

    public Loan(String id, int loan, Client borrower, Category loanCategory, int totalYaz, int periodOfYazToPay, int interestEveryYaz) {
        this.id = id;//Yes
        this.loan = loan;//yes
        this.interestEveryYaz=interestEveryYaz;
        this.owner = borrower;
        this.loanCategory = loanCategory;
        this.totalYaz = totalYaz;
        this.periodOfYazToPay = periodOfYazToPay;
        this.loanPaid=0;
        this.missingToActive=loan;
        this.loaners=new ArrayList<>();
        this.status=Status.NEW;
        this.paymentInfo=new ArrayList<>();
        this.startedYaz=Yaz.getYaz();
        this.interest = interestEveryYaz*(totalYaz/periodOfYazToPay);

    }
    public void payForLoan(int toPay){
        this.missingToActive-=toPay;
        if(loanPaid == loan){
            status=Status.ACTIVE;
        }
        else{
            status=Status.PENDING;
        }
    }
    public Category getLoanCategory() {
        return loanCategory;
    }

    public int getTotalYaz() {
        return totalYaz;
    }

    public Status getStatus() {
        return status;
    }

    public List<Pair<Client,Integer>> getLoaners() {
        return loaners;
    }


    public int howMuchYazToEnd(){
        return  startedYaz+totalYaz-Yaz.getYaz();
    }

    public boolean isActive(){
        return status.equals(Status.ACTIVE);
    }

    public double interestPaid(){
        double amountOfPayments=(double) (this.totalYaz/this.periodOfYazToPay);
        return this.interest/amountOfPayments;
    }
    public void addLoaner(Client client, Integer invest){
        loaners.add(new Pair<>(client, invest));
    }

    public int getLoan() {
        return loan;
    }

    public double getInterest() {
        return interest;
    }


    public void printLoaners(){
        for (Pair<Client, Integer> l: loaners){
            System.out.println(l.getKey().getName() + " paid " + l.getValue().toString() + "\n");
        }
    }
    public Double leftForActive(){
        Double res=new Double(0);
        for (Pair<Client, Integer> l: loaners){
            res+=l.getValue();
        }
        return res;
    }

    public int getActiveYaz() {
        return activeYaz;
    }
    public void printPayments(){

        double interestPaid=0;
        for(Payment ls: paymentInfo){
            ls.printPaymentInfo();
            loanPaid+=ls.getPartOfLoanPaid();
            interestPaid+=ls.getPartOfInterestPaid();
        }
        System.out.println("Total Loan paid (with no interest): " + loanPaid + " | Total interest paid: " + interestPaid + "\n"
        + "Loan left to pay(with no interest): " + (loan-loanPaid) + " | Interest left to pay: " + (interest- interestPaid));

    }

    @Override
    public boolean equals(Object obj) {
        Loan loan=(Loan) obj;
        return this.id.equals(loan.id);
    }
    public String getId(){
        return this.id;
    }
}
