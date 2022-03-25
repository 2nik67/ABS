package load;

import client.Client;
import loan.Loan;
import loan.category.Category;

import java.util.ArrayList;

public abstract class ValidityCheck {

    public static boolean checkIfCategoryExistsByString(ArrayList<Category> categories, String category){
        for (Category value : categories) {
            if (value.getCategory().equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    /*public static boolean checkIfLoanExist(ArrayList<Loan> loanArrayList, String loanID){
        for (int i = 0; i < loanArrayList.size()-1; i++) {
            if(loanArrayList.get(i).getId().equals(loanID))
                return true;
        }
        return false;
    }*/

    public static boolean checkIfClientExist(ArrayList<Client> clients, String clientName){
        for (Client client : clients) {
            if (client.getName().equalsIgnoreCase(clientName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTotalYazDivisible(int totalYaz, int payEveryYaz){
        return totalYaz % payEveryYaz == 0;
    }


}
