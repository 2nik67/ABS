package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import loan.Loans;

import java.io.IOException;


@WebServlet(name="PayLoan", urlPatterns = "/servlets/PayLoan")
public class PayLoanServlet extends HttpServlet {


    //doGet: close loan
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId= req.getParameter("LoanId");
        Loan loan = Loans.getLoanByID(loanId);
        assert loan != null;
        if (!loan.payFullLoan()){
            resp.getWriter().println("Not Enough Money");
        }
        else{
            resp.getWriter().println("Loan Closed");
        }
    }

    //doHead: pay any amount
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId= req.getParameter("LoanId");
        String amount = req.getParameter("ToPay");
        Loan loan = Loans.getLoanByID(loanId);
        double toPay = Double.parseDouble(amount);
        assert loan != null;

        if (loan.getOwner().getMoney() < toPay){
            resp.getWriter().println("Not Enough Money");
        }
        else{
            resp.getWriter().println("paid");
            loan.payPartOfLoan(toPay);
            loan.shouldAutoPay();
        }
    }


    //doPost: auto pay
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId= req.getParameter("LoanId");
        Loan loan = Loans.getLoanByID(loanId);
        assert loan != null;
        if (loan.getLoanEveryYaz() + loan.getInterestEveryYaz() > loan.getOwner().getMoney()){
            resp.getWriter().println("Not Enough Money");
        }else{
            resp.getWriter().println("paid");
            loan.payLoan();
            loan.shouldAutoPay();
        }

    }
}
