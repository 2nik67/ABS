package client;


import time.Yaz;

import java.util.ArrayList;

public class Client {

    //The name of the client.
    private final String name;

    //current amount of money.
    private int money;

    //List of transactions of money, including Yaz.
    private ArrayList<Transactions> transactions = new ArrayList<>();

    //List of investments the client is in.





    public Client(String name, int money) {
        this.name = name;
        this.money = money;
        this.transactions.add(new Transactions(money, Yaz.getYaz()));
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }
    public void loadMoney(int moneyToAdd ){
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


