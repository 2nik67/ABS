package Investment;

import java.util.ArrayList;

public abstract class Investments {
    private static ArrayList<Investment> investmentList = new ArrayList<>();

    public static void addInvestment(Investment investment){
        investmentList.add(investment);
    }


}
