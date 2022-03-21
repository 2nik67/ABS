package load;

import client.Client;
import client.Clients;
import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
import load.jaxb.schema.generated.*;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public abstract class LoadFile {

    private static AbsDescriptor absDescriptor;
    public static void main(String[] args) {
        readFile();
        Categories.printCategories();
        Clients.PrintList();
        Loans.printLoans();
    }

    public static void readFile(){

        try {
            InputStream inputStream=new FileInputStream(new File("engine/src/resources/ex1-big.xml"));
            JAXBContext jaxbContext=JAXBContext.newInstance(AbsDescriptor.class);
            JAXBContext jc=JAXBContext.newInstance("load.jaxb.schema.generated");
            Unmarshaller u=jc.createUnmarshaller();
            absDescriptor=(AbsDescriptor) u.unmarshal(inputStream);
            importData();

        }catch (JAXBException  | FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private static void importData(){
        importCategory();
        importCustomers();
        importLoans();

    }

    private static void importLoans() {
        List <AbsLoan> absLoans=absDescriptor.getAbsLoans().getAbsLoan();
        for (int i = 0; i < absLoans.size(); i++) {
            double interest = absLoans.get(i).getAbsIntristPerPayment()*(absLoans.get(i).getAbsTotalYazTime()/absLoans.get(i).getAbsPaysEveryYaz());

            Loan loan=new Loan(absLoans.get(i).getId(), absLoans.get(i).getAbsCapital(),interest,
                    Clients.findClient(absLoans.get(i).getAbsOwner()), absLoans.get(i).getAbsCategory(), absLoans.get(i).getAbsTotalYazTime(),
                            absLoans.get(i).getAbsPaysEveryYaz());
        }
    }

    private static void importCustomers() {
        List<AbsCustomer> absCustomersList = absDescriptor.getAbsCustomers().getAbsCustomer();
        for (int i = 0; i < absCustomersList.size(); i++) {
            Client client=new Client(absCustomersList.get(i).getName(), absCustomersList.get(i).getAbsBalance());
        }
    }

    private static void importCategory(){
        List <String> absCategories = new ArrayList<>();
        absCategories=absDescriptor.getAbsCategories().getAbsCategory();
        for (int i = 0; i < absCategories.size(); i++) {
            Category category=new Category(absCategories.get(i));
        }
    }

}
