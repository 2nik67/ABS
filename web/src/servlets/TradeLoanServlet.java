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
        boolean sell = Boolean.parseBoolean(req.getParameter("IsSell"));
        if (sell){
            Loans.getLoanByID(loanId).setForSale(true);

        }
        else{
            Loan loan = Loans.getLoanByID(loanId);
            Client client = Clients.getClientByName(req.getParameter("ClientName"));
            loan.getOwner().loadMoney(loan.getLoan() - loan.getLoanPaid(), loan.getOwner().getMoney());
            loan.setOwner(client);
            client.loadMoney((loan.getLoan() - loan.getLoanPaid())*-1, client.getMoney());
        }
    }
}
