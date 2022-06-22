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
import loan.category.Category;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="ListOfPossibleLoans", urlPatterns = "/servlets/ListOfPossibleLoans")
public class ListOfPossibleLoansServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("ClientName");
        Double amountToInvest = Double.parseDouble(req.getParameter("AmountToInvest"));
        Double minimumInterest = Double.parseDouble(req.getParameter("MinInterest"));
        Integer minimumYaz = Integer.parseInt(req.getParameter("MinYaz"));
        Integer maxOpenLoans = Integer.parseInt(req.getParameter("MaxOpenLoans"));
        Integer maxLoanOwnerships = Integer.parseInt(req.getParameter("maxLoanOwnerships"));
        //List<Loan> possibleLoans = Investment.fillList(Loans.getLoans(), );
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            Client client = Clients.getClientByName(name);
            String json = gson.toJson(client);
            out.println(json);
            out.flush();
        }
        //TODO: Ask about all the info all the time, and about doPost(how to call it and how to pass unknown number of arguments).
    }


}
