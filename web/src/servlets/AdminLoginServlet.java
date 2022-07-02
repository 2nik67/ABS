package servlets;

import client.Client;
import client.Clients;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="AdminLogin", urlPatterns = "/servlets/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (this){
            resp.setContentType("text/html");
            String userName = req.getParameter("username");
            if (!Clients.getIsAdminOn()){
                Clients.setIsAdminOn(true);
                resp.getWriter().println("WELCOME");
            }
            else{
                resp.getWriter().println("ERROR: only one admin can login at the same time.");
            }

        }
    }
}
