package servlets;


import client.Client;
import client.Clients;
import client.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet (name="UserLogin", urlPatterns = "/servlets/UserLogin")
public class UserLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //processRequest(req, resp);
        resp.setContentType("text/html");
        String userName = req.getParameter("username");
        Client client = Clients.getClientByName(userName);
        if (client == null){
            client = new Client(userName, 0);
            Clients.getClientsList().add(client);
            //List<Client> clients = new ArrayList<>();
            //clients.add(client);
            //Clients.setClientsList(clients);
            resp.getWriter().println("NEW");
        }else{
            resp.getWriter().println("EXISTING");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String userName = request.getParameter("username");
        Client client = Clients.getClientByName(userName);
        if (client == null){
            response.getWriter().println("new client");
        }else{
            client= new Client(userName, 0);
            response.getWriter().println("not a new ");
        }
    }

    /*protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("hi");
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter("username");
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter -
                //redirect back to the index page
                //this return an HTTP code back to the browser telling it to load
                System.out.println("im here and i shouldnt");
                response.getWriter().println("Error");
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                *//*
                One can ask why not enclose all the synchronizations inside the userManager object ?
                Well, the atomic action we need to perform here includes both the question (isUserExists) and (potentially) the insertion
                of a new user (addUser). These two actions needs to be considered atomic, and synchronizing only each one of them, solely, is not enough.
                (of course there are other more sophisticated and performable means for that (atomic objects etc) but these are not in our scope)

                The synchronized is on this instance (the servlet).
                As the servlet is singleton - it is promised that all threads will be synchronized on the very same instance (crucial here)

                A better code would be to perform only as little and as necessary things we need here inside the synchronized block and avoid
                do here other not related actions (such as request dispatcher\redirection etc. this is shown here in that manner just to stress this issue
                 *//*
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        // username already exists, forward the request back to index.jsp
                        // with a parameter that indicates that an error should be displayed
                        // the request dispatcher obtained from the servlet context is one that MUST get an absolute path (starting with'/')
                        // and is relative to the web app root
                        // see this link for more details:
                        // http://timjansen.github.io/jarfiller/guide/servlet25/requestdispatcher.xhtml

                        //TODO: 2 lines

                        request.setAttribute("username error", errorMessage);
                        getServletContext().getRequestDispatcher(CHAT_ROOM_URL).forward(request, response);
                        response.getWriter().println(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute("username", usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());

                        //TODO:check this line
                        response.sendRedirect(CHAT_ROOM_URL);
                        response.getWriter().println("logged in");
                    }
                }
            }
        } else {
            //user is already logged in
            System.out.println("im here 2");
            response.sendRedirect(CHAT_ROOM_URL);
            response.getWriter().println("logged in");
        }
    }*/

}
