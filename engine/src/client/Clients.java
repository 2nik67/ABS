package client;

import java.util.ArrayList;
import java.util.List;

public abstract class Clients {
    private static List <Client> clientsList=new ArrayList<>();

    public static void addClient(Client client){
        clientsList.add(client);
    }
    public static void PrintList(){
        for (int i = 0; i < clientsList.size(); i++) {
            System.out.println( (i+1) + ") " + clientsList.get(i).getName());
        }
    }
    public static void addMoneyToAccount(int index, double money){
        clientsList.get(index).loadMoney(money);
    }

    public static void printPayments(){
        for (int i = 0; i < clientsList.size(); i++) {
            System.out.println(clientsList.get(i).getName() + ":");
            clientsList.get(i).printPaymentsList();
        }
    }

    public static Client findClient(String client){
        for (int i = 0; i < clientsList.size(); i++) {
            if(clientsList.get(i).getName().equals(client)){
                return clientsList.get(i);
            }
        }
        return null;
    }


}
