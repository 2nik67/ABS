package utils;

import client.Client;
import client.Clients;
import client.UserManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import loan.Loan;
import loan.Loans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object chatManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static Set<String> getClients(){
        List<Client> clients= Clients.getClientsList();
        Set<String> res = new HashSet<>();
        synchronized(userManagerLock){
            for (Client client : clients) {
                res.add(client.getName());
            }
        }
        return res;
    }

    public static List<Loan> getLoansOfClient(String name) {
        List<Loan> res = new ArrayList<>();
        List<Loan> loans = Loans.getLoans();
        for (Loan loan : loans) {
            if (loan.getOwner().getName().equals(name)) {
                res.add(loan);
            }
        }
        return res;
    }
}

