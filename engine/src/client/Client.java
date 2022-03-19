package client;

import javafx.util.Pair;
import time.Yaz;

import java.util.ArrayList;
import java.util.List;

public class Client {

    //The name of the client.
    private String name;

    //current amount of money.
    private Double money;

    //List of transactions of money, including Yaz.
    private List<Transactions> transactions;

    public Client(String name, double money) {
        this.name = name;
        this.money = money;
        this.transactions = new ArrayList<>();
        this.transactions.add(new Transactions(money, Yaz.getYaz()));
        Clients.addClient(this);
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
        for (int i = 0; i < transactions.size(); i++) {
            transactions.get(i).printTransaction();
        }
    }
}



