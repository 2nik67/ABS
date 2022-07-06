package servlets;

import client.Client;
import client.Clients;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import loan.Loans;

import java.io.IOException;

@WebServlet(name="TradeLoan", urlPatterns = "/servlets/TradeLoan")
public class TradeLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId = req.getParameter("LoanId");
        Loan loan = Loans.getLoanByID(loanId);
        Client client = Clients.getClientByName(req.getParameter("ClientName"));
        if (loan == null || client == null){
            return;
        }
        boolean sell = Boolean.parseBoolean(req.getParameter("IsSell"));
        if (sell){
            client.getInvestments().get(loanId).setForSale(true);
        }
        else{
            Client buyFrom = Clients.getClientByName(req.getParameter("BuyFrom"));
            client.fixInvestments(buyFrom, loan);
        }
    }
}
