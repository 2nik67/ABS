package Investment;

import client.Client;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;

import java.util.ArrayList;
import java.util.List;

public class Investment {
    private int investment;
    private Category category;
    private double minimumInterestPerYaz;
    private int minimumLoanYaz;
    private Client investor;



    public Investment(int investment, int categoryIndex, double minimumInterestPerYaz, int minimumLoanYaz, Client client) {
        this.investment = investment;
        if (categoryIndex > 0){
            this.category= Categories.getCategoryList().get(categoryIndex);
        }
        else{
            category = null;
        }
        this.minimumInterestPerYaz = minimumInterestPerYaz;
        this.minimumLoanYaz = minimumLoanYaz;
        this.investor = client;
    }

    public void investmentAssigning(){
        List<Loan> possible = fillList();
        int payEachLoan;
        int minLoan = getMinLoan(possible);
        if (minLoan > investment){
            payEachLoan = investment/Loans.getLoans().size();
        }
        else{
            payEachLoan=minLoan;
        }
        for (int i = 0; i < possible.size(); i++) {
            possible.get(i).payForLoan(payEachLoan);
            investment-=payEachLoan;
            if(investment==0){
                return;
            }
        }

    }

    private int getMinLoan(List<Loan> possibleLoans) {
        int min=10000;
        for (int i = 0; i < possibleLoans.size(); i++) {
            if(possibleLoans.get(i).getLoan() < min)
                min=possibleLoans.get(i).getLoan();
        }
        return min;
    }

    private List <Loan> fillList() {
        List <Loan> allLoans=Loans.getLoans();
        List <Loan> possibleLoan = new ArrayList<>();
        for (Loan allLoan : allLoans) {
            if (validLoan(allLoan)) {
                possibleLoan.add(allLoan);
            }
        }
        return possibleLoan;
    }

    private boolean validLoan(Loan loan) {
        if (!category.equals(null)){
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
        return false;
    }
}
