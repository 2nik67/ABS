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
import loan.category.Category;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name="Invest", urlPatterns = "/servlets/Invest")
public class InvestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*//String client = req.getParameter("ClientName");
        Double sumToInvest = Double.parseDouble(req.getParameter("SumToInvest"));
        //Client chosenClient = Clients.getClientByName(client);
        String jsonArrayLoans = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Loan[] loansArray = new Gson().fromJson(jsonArrayLoans, Loan[].class);
        List<Loan> loans = Arrays.asList(loansArray);
        Investment.investmentAssigning(loans, sumToInvest);
*//*        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(possibleLoans);
            out.println(json);
            out.flush();
        };*/
    }
}
