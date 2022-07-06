package servlets;

import client.Client;
import client.Clients;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import load.LoadFile;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;

import java.io.File;
import java.io.IOException;


@WebServlet(name="NewLoan", urlPatterns = "/servlets/NewLoan")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class NewLoan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = req.getParameter("ClientName");
        Client client = Clients.getClientByName(clientName);

        Double capital = Double.parseDouble(req.getParameter("Capital"));

        String loanID = req.getParameter("LoanID");

        Category cat = Categories.getCategoryByName(req.getParameter("Category"));

        int totalYaz = Integer.parseInt(req.getParameter("TotalYaz"));
        int yazInterval = Integer.parseInt(req.getParameter("YazInterval"));
        int interest = Integer.parseInt(req.getParameter("InterestPerPayment"));

        Loans.getLoans().add(new Loan(loanID, capital, client, cat, totalYaz, yazInterval, interest));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = "";
        String clientName = req.getParameter("ClientName");
        Client client = Clients.getClientByName(clientName);

        System.out.println(req.getParts().size());

        String uploadPath = getServletContext().getRealPath("") + File.separator + "files";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        for (Part part : req.getParts()) {
            System.out.println("part name:" + part.getName());
            fileName = part.getName();
            part.write(uploadPath + File.separator + fileName + ".xml");
        }

        LoadFile.setPath(uploadPath + File.separator + fileName + ".xml");
        LoadFile.readFile(client);

        resp.getWriter().println(" Hey");
    }
}
