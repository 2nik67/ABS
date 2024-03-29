package loan.payment;

public class Payment {
    private final int paidYaz;
    private final double partOfLoanPaid;
    private final double partOfInterestPaid;

    public Payment(int paidYaz, double partOfLoanPaid, double partOfInterestPaid) {
        this.paidYaz = paidYaz;
        this.partOfLoanPaid = partOfLoanPaid;
        this.partOfInterestPaid = partOfInterestPaid;
    }

    public void printPaymentInfo(){
        System.out.println("Payment yaz: " + paidYaz + " | Part of loan paid (without interest): " + partOfLoanPaid +
                " | Part of the interest paid: " + partOfInterestPaid +" | Total:" + (partOfInterestPaid+partOfLoanPaid));
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
