package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name="CategoryListServlet", urlPatterns = "/servlets/CategoryList")
public class CategoryListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.reset();
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            List<Category> categoryList = Categories.getCategoryList();
            String json = gson.toJson(categoryList);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonArrayCategories =  req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUsersNames);
        String[] categories = new Gson().fromJson(jsonArrayCategories, String[].class);
        List<Category> res = new ArrayList<>();
        List<String> categoriesName = Arrays.asList(categories);
        for (String s : categoriesName) {
            res.add(Categories.getCategoryByName(s));
        }
        resp.setContentType("application/json");
        resp.reset();
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(res);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
