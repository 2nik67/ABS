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
    private final int investment;
    private int investmentLeft;
    private final Category category;
    private final double minimumInterestPerYaz;
    private final int minimumLoanYaz;
    private final Client investor;



    public Investment(int investment, int categoryIndex, double minimumInterestPerYaz, int minimumLoanYaz, Client client) {
        this.investment = investment;
        this.investmentLeft=investment;
        if (categoryIndex >= 0){
            this.category = Categories.getCategoryList().get(categoryIndex);
        }
        else{
            category = null;
        }
        this.minimumInterestPerYaz = minimumInterestPerYaz;
        this.minimumLoanYaz = minimumLoanYaz;
        this.investor = client;
        Investments.addInvestment(this);
        investmentAssigning();
    }

    public void investmentAssigning(){
        investor.loadMoney((-1*investment));//TODO: change cast
        List<Loan> possible = fillList();
        int payEachLoan;
        int minLoan = getMinLoan(possible);
        int temp=investment;

        while(temp>0){

            if (minLoan > temp){
                payEachLoan = temp/Loans.getLoans().size();
            }
            else{
                payEachLoan=minLoan;
            }
            if (possible.isEmpty()){
                return;
            }
            for (Loan loan : possible) {
                loan.investmentPay(payEachLoan, investor);
                temp -= payEachLoan;
                if (temp == 0) {
                    return;
                }
            }
            possible=fillList();
            minLoan = getMinLoan(possible);

        }



    }

    private int getMinLoan(List<Loan> possibleLoans) {
        int min=10000;
        for (Loan possibleLoan : possibleLoans) {
            if (possibleLoan.getMissingToActive() < min)
                min = possibleLoan.getMissingToActive();
        }
        return min;
    }

    private List <Loan> fillList() {
        List <Loan> allLoans=Loans.getLoans();
        List <Loan> possibleLoan = new ArrayList<>();
        for (Loan allLoan : allLoans) {
            if (validLoan(allLoan) && (allLoan.getStatus().equals(Status.PENDING) || allLoan.getStatus().equals(Status.NEW))) {
                possibleLoan.add(allLoan);
            }
        }
        return possibleLoan;
    }

    private boolean validLoan(Loan loan) {
        if(loan.getOwner().equals(investor)){
            return false;
        }
        if (category != null){
            if (!loan.getLoanCategory().equals(category)){
                return false;
            }
        }
        if(minimumInterestPerYaz != 0){
            if(minimumInterestPerYaz > loan.getTotalYaz()){
                return false;
            }
        }
        if (minimumInterestPerYaz != 0){
            //TODO: wtf is this
        }
        return true;
    }
}
