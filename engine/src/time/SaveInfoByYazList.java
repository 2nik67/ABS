package time;

import client.Client;
import client.Clients;
import loan.Loan;
import loan.Loans;

import java.util.ArrayList;
import java.util.List;

public class SaveInfoByYazList {
    List <SaveInfoPerYaz> archive = new ArrayList<>();


    public void yazWasAdvanced(){
        SaveInfoPerYaz saveInfoPerYaz = new SaveInfoPerYaz(Loans.getLoans(), Clients.getClientsList(), Yaz.getYaz());
        archive.add(saveInfoPerYaz);
    }
}
