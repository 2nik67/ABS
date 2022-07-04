package servlets;

import Investment.Investment;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import loan.Loans;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@WebServlet(name="LoanList", urlPatterns = "/servlets/LoanList")
public class LoanListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

            resp.setContentType("application/json");
            resp.reset();
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                List<Loan> loanList = Loans.getLoans();
                String json = gson.toJson(loanList);
                out.println(json);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId = req.getParameter("LoanID");
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            Loan loan = Loans.getLoanByID(loanId);
            String json = gson.toJson(loan);
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //String client = req.getParameter("ClientName");
        double sumToInvest = Double.parseDouble(req.getParameter("SumToInvest"));
        //Client chosenClient = Clients.getClientByName(client);
        String jsonArrayLoans = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String[] loansIdArray = new Gson().fromJson(jsonArrayLoans, String[].class);
        List<String> loansId = Arrays.asList(loansIdArray);
        List<Loan> loans = new ArrayList<>();
        for (String s : loansId) {
            loans.add(Loans.getLoanByID(s));
        }
        resp.getWriter().println(Investment.investmentAssigning(loans, sumToInvest));
/*        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(possibleLoans);
            out.println(json);
            out.flush();
        };*/
    }
}
