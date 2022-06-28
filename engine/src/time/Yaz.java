package time;

import javafx.beans.property.IntegerProperty;
import loan.Loans;

import java.util.ArrayList;
import java.util.List;

public abstract class Yaz {
    private static int yaz = 0;
    private static SaveInfoByYazList archive = new SaveInfoByYazList();

    public static int getYaz() {
        return yaz;
    }

    public static void advanceYaz(int yaz) {
        archive.yazWasAdvanced();
        Yaz.yaz += yaz;
        Loans.checkForRiskLoans();
    }


}
