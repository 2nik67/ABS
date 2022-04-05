package client;

public class Transaction {
    private final double moneyChange;
    private final int yazOfTransaction;
    private final double original;

    public Transaction(double moneyChange, int yazOfTransaction, double original) {
        this.moneyChange = moneyChange;
        this.yazOfTransaction = yazOfTransaction;
        this.original = original;
    }
    public void printTransaction(){
        if(moneyChange > 0){
            System.out.println("Amount of money withdrawn: " +String.format("%.3f", this.moneyChange) + " | Yaz of transaction: " + this.yazOfTransaction
                    + " | " + "Amount of money after transaction: " +String.format("%.3f", (original+moneyChange)));
        }
        else{
            System.out.println("Amount of money deposited: " +String.format("%.3f", this.moneyChange) + " | Yaz of transaction: " + this.yazOfTransaction
                    + " | " + "Amount of money after transaction: " +String.format("%.3f", (original+moneyChange)));
        }
    }
}
