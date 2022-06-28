package servlets;

import Investment.Investment;
import client.Client;
import client.Clients;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import loan.Loans;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="Scramble", urlPatterns = "/servlets/Scramble")
public class ScrambleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String client = req.getParameter("ClientName");
        String AmountToInvest = req.getParameter("AmountToInvest");
        String minimumInterestYaz = req.getParameter("MinimumInterestYaz");
        String minimumYaz = req.getParameter("MinimumYaz");
        String maxOpenLoans = req.getParameter("MaxOpenLoans");
        String maxLoanOwnerShip = req.getParameter("MaxLoanOwnerShip");
        Client chosenClient = Clients.getClientByName(client);

        //List<Loan> possibleLoans = Investment.fillList(Loans.getLoans(), )
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            List<Loan> loanList = Loans.getLoans();
            String json = gson.toJson(loanList);
            out.println(json);
            out.flush();
        };
    }
}
