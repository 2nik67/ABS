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


@WebServlet(name="LoanList", urlPatterns = "/servlets/LoanListServlet")
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
}
