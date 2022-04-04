package client;

public class Transactions {
    private final double moneyChange;
    private final int yazOfTransaction;
    private final double original;

    public Transactions(double moneyChange, int yazOfTransaction, double original) {
        this.moneyChange = moneyChange;
        this.yazOfTransaction = yazOfTransaction;
        this.original = original;
    }
    public void printTransaction(double money){
        System.out.println("Money added: " +String.format("%.3f", this.moneyChange) + " | Yaz of transaction: " + this.yazOfTransaction
                + " | " + "Amount of money after transaction: " +String.format("%.3f", (original+moneyChange)));
    }
}
