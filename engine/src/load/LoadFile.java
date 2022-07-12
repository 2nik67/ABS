package load;

import client.Client;

import load.jaxbv2.schema.generated.AbsDescriptor;
import load.jaxbv2.schema.generated.AbsLoans;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


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

    public static String readFile(Client client){

        try {
            File file=new File(path);
            InputStream inputStream = new FileInputStream(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
            JAXBContext jc = JAXBContext.newInstance("load.jaxbv2.schema.generated");
            Unmarshaller u = jc.createUnmarshaller();
            absDescriptor = (AbsDescriptor) u.unmarshal(inputStream);
            return importDataForLoan(client);

        }catch (JAXBException e){
            e.printStackTrace();

        }catch (FileNotFoundException e){
            System.out.println("File does not exist in this path!");
        }catch (NullPointerException e){
            System.out.println("File is null");
        }
        return null;
    }

    private static String importDataForLoan(Client client) {
        AbsLoans absLoans = absDescriptor.getAbsLoans();
        for (int i = 0; i < absLoans.getAbsLoan().size(); i++) {

            Loan loan = Loans.getLoanByID(absLoans.getAbsLoan().get(i).getId());
            if (loan !=null){
                return "Loan ID already exists!";
            }

            Category category = Categories.getCategoryByName(absLoans.getAbsLoan().get(i).getAbsCategory());
            if (category == null){
                category = new Category(absLoans.getAbsLoan().get(i).getAbsCategory());
            }
            Categories.addCategory(category);

            Loan newLoan = new Loan(absLoans.getAbsLoan().get(i).getId(), absLoans.getAbsLoan().get(i).getAbsCapital(), client,
                    category, absLoans.getAbsLoan().get(i).getAbsTotalYazTime(),
                    absLoans.getAbsLoan().get(i).getAbsPaysEveryYaz(), absLoans.getAbsLoan().get(i).getAbsIntristPerPayment());
            loans.add(newLoan);
        }

        for (Loan loan : loans) {
            Loans.getLoans().add(loan);
        }

        loans = new ArrayList<>();
        return "Loan was added!";
    }

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
