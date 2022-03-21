package loan.payment;

public class Payment {
    private int paidYaz;
    private double partOfLoanPaid;
    private double partOfInterestPaid;

    public void printPaymentInfo(){
        System.out.println("Payment yaz: " + paidYaz + " | Part of loan paid (without interest): " + partOfLoanPaid +
                " | Part of the interest paid: " + partOfInterestPaid +" | Total:" + (partOfInterestPaid+partOfLoanPaid) + "\n");
    }

    public int getPaidYaz() {
        return paidYaz;
    }

    public double getPartOfLoanPaid() {
        return partOfLoanPaid;
    }

    public double getPartOfInterestPaid() {
        return partOfInterestPaid;
    }
}
