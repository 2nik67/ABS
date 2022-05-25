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
    private final double loan; //Amount of the loan.
    private double loanPaid;//Amount of the loan paid.
    private double interestPaid;
    private final double interest;//The interest of the loan.
    private final double interestEveryYaz;//how much to pay from the interest
    private final double loanEveryYaz;//How much to pay from the loan
    private final Client owner;//The borrower of the loan.
    private final List<Pair<Client,Double>> loaners;//List of the loaners
    private final Category loanCategory;//Category of the loan.
    private Status status;//Status of the loan. Using ENUM.
    private final int totalYaz; //Total Yaz of Payment.
    private double missingToActive;//How much is missing to be active
    private final int periodOfYazToPay; //how many yaz between payments.
    private final int startedYaz; //Yaz of the loan when started.
    private final List<Payment> paymentInfo;//Every payment made and it's yaz.
    private final int interestPercentage;
    private int activeYaz;//Yaz when the loan started to be active
    private int finishYaz;//Yaz when the loan finished.
    private final List<Pair<Integer, Double>> latePayments;

    public Loan(String id, double loan, Client borrower, Category loanCategory, int totalYaz, int periodOfYazToPay, int interestEveryYaz) {
        this.id = id;
        this.loan = loan;

        this.owner = borrower;
        this.loanCategory = loanCategory;
        this.totalYaz = totalYaz;
        this.periodOfYazToPay = periodOfYazToPay;
        this.loanPaid=0;
        this.interestPaid=0;
        this.missingToActive=loan;
        this.loaners=new ArrayList<>();
        this.status=Status.NEW;
        this.paymentInfo=new ArrayList<>();
        this.startedYaz=Yaz.getYaz();
        this.interestPercentage=interestEveryYaz;
        this.interest = loan*((double)interestPercentage/100);
        this.loanEveryYaz=loan/(double)(totalYaz/periodOfYazToPay);
        this.interestEveryYaz= (int) (interest/(totalYaz/periodOfYazToPay));
        this.latePayments=new ArrayList<>();

    }
    public void investmentPay(double toPay, Client investor){
        this.missingToActive-=toPay;
        if(missingToActive==0){
            status=Status.ACTIVE;
            activeYaz=Yaz.getYaz();
            owner.loadMoney(loan, owner.getMoney());
        }
        else{
            status=Status.PENDING;
        }
        loaners.add(new Pair<>(investor, toPay));

    }

    public int getInterestPercentage() {
        return interestPercentage;
    }

    public double getMissingToActive() {
        return missingToActive;
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

    public List<Pair<Client,Double>> getLoaners() {
        return loaners;
    }

    public Client getOwner() {
        return owner;
    }

    public int howMuchYazToEnd(){
        return  startedYaz+totalYaz-Yaz.getYaz();
    }

    public boolean isActive(){
        return status.equals(Status.ACTIVE);
    }

    public void addLoaner(Client client, Double invest){
        loaners.add(new Pair<>(client, invest));
    }

    public double getLoan() {
        return loan;
    }

    public double getInterest() {
        return interest;
    }


    public void printLoaners(){
        for (Pair<Client, Double> l: loaners){
            System.out.println(l.getKey().getName() + " paid " +String.format("%.2f",l.getValue()));
        }
    }
    public Double leftForActive(){
        double res=  0;
        for (Pair<Client, Double> l: loaners){
            res+=l.getValue();
        }
        return res;
    }

    public int getActiveYaz() {
        return activeYaz;
    }
    public void printPayments(){


        /*for(Payment ls: paymentInfo){
            ls.printPaymentInfo();
            loanPaid+=ls.getPartOfLoanPaid();
            interestPaid+=ls.getPartOfInterestPaid();
        }*/
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

    public int getStartedYaz() {
        return startedYaz;
    }

    public int getFinishYaz() {
        return finishYaz;
    }

    public void payLoan() {
        if((Yaz.getYaz()-activeYaz) % periodOfYazToPay == 0){
            paymentInfo.add(new Payment(Yaz.getYaz(), loanEveryYaz, interestEveryYaz ));
            if(owner.getMoney() < loanEveryYaz+interestEveryYaz){
                status=Status.RISK;
                latePayments.add(new Pair<>(Yaz.getYaz(), loanEveryYaz+interestEveryYaz));
                return;

            }
            for (Pair<Client, Double> loaner : loaners) {
                double payBack = loaner.getValue() /loan;
                loaner.getKey().loadMoney(payBack * loanEveryYaz + interestEveryYaz * payBack, loaner.getKey().getMoney());


            }
            loanPaid += loanEveryYaz;
            interestPaid +=interestEveryYaz;
            owner.loadMoney(-1*(loanEveryYaz+interestEveryYaz), owner.getMoney());

        }
        if(loanPaid==loan){
            status=Status.FINISHED;
            finishYaz=Yaz.getYaz();
        }

    }

    public void printLatePayments() {
        System.out.println("Number of payments missed: " + latePayments.size());
        int sum=0;
        for (int i = 0; i < latePayments.size(); i++) {
            //System.out.println("Payment " + (i+1) + ":");
            //System.out.println("Yaz: " + latePayments.get(i).getKey() + " | "+"Amount: " + latePayments.get(i).getValue());
            sum+=latePayments.get(i).getValue();
        }
        System.out.println("Total amount missed: " + sum);
    }

    public int getAmountOfMissedPayments(){
        return latePayments.size();
    }
    public double getLoanPaid() {
        return loanPaid;
    }

    public double getTotalAmountMissed(){
        double sum=0;
        for (int i = 0; i < latePayments.size(); i++) {
            sum+=latePayments.get(i).getValue();
        }
        return sum;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public boolean timeToPay(){
        return (Yaz.getYaz() - activeYaz) % periodOfYazToPay == 0;
    }

    public boolean payFullLoan(){
        if (owner.getMoney() < loan - loanPaid){
            return false;
        }
        status = Status.FINISHED;
        finishYaz = Yaz.getYaz();
        for (Pair<Client, Double> loaner : loaners) {
            double payBack = loaner.getValue() / loan;
            loaner.getKey().loadMoney(payBack * (loan-loanPaid) + interestEveryYaz * payBack, loaner.getKey().getMoney());
        }
        owner.loadMoney(-1*((loan-loanPaid) + (double)interestPercentage/100 *(loan-loanPaid)), owner.getMoney());
        return true;
    }

    public void checkIfLoanIsInRisk(){
        if(this.status.equals(Status.ACTIVE)){
            if(checkHowMuchShouldBePaid()){
                status = Status.RISK;
            }
        }
    }

    private boolean checkHowMuchShouldBePaid(){
        int yazSinceActive= Yaz.getYaz() - activeYaz;
        int amountOfPayments = yazSinceActive/periodOfYazToPay;
        return !(loanPaid < amountOfPayments * (interestPaid + loanPaid));
    }
}
