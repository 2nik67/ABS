package time;

import client.Client;
import loan.Loan;

import java.util.List;

public class SaveInfoPerYaz {
    private List <Loan> loans;
    private List <Client> clients;
    int yazSaved;

    public SaveInfoPerYaz(List<Loan> loans, List<Client> clients, int yazSaved) {
        this.loans = loans;
        this.clients = clients;
        this.yazSaved = yazSaved;
    }
}