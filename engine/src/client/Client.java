package client;


import javafx.util.Pair;
import loan.Loan;
import loan.Loans;
import time.Yaz;

import java.util.*;

public class Client {

    //The name of the client.
    private final String name;

    //current amount of money.
    private double money;

    //List of transactions of money, including Yaz.
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    //List of investments the client is in.
    private final Map<String, InvestmentForSale> investments = new HashMap<>();


    public Map<String, InvestmentForSale> getInvestments() {
        return investments;
    }

    public void addToLoan(Loan loan, double investment){
        investments.get(loan.getId()).add(investment);
    }

    public void addLoanToMap(Loan loan, double investment){
        investments.put(loan.getId(), new InvestmentForSale(investment, false));
    }
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
    public boolean isInvestor(Loan loan){
        List<Pair<Client,Double>> clients = loan.getLoaners();
        for (Pair<Client, Double> client : clients) {
            if (client.getKey().getName().equals(name))
                return true;
        }
        return false;
    }

    public int numOfOpenLoans(){
        int res = 0;
        List<Loan> loans = Loans.getLoans();
        for (Loan loan : loans) {
            if (loan.getOwner().getName().equals(name)) {
                res++;
            }
        }
        return res;
    }

    public void fixInvestments(Client buyingFromm, Loan loan){
        if (this.investments.containsKey(loan.getId())){
            investments.get(loan.getId()).add(buyingFromm.getInvestments().get(loan.getId()).getInvestment());

        }else{
            investments.put(loan.getId(),new InvestmentForSale(buyingFromm.getInvestments().get(loan.getId()).getInvestment(), false));

        }
        buyingFromm.loadMoney(buyingFromm.getInvestments().get(loan.getId()).getInvestment(), buyingFromm.getMoney());
        this.loadMoney(buyingFromm.getInvestments().get(loan.getId()).getInvestment() * -1 , this.money);
        buyingFromm.getInvestments().remove(loan.getId());
        loan.updateLoaners(buyingFromm, this);

    }

}



