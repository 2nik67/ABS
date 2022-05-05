package client;


import time.Yaz;

import java.util.ArrayList;

public class Client {

    //The name of the client.
    private final String name;

    //current amount of money.
    private double money;

    //List of transactions of money, including Yaz.
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    //List of investments the client is in.





    public Client(String name, double money) {
        this.name = name;
        this.money = money;
        this.transactions.add(new Transaction(money, Yaz.getYaz(), 0));
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }
    public void loadMoney(double moneyToAdd , double original){
        this.money+=moneyToAdd;
        transactions.add(new Transaction(moneyToAdd, Yaz.getYaz(), original));
    }

    public void printPaymentsList(){
        for (Transaction transaction : transactions) {
            transaction.printTransaction();
        }
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object obj) {
        Client client=(Client) obj;
        return this.getName().equalsIgnoreCase(client.getName());
    }
}



