package client;

public class Transactions {
    private double moneyChange;
    private int yazOfTransaction;

    public Transactions(double moneyChange, int yazOfTransaction) {
        this.moneyChange = moneyChange;
        this.yazOfTransaction = yazOfTransaction;
    }
    public void printTransaction(){
        System.out.println("Money added: " + this.moneyChange + " | Yaz of transaction: " + this.yazOfTransaction);
    }
}
