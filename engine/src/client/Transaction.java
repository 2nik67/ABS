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
        System.out.println("Money added: " +String.format("%.3f", this.moneyChange) + " | Yaz of transaction: " + this.yazOfTransaction
                + " | " + "Amount of money after transaction: " +String.format("%.3f", (original+moneyChange)));
    }
}
