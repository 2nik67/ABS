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
            Client admin = Clients.getClientByName(userName);
            if (admin == null && !Clients.getIsAdminOn()){
                Clients.setIsAdminOn(true);
                admin = new Client(userName, 0);
                Clients.getClientsList().add(admin);
                resp.getWriter().println("WELCOME");
            }else if(admin!=null){
                resp.getWriter().println(userName + " already signed in!");
            }
            else if (Clients.getIsAdminOn()){
                resp.getWriter().println("Admin already signed in!");

            }




        }
    }
}
