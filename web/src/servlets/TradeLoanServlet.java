package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loans;

import java.io.IOException;

@WebServlet(name="TradeLoan", urlPatterns = "/servlets/TradeLoan")
public class TradeLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId = req.getParameter("LoanId");
        boolean sell = Boolean.parseBoolean(req.getParameter("IsSell"));
        if (sell){
            Loans.getLoanByID(loanId).setForSale(true);

        }
        else{

        }
    }
}
