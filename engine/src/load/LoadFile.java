package load;

import client.Client;
import client.Clients;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import load.jaxbv2.schema.generated.AbsDescriptor;
import load.jaxbv2.schema.generated.AbsLoan;
import load.jaxbv2.schema.generated.AbsLoan;
import load.jaxbv2.schema.generated.AbsLoans;
import load.jaxbv2.schema.generated.ObjectFactory;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;
import time.Yaz;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public abstract class LoadFile{

    private static boolean fileLoaded = false;
    private static boolean clientRead=false;
    private static boolean loansRead=false;
    private static ArrayList<Client> clients = new ArrayList<>();
    private static ArrayList<Category> categories = new ArrayList<>();
    private static ArrayList<Loan> loans =new ArrayList<>();
    private static String path;
    private static AbsDescriptor absDescriptor;

    public static boolean isFileLoaded() {
        return fileLoaded;
    }

    public static void setPath(String path) {
       LoadFile.path = path;
    }

    public static String getPath() {
        return path;
    }

    public static void readFile(Client client){

        try {
            File file=new File(path);
            InputStream inputStream = new FileInputStream(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
            JAXBContext jc = JAXBContext.newInstance("load.jaxbv2.schema.generated");
            Unmarshaller u = jc.createUnmarshaller();
            absDescriptor = (AbsDescriptor) u.unmarshal(inputStream);
            importDataForLoan(client);

        }catch (JAXBException e){
            e.printStackTrace();

        }catch (FileNotFoundException e){
            System.out.println("File does not exist in this path!");
        }catch (NullPointerException e){
            System.out.println("File is null");
        }

    }

    private static void importDataForLoan(Client client) {
        AbsLoans absLoans = absDescriptor.getAbsLoans();
        for (int i = 0; i < absLoans.getAbsLoan().size(); i++) {
            loans.add(new Loan(absLoans.getAbsLoan().get(i).getId(), absLoans.getAbsLoan().get(i).getAbsCapital(), client,
                    returnCategoryByString(absLoans.getAbsLoan().get(i).getAbsCategory()), absLoans.getAbsLoan().get(i).getAbsTotalYazTime(),
                    absLoans.getAbsLoan().get(i).getAbsPaysEveryYaz(), absLoans.getAbsLoan().get(i).getAbsIntristPerPayment()));

        }

    }
/*
    private static void importData(){
        importCategory();
        importCustomers();
        importLoans();
        if(loansRead){
            Categories.setCategoryList(categories);
            Clients.setClientsList(clients);
            Loans.setLoans(loans);
            Yaz.advanceYaz(Yaz.getYaz()*-1);
        }
        resetData();

    }

    private static void importLoans() {
        if(clientRead){
            List <AbsLoan> absLoans=absDescriptor.getAbsLoans().getAbsLoan();
            for (AbsLoan absLoan : absLoans) {
                if (!ValidityCheck.checkIfClientExist(clients, absLoan.getAbsOwner())) {
                    System.out.println("Client " + absLoan.getAbsOwner() + " does not exist");
                    resetData();
                    LoadFile.loansRead=false;
                    return;
                } else if (!ValidityCheck.checkIfCategoryExistsByString(categories, absLoan.getAbsCategory())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid File");
                    alert.setHeaderText("Invalid File");
                    alert.setContentText("Category " + absLoan.getAbsCategory() + " does not exist");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                    //System.out.println("Category " + absLoan.getAbsCategory() + " does not exist");
                    LoadFile.loansRead=false;
                    resetData();
                    return;
                } else if (!ValidityCheck.isTotalYazDivisible(absLoan.getAbsTotalYazTime(), absLoan.getAbsPaysEveryYaz())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid File");
                    alert.setHeaderText("Invalid File");
                    alert.setContentText("Loan " + "\"" + absLoan.getId() + "\"" + " is not divisible");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                    //System.out.println("Loan " + absLoan.getId() + " is not divisible");
                    LoadFile.loansRead=false;
                    resetData();
                    return;
                } else {
                    loans.add(new Loan(absLoan.getId(), absLoan.getAbsCapital(), findClient(absLoan.getAbsOwner()),
                            returnCategoryByString(absLoan.getAbsCategory()), absLoan.getAbsTotalYazTime(),
                            absLoan.getAbsPaysEveryYaz(), absLoan.getAbsIntristPerPayment()));
                }


            }
            LoadFile.fileLoaded = true;
            LoadFile.loansRead=true;
            System.out.println("File was loaded!");
        }


    }

    private static void importCustomers() {
        List<AbsCustomer> absCustomersList = absDescriptor.getAbsCustomers().getAbsCustomer();
        for (AbsCustomer absCustomer : absCustomersList) {
            if (!ValidityCheck.checkIfClientExist(clients, absCustomer.getName())) {
                clients.add(new Client(absCustomer.getName(), absCustomer.getAbsBalance()));
            } else {
                System.out.println("Client " + absCustomer.getName() + " exists!");
                LoadFile.clientRead=false;
                resetData();
                return;
            }
        }
        LoadFile.clientRead=true;
    }



    private static void importCategory(){
        List <String> absCategories;
        absCategories=absDescriptor.getAbsCategories().getAbsCategory();
        for (String absCategory : absCategories) {
            Category category = new Category(absCategory);
            categories.add(category);
        }
    }

    private static void resetData(){
        clients = new ArrayList<>();
        loans = new ArrayList<>();
        categories =new ArrayList<>();
        clientRead=false;
        loansRead=false;
    }

    public static Client findClient(String client){
        for (Client value : clients) {
            if (value.getName().equals(client)) {
                return value;
            }
        }
        return null;
    }*/
    public static Category returnCategoryByString(String category){
        for (Category value : categories) {
            if (value.getCategory().equalsIgnoreCase(category)) {
                return value;
            }
        }
        return null;
    }

    public static boolean isClientRead() {
        return clientRead;
    }

    public static boolean isLoansRead() {
        return loansRead;
    }
}
