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
import java.util.List;


@WebServlet(name="NewLoan", urlPatterns = "/servlets/NewLoan")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class NewLoan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = req.getParameter("ClientName");
        Client client = Clients.getClientByName(clientName);
        if (client == null){
            resp.getWriter().println("Client does not exist!");
            return;
        }

        double capital = Double.parseDouble(req.getParameter("Capital"));
        if (capital <= 0){
            resp.getWriter().println("Capital must be positive number! (As we should be :-) )");
            return;
        }

        String loanID = req.getParameter("LoanID");
        if (loanID.isEmpty()){
            resp.getWriter().println("Loan ID is empty!");
            return;
        }

        Category cat = Categories.getCategoryByName(req.getParameter("Category"));
        if (cat == null){
            resp.getWriter().println("Category does not exist!");
            return;
        }

        int totalYaz = Integer.parseInt(req.getParameter("TotalYaz"));
        if (totalYaz < 0){
            resp.getWriter().println("Total yaz can not be negative!");
            return;
        }
        int yazInterval = Integer.parseInt(req.getParameter("YazInterval"));
        if (yazInterval < 0){
            resp.getWriter().println("Yaz interval can not be negative!");
            return;
        }
        if (totalYaz % yazInterval != 0){
            resp.getWriter().println("Yaz is not divisible");
            return;
        }

        int interest = Integer.parseInt(req.getParameter("InterestPerPayment"));
        if (interest < 0 || interest >100){
            resp.getWriter().println("Interest is not between 0-100");
            return;
        }

        List<Loan> loans = Loans.getLoans();
        for (Loan loan : loans) {
            if (loan.getId().equals(loanID)) {
                resp.getWriter().println("Loan already exists!");
                return;
            }
        }

        Loans.getLoans().add(new Loan(loanID, capital, client, cat, totalYaz, yazInterval, interest));
        resp.getWriter().println("Loan " + "\""+loanID +  "\"" +  " was added");
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
        String res = LoadFile.readFile(client);

        resp.getWriter().println(res);
    }
}
