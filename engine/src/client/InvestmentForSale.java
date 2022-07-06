package client;

public class InvestmentForSale {
    private double investment;

    private boolean isForSale;

    public InvestmentForSale(double investment, boolean isForSale) {
        this.investment = investment;
        this.isForSale = isForSale;
    }

    public void add(double investment){
        this.investment += investment;
    }
    public void setInvestment(double investment) {
        this.investment = investment;
    }

    public void setForSale(boolean forSale) {
        isForSale = forSale;
    }

    public double getInvestment() {
        return investment;
    }

    public boolean isForSale() {
        return isForSale;
    }
}
