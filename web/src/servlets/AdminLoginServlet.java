package servlets;

import client.Client;
import client.Clients;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="AdminLogin", urlPatterns = "/servlets/AdminLogin")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (this){
            resp.setContentType("text/html");
            String userName = req.getParameter("username");
            Client client = Clients.getClientByName(userName);
            if (client == null){
                client = new Client(userName, 0);
                Clients.addClient(client);
                resp.getWriter().println("NEW");
            }else{
                resp.getWriter().println("NOT NEW");
            }

        }
    }
}
