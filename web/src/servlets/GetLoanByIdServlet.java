package servlets;

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


@WebServlet(name="GetLoanById", urlPatterns = "/servlets/GetLoanById")
public class GetLoanByIdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanId = req.getParameter("LoanId");
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
