package loan;
import client.Borrower;
import time.*;

import java.util.HashMap;

public class Loan {
    private int id;
    private Borrower borrower;
    private String loanCategory;
    private HashMap<Double, time> loan;
    private HashMap<Double, time> interest;
    private Status status;
}
