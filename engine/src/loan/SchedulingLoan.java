package loan;

public interface SchedulingLoan{

    //print the client list and their balance
    void printClientsAndBalance();

    //Get how much the client wants to invest
    void getInvestment();

    //Get the category
    void getCategoryForLoan();

    //Get the minimum interest per yaz in %
    void getMinimumInterestPerYaz();

    //Get The Minimum total yaz for loan
    void getMinimumTotalYazForLoan();
}
