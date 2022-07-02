package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class Clients {
    private static List <Client> clientsList = new ArrayList<>();
    private static boolean isAdminOn = false;

    public static void setClientsList(List<Client> clients) {
        Clients.clientsList = new ArrayList<>(clients);
    }

    public static void addClient(Client client){clientsList.add(client);}

    public static void PrintList(){
        for (int i = 0; i < clientsList.size(); i++) {
            System.out.println( (i+1) + ") " + clientsList.get(i).getName() + " | " + "Money in the bank: " + String.format("%.2f", clientsList.get(i).getMoney()));
        }
    }

    public static void addMoneyToAccount(int index, double money){
        clientsList.get(index).loadMoney(money, clientsList.get(index).getMoney());
    }

    public static void printPayments(){
        for (Client client : clientsList) {
            System.out.println(client.getName() + " | " + "Current amount of money: " + String.format("%.2f", client.getMoney()) );
            client.printPaymentsList();
            System.out.println("\n");
        }
    }

    public static double getBalanceOfClient(int index)
    {
        return clientsList.get(index).getMoney();
    }

    public static int getClientsSize(){
        return clientsList.size();
    }

    public static void resetClients(){
        clientsList.clear();
    }

    public static List<Client> getClientsList() {
        return clientsList;
    }

    public static Client getClientByName(String name){
        for (int i = 0; i < clientsList.size(); i++) {
            if (clientsList.get(i).getName().equals(name))
                return clientsList.get(i);
        }

        return null;
    }

    public static boolean getIsAdminOn(){ return isAdminOn; }

    public static void setIsAdminOn(boolean isAdminOn) {
        Clients.isAdminOn = isAdminOn;
    }
}
