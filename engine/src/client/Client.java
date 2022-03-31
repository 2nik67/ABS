package client;

import time.Yaz;

import java.util.ArrayList;
import java.util.List;

public class Client {

    //The name of the client.
    private final String name;

    //current amount of money.
    private Double money;

    //List of transactions of money, including Yaz.
    private List<Transactions> transactions;

    public Client(String name, double money) {
        this.name = name;
        this.money = money;
        this.transactions = new ArrayList<>();
        this.transactions.add(new Transactions(money, Yaz.getYaz()));

    }

    public String getName() {
        return name;
    }

    public Double getMoney() {
        return money;
    }
    public void loadMoney(Double moneyToAdd ){
        this.money+=moneyToAdd;
        transactions.add(new Transactions(moneyToAdd, Yaz.getYaz()));
    }

    public void printPaymentsList(){
        for (Transactions transaction : transactions) {
            transaction.printTransaction();
        }
    }

    @Override
    public boolean equals(Object obj) {
        Client client=(Client) obj;
        return this.getName().equalsIgnoreCase(client.getName());
    }
}



