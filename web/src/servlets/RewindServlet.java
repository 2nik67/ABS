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
import time.SaveInfoPerYaz;
import time.Yaz;

import java.io.IOException;
import java.util.List;


@WebServlet(name="Rewind", urlPatterns = "/servlets/Rewind")
public class RewindServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int yaz = Integer.parseInt(req.getParameter("Yaz"));

        if(yaz == Yaz.getYaz() - 1)
            Yaz.saveCurrentYazStateToArchive();

        SaveInfoPerYaz yazData = Yaz.getPreviousYazData(yaz);
        List<Loan> loanData= yazData.getLoans();
        List<Client> clientData = yazData.getClients();

        Clients.setClientsList(clientData);
        Loans.setLoans(loanData);
        Yaz.setYaz(yaz);
    }
}