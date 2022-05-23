package client;

public class Transaction {
    private final double moneyChange;
    private final Integer yazOfTransaction;
    private final double original;
    private final double afterChange;

    public Transaction(double moneyChange, int yazOfTransaction, double original) {
        this.moneyChange = moneyChange;
        this.yazOfTransaction = yazOfTransaction;
        this.original = original;
        this.afterChange = original + moneyChange;
    }
    public void printTransaction(){
        if(moneyChange < 0){
            System.out.println("Amount of money withdrawn: " +String.format("%.3f", this.moneyChange) + " | Yaz of transaction: " + this.yazOfTransaction
                    + " | " + "Amount of money after transaction: " +String.format("%.3f", (original+moneyChange)));
        }
        else{
            System.out.println("Amount of money deposited: " +String.format("%.3f", this.moneyChange) + " | Yaz of transaction: " + this.yazOfTransaction
                    + " | " + "Amount of money after transaction: " +String.format("%.3f", (original+moneyChange)));
        }
    }

    public double getMoneyChange() {
        return moneyChange;
    }

    public int getYazOfTransaction() {
        return yazOfTransaction;
    }

    public double getOriginal() {
        return original;
    }

    public double getAfterChange() {
        return afterChange;
    }
}
