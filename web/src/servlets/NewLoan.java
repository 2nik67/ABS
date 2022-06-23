package servlets;

import client.Client;
import client.Clients;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import load.LoadFile;

import java.io.IOException;

//TODO: localhost:8080/servlets/NewLoan?Path=blabla&ClientName=tal
@WebServlet(name="NewLoan", urlPatterns = "/servlets/NewLoan")
public class NewLoan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getReader().readLine().toString();
        resp.getWriter().println(path);
       // String clientName = req.getParameter("ClientName");
        //Client client = Clients.getClientByName(clientName);
       // LoadFile.setPath(path);
        //LoadFile.readFile(client);
       // resp.getWriter().println("Hey");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getReader().readLine().toString();
        resp.getWriter().println(path);
    }
}
