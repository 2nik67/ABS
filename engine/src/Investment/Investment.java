package Investment;

import client.Client;
import loan.Loan;
import loan.Status;
import loan.category.Category;

import java.util.ArrayList;
import java.util.List;

public class Investment {


    private static List<Category> categories;
    private static int minimumYaz;
    private static double minimumInterest;
    private static Client investor;
    private static double investment;



    public static void investmentAssigning(List<Loan> loans,double investment){
        Investment.investment=investment;
        List<Loan> tempLoans= new ArrayList<>(loans);
        double payEachLoan;
        double minLoan = getMinLoan(loans);
        double temp=investment;
        while(temp>0){

            payEachLoan = Math.min(minLoan, temp / tempLoans.size());
            if (tempLoans.isEmpty()){
                System.out.println("Could only invest " + String.format("%.2f",(investment-temp)));
                Investment.investment=investment-temp;
                break;
            }
            for (Loan loan : tempLoans) {
                loan.investmentPay(payEachLoan, investor);
                temp -= payEachLoan;
                if (temp<0.002) {
                    investor.loadMoney((-1*investment), investor.getMoney());
                    return;
                }
            }
            tempLoans=fillList(tempLoans, categories, minimumYaz, minimumInterest, investor,0);
            minLoan = getMinLoan(tempLoans);

        }

        investor.loadMoney((-1*Investment.investment), investor.getMoney());

    }

    private static double getMinLoan(List<Loan> possibleLoans) {
        double min=100000;
        for (Loan possibleLoan : possibleLoans) {
            if (possibleLoan.getMissingToActive() < min)
                min = possibleLoan.getMissingToActive();
        }
        return min;
    }

    public static List <Loan> fillList(List<Loan> loans, List<Category> categories, int yaz, double minimumInterest, Client client, int maxOpenLoans) {
        List <Loan> possibleLoan = new ArrayList<>();
        for (Loan allLoan : loans) {
            if (validLoan(allLoan, categories,yaz, minimumInterest, client, maxOpenLoans)) {
                possibleLoan.add(allLoan);
            }
        }
        return possibleLoan;
    }

    private static boolean validLoan(Loan loan, List<Category> categories, int yaz, double minimumInterest, Client client, int maxOpenLoans) {
        Investment.categories=new ArrayList<>(categories);
        Investment.investor=client;
        Investment.minimumInterest=minimumInterest;
        Investment.minimumYaz=yaz;
        if (loan.getStatus().equals(Status.RISK) || loan.getStatus().equals(Status.ACTIVE) || loan.getStatus().equals(Status.FINISHED)){
            return false;
        }
        if(loan.getOwner().equals(client)){
            return false;
        }
        if (!categories.isEmpty()){
            boolean exist=false;
            for (Category category : categories) {
                if (category.equals(loan.getLoanCategory())) {
                    exist = true;
                    break;
                }
            }
            if (!exist){
                return false;
            }

        }
        if(yaz != 0){
            if(yaz > loan.getTotalYaz()){
                return false;
            }
        }
        if (minimumInterest != 0){
            return !(minimumInterest > (loan.getInterestPercentage()));
        }
        if (loan.getOwner().numOfOpenLoans() > maxOpenLoans && maxOpenLoans != 0){
            return false;
        }
        return true;
    }

}
