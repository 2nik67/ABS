package time;

import javafx.beans.property.IntegerProperty;
import loan.Loans;

import java.util.ArrayList;
import java.util.List;

public abstract class Yaz {
    private static int yaz = 1;
    private static SaveInfoByYazList archive = new SaveInfoByYazList();
    private static boolean isRewindActive = false;

    public static int getYaz() {
        return yaz;
    }

    public static void advanceYaz(int yaz) {
        archive.yazWasAdvanced();
        Yaz.yaz += yaz;
        Loans.checkForRiskLoans();
    }

    public static boolean isIsRewindActive() {
        return isRewindActive;
    }

    public static void setIsRewindActive(boolean isRewindActive) {
        Yaz.isRewindActive = isRewindActive;
    }
}
