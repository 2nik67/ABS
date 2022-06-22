package servlets;

import client.Client;
import client.Clients;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.swing.text.html.HTML;
import java.io.IOException;

@WebServlet(name="LoadMoney", urlPatterns = "/servlets/LoadMoney")
public class LoadMoneyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("ClientName");
        String money = req.getParameter("Money");
        Client client = Clients.getClientByName(name);
        client.loadMoney(Double.parseDouble(money), client.getMoney());
        resp.getWriter().println("money was added");
    }

}
