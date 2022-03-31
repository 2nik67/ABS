package client.investment;

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
    private ArrayList<Loan> loansInvested;

    public Investment(int investment, int categoryIndex, double minimumInterestPerYaz, int minimumLoanYaz) {
        this.investment = investment;
        if (categoryIndex > 0){
            this.category= Categories.getCategoryList().get(categoryIndex);
        }
        else{
            category = null;
        }
        this.minimumInterestPerYaz = minimumInterestPerYaz;
        this.minimumLoanYaz = minimumLoanYaz;
    }

    public void investmentAssigning(){
        fillList();
        int payEachLoan = investment/loansInvested.size();
        for(Loan loan: loansInvested){
            if (loan.getLoan() > payEachLoan){

            }
        }
    }

    private void fillList() {
        List <Loan> loanList= Loans.getLoans();
        for (Loan loan : loanList) {
            if (validLoan(loan)) {
                loanList.add(loan);
            }
        }
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
