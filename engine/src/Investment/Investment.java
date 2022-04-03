package Investment;

import client.Client;
import loan.Loan;
import loan.Loans;
import loan.Status;
import loan.category.Categories;
import loan.category.Category;

import java.util.ArrayList;
import java.util.List;

public class Investment {


    private static List<Category> categories;
    private static int minimumYaz;
    private static double minimumInterest;
    private static Client investor;
    private static int investment;



    public static void investmentAssigning(List<Loan> loans,int investment){
        Investment.investment=investment;
        investor.loadMoney((-1*investment), investor.getMoney());
        List<Loan> tempLoans= new ArrayList<>(loans);
        double payEachLoan;
        double minLoan = getMinLoan(loans);
        double temp=investment;

        while(temp>0){

            if (minLoan > temp){
                payEachLoan = temp/loans.size();
            }
            else{
                payEachLoan=minLoan;
            }
            if (loans.isEmpty()){
                return;
            }
            for (Loan loan : loans) {
                loan.investmentPay(payEachLoan, investor);
                temp -= payEachLoan;
                if (temp == 0) {
                    return;
                }
            }
            tempLoans=fillList(categories, minimumYaz, minimumInterest, investor);
            minLoan = getMinLoan(tempLoans);

        }



    }

    private static double getMinLoan(List<Loan> possibleLoans) {
        double min=100000;
        for (Loan possibleLoan : possibleLoans) {
            if (possibleLoan.getMissingToActive() < min)
                min = possibleLoan.getMissingToActive();
        }
        return min;
    }

    public static List <Loan> fillList(List<Category> categories, int yaz, double minimumInterest, Client client) {
        List <Loan> allLoans=Loans.getLoans();
        List <Loan> possibleLoan = new ArrayList<>();
        for (Loan allLoan : allLoans) {
            if (validLoan(allLoan, categories,yaz, minimumInterest, client) && (allLoan.getStatus().equals(Status.PENDING) || allLoan.getStatus().equals(Status.NEW))) {
                possibleLoan.add(allLoan);
            }
        }
        return possibleLoan;
    }

    private static boolean validLoan(Loan loan, List<Category> categories, int yaz, double minimumInterest, Client client) {
        Investment.categories=new ArrayList<>(categories);
        Investment.investor=client;
        Investment.minimumInterest=minimumInterest;
        Investment.minimumYaz=yaz;
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
            if (minimumInterest > (loan.getInterestPercentage()*100)){
                return false;
            }
        }
        return true;
    }

}
