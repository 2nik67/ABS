package loan;

import client.Client;
import javafx.util.Pair;
import loan.category.Category;
import loan.payment.Payment;
import time.Yaz;

import java.util.ArrayList;
import java.util.List;

public class Loan {

    private boolean shouldAutoPay;
    private boolean isForSale;
    private final String id; //ID of the loan.
    private final double loan; //Amount of the loan.
    private double loanPaid;//Amount of the loan paid.
    private double interestPaid;
    private final double interest;//The interest of the loan.
    private final double interestEveryYaz;//how much to pay from the interest
    private final double loanEveryYaz;//How much to pay from the loan
    private Client owner;//The borrower of the loan.
    private final List<Pair<Client,Double>> loaners;//List of the loaners
    private List<Pair<Client,Double>> updatedLoaners;
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

    public boolean isForSale() {
        return isForSale;
    }

    public void setForSale(boolean forSale) {
        isForSale = forSale;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Loan(String id, double loan, Client borrower, Category loanCategory, int totalYaz, int periodOfYazToPay, int interestEveryYaz) {
        this.shouldAutoPay = true;
        this.id = id;
        this.loan = loan;
        this.isForSale = false;
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
        this.updatedLoaners=null;

    }

    public Loan(Loan otherLoan) {
        this.shouldAutoPay = otherLoan.shouldAutoPay;
        this.isForSale = otherLoan.isForSale;
        this.id = otherLoan.id;
        this.loan = otherLoan.loan;
        this.loanPaid = otherLoan.loanPaid;
        this.interest = otherLoan.interest;
        this.interestPaid = otherLoan.interestPaid;
        this.interestEveryYaz = otherLoan.interestEveryYaz;
        this.loanEveryYaz= otherLoan.loanEveryYaz;
        this.owner = new Client(otherLoan.owner);

        this.loaners = new ArrayList<>();
        if(otherLoan.loaners != null)
            for (int i = 0; i < otherLoan.loaners.size(); i++) {
                this.loaners.add(new Pair<>(new Client(otherLoan.loaners.get(i).getKey()), otherLoan.loaners.get(i).getValue()));
            }

        this.updatedLoaners = new ArrayList<>();
        if(otherLoan.updatedLoaners != null)
            for (int i = 0; i < otherLoan.updatedLoaners.size(); i++) {
                this.updatedLoaners.add(new Pair<>(new Client(otherLoan.updatedLoaners.get(i).getKey()), otherLoan.updatedLoaners.get(i).getValue()));
            }

        this.loanCategory = otherLoan.loanCategory;
        this.status = otherLoan.status;
        this.totalYaz = otherLoan.totalYaz;
        this.missingToActive = otherLoan.missingToActive;
        this.periodOfYazToPay = otherLoan.periodOfYazToPay;
        this.startedYaz = otherLoan.startedYaz;

        this.paymentInfo = new ArrayList<>();
        if (otherLoan.paymentInfo != null)
            for (int i = 0; i < otherLoan.paymentInfo.size(); i++) {
                this.paymentInfo.add(new Payment(otherLoan.paymentInfo.get(i).getPaidYaz(),
                        otherLoan.paymentInfo.get(i).getPartOfLoanPaid(),
                        otherLoan.paymentInfo.get(i).getPartOfInterestPaid()));
            }

        this.interestPercentage = otherLoan.interestPercentage;
        this.activeYaz = otherLoan.activeYaz;
        this.finishYaz = this.getFinishYaz();

        this.latePayments = new ArrayList<>();
            if(otherLoan.latePayments != null)
                for (int i = 0; i < otherLoan.latePayments.size(); i++) {
                    this.latePayments.add(new Pair<>(otherLoan.latePayments.get(i).getKey(),
                            otherLoan.latePayments.get(i).getValue()));
                }
    }

    public void updateLoaners(Client oldClient, Client newClient){
        if (updatedLoaners == null){
            updatedLoaners = new ArrayList<>(this.loaners);
        }
        for (int i = 0; i < updatedLoaners.size(); i++) {
            if (updatedLoaners.get(i).getKey().getName().equals(oldClient.getName())){
                updatedLoaners.add(new Pair<>(newClient, updatedLoaners.get(i).getValue()));
                updatedLoaners.remove(i);
            }
        }
    }

    public void investmentPay(double toPay, Client investor){
        boolean shouldDo =true;
        this.missingToActive-=toPay;
        if(missingToActive==0){
            status=Status.ACTIVE;
            activeYaz=Yaz.getYaz();
            owner.loadMoney(loan, owner.getMoney());
            shouldAutoPay = true;

        }
        else{
            status=Status.PENDING;
        }

        for (int i = 0; i < loaners.size(); i++) {
            if (loaners.get(i).getKey().getName().equals(investor.getName())){
                loaners.add(new Pair<>(investor, loaners.get(i).getValue() + toPay));
                investor.addToLoan(this, toPay);
                loaners.remove(i);
                shouldDo = false;
            }
        }
        if (shouldDo){
            loaners.add(new Pair<>(investor, toPay));
            investor.addLoanToMap(this, toPay);
        }
        if (status.equals(Status.ACTIVE)){
            updatedLoaners = new ArrayList<>(loaners);
        }


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

/*    @Override
    public boolean equals(Object obj) {
        Loan loan=(Loan) obj;
        return this.id.equals(loan.id);
    }*/
    public String getId(){
        return this.id;
    }

    public int getStartedYaz() {
        return startedYaz;
    }

    public int getFinishYaz() {
        return finishYaz;
    }

    public void payPartOfLoan(double toPay) {
        double interest = toPay * interestPercentage/100;
        double partOfLoan = toPay - interest;
        paymentInfo.add(new Payment(Yaz.getYaz(), interest, partOfLoan));
        if(loan+interest - (loanPaid+interestPaid) < toPay){
            payFullLoan();
        }
        for (Pair<Client, Double> loaner : updatedLoaners) {
            double payBack = loaner.getValue() /loan;
            loaner.getKey().loadMoney(payBack * partOfLoan + interest * payBack, loaner.getKey().getMoney());
        }
        loanPaid += partOfLoan;
        interestPaid +=interest;
        owner.loadMoney(-1*(toPay), owner.getMoney());
    }

    public List<Pair<Client, Double>> getUpdatedLoaners() {
        return updatedLoaners;
    }

    public void payLoan() {
        if((Yaz.getYaz()-activeYaz) % periodOfYazToPay == 0){
            paymentInfo.add(new Payment(Yaz.getYaz(), loanEveryYaz, interestEveryYaz ));
            if(owner.getMoney() < loanEveryYaz+interestEveryYaz){
                status=Status.RISK;
                latePayments.add(new Pair<>(Yaz.getYaz(), loanEveryYaz+interestEveryYaz));
                return;

            }
            for (Pair<Client, Double> loaner : updatedLoaners) {
                double payBack = loaner.getValue() /loan;
                loaner.getKey().loadMoney(payBack * loanEveryYaz + interestEveryYaz * payBack, loaner.getKey().getMoney());
                loaner.getKey().addToLoan(this, -1 * payBack * loanEveryYaz);
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
        for (Pair<Integer, Double> latePayment : latePayments) {
            //System.out.println("Payment " + (i+1) + ":");
            //System.out.println("Yaz: " + latePayments.get(i).getKey() + " | "+"Amount: " + latePayments.get(i).getValue());
            sum += latePayment.getValue();
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
        int periodOfLoan = Yaz.getYaz()-startedYaz;
        return (double) periodOfLoan / periodOfYazToPay * (loanEveryYaz + interestEveryYaz);
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public boolean timeToPay(){
        return (Yaz.getYaz() - activeYaz) % periodOfYazToPay == 0;
    }

    public boolean payFullLoan(){
        if (owner.getMoney() < loan + interest - loanPaid - interestPaid){
            return false;
        }
        status = Status.FINISHED;
        finishYaz = Yaz.getYaz();
        for (Pair<Client, Double> loaner : updatedLoaners) {
            double payBack = loaner.getValue() / loan;
            loaner.getKey().loadMoney(payBack * (loan-loanPaid) + (interest-interestPaid) * payBack, loaner.getKey().getMoney());
            loaner.getKey().addToLoan(this, -1 * payBack * (loan-loanPaid));
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
        return (loanPaid + interestPaid < amountOfPayments * (interestEveryYaz + loanEveryYaz));
    }

    public void shouldAutoPay(){
        int yazSinceActive= Yaz.getYaz() - activeYaz;
        int amountOfPayments = yazSinceActive/periodOfYazToPay;
        shouldAutoPay =  timeToPay() && loanPaid + interestPaid == amountOfPayments * (interestEveryYaz + loanEveryYaz);
    }

    public boolean isShouldAutoPay() {
        return shouldAutoPay;
    }

    public double getInterestEveryYaz() {
        return interestEveryYaz;
    }

    public double getLoanEveryYaz() {
        return loanEveryYaz;
    }

    @Override
    public String toString() {
        return id;
    }
}
