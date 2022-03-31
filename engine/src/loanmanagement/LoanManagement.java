package loanmanagement;

import client.Client;
import javafx.util.Pair;

import java.util.ArrayList;

public abstract class LoanManagement {
    private static ArrayList<Pair<Client, Integer>> investors;

    public static void addInvestor(Client client, Integer investment){
        investors.add(new Pair<>(client, investment));
    }


}
