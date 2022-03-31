package loan;

import client.Client;
import client.Clients;
import loan.category.Categories;
import loan.category.Category;

import java.util.List;

public abstract class SchedulingLoanImp implements SchedulingLoan{

    @Override
    public void printClientsAndBalance() {
        List<Client> clientsList= Clients.getClientsList();
        System.out.println("Please choose client by number:");
        for (int i = 0; i < clientsList.size(); i++) {
            System.out.println(i + ") " + clientsList.get(i).getName() + "| Balance: " + clientsList.get(i).getMoney());
        }

    }

    @Override
    public void getInvestment() {
        System.out.println("How much money would you like to invest?");
        //TODO: get the money
    }

    @Override
    public void getCategoryForLoan() {
        List <Category> categoryList = Categories.getCategoryList();
        System.out.println("Please choose one of the following categories (0 for no category)");
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println(i + ") " + categoryList.get(i).getCategory());
        }
    }

    @Override
    public void getMinimumInterestPerYaz() {
        System.out.println("Please choose the minimum amount of interest per yaz (0 for no minimum): ");
        //TODO: get and check
    }

    @Override
    public void getMinimumTotalYazForLoan() {
        System.out.println("Please choose the minimum yaz per loan (0 for no minimum): ");
    }


}
