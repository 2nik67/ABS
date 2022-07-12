package time;

import client.Client;
import loan.Loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveInfoPerYaz {
    private List <Loan> loans = new ArrayList<>();
    private List <Client> clients = new ArrayList<>();
    int yazSaved;

    public SaveInfoPerYaz(List<Loan> loans, List<Client> clients, int yazSaved) {
        //this.loans = new ArrayList<>(loans);
        //this.clients = new ArrayList<>(clients);
        for (int i = 0; i < loans.size(); i++) {
            this.loans.add(new Loan(loans.get(i)));
        }

        for (int i = 0; i < clients.size(); i++) {
            this.clients.add(new Client(clients.get(i)));
        }

        this.yazSaved = yazSaved;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<Client> getClients() {
        return clients;
    }
}
