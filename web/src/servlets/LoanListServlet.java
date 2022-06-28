package servlets;

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
import java.util.List;
import java.util.Set;


@WebServlet(name="LoanList", urlPatterns = "/servlets/LoanList")
public class LoanListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("ClientName");
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            List<Loan> loanList = Loans.getLoans();
            String json = gson.toJson(loanList);
            out.println(json);
            out.flush();
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
}
