package time;

import javafx.beans.property.IntegerProperty;
import loan.Loans;

public abstract class Yaz {
    private static int yaz;

    public static int getYaz() {
        return yaz;
    }

    public static void advanceYaz(int yaz) {
        Yaz.yaz += yaz;
        Loans.checkForRiskLoans();
    }


}
