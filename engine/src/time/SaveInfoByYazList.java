package time;

import client.Client;
import client.Clients;
import loan.Loan;
import loan.Loans;

import java.util.ArrayList;
import java.util.List;

public class SaveInfoByYazList {
    List <SaveInfoPerYaz> archive = new ArrayList<>(2);

    public SaveInfoByYazList() {
        archive.add(null);
    }

    public void yazWasAdvanced(int lastYaz){
        SaveInfoPerYaz saveInfoPerYaz = new SaveInfoPerYaz(Loans.getLoans(), Clients.getClientsList(), Yaz.getYaz());
        if(lastYaz > archive.size())
            archive.add(null);

        if(lastYaz < archive.size())
            archive.remove(lastYaz);
        archive.add(lastYaz , saveInfoPerYaz);
        //archive.get(lastYaz) = saveInfoPerYaz;
    }
}
