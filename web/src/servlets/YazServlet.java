package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import time.Yaz;

import java.io.IOException;


@WebServlet(name="Yaz", urlPatterns = "/servlets/Yaz")
public class YazServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String isRewind = req.getParameter("REWIND");
        if(isRewind != null){
            if(isRewind.contains("true")) {
                Yaz.setIsRewindActive(true);
                Yaz.saveCurrentYazStateToArchive();
            }
            else if(isRewind.contains("false"))
                Yaz.setIsRewindActive(false);
        }

        resp.getWriter().println(String.valueOf(Yaz.getYaz()));
        resp.getWriter().println(Yaz.isIsRewindActive());
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Yaz.advanceYaz(1);
        resp.getWriter().println("Yaz was increased");
    }
}
