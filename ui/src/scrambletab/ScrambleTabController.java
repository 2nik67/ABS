package scrambletab;

import Investment.Investment;
import client.Client;
import client.Clients;
import clientbody.ClientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import loan.Loan;
import loan.Loans;
import loan.category.Category;
import org.controlsfx.control.CheckListView;
import javafx.scene.control.TextFormatter.Change;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.util.converter.IntegerStringConverter;
import loan.category.Categories;
import possibleloans.PossibleLoansController;

import static client.Clients.*;


public class ScrambleTabController {

    private PossibleLoansController possibleLoansController;

    private ClientController clientController;

    @FXML
    private ScrollPane scrambleTabComponent;

    @FXML
    private VBox leftVBox;

    @FXML
    private TextField amountTextField;

    @FXML
    private ContextMenu amountContext;

    @FXML
    private TextField interestTextField;

    @FXML
    private TextField minYazTextField;

    @FXML
    private TextField maxOpenLoansTextField;

    @FXML
    private ContextMenu ownershipContext;

    @FXML
    private TextField maxLoanOwnTextField;

    @FXML
    private Button scrambleBtn;

    @FXML
    private CheckListView<String> categoryCheckList;

    private String currentClient;

    @FXML
    public void initialize(){
        initializeTextFields();
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    UnaryOperator<Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([0-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    public void initializeTextFields(){
        amountTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        interestTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        minYazTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        maxOpenLoansTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        maxLoanOwnTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));
    }

    @FXML
    public void scrambleOperation() throws  Exception{
        Client currClient = getClientByName(currentClient);

        if(amountTextField.getText().equals(""))
            amountTextField.setText("0");
        if(interestTextField.getText().equals(""))
            interestTextField.setText("0");
        if(minYazTextField.getText().equals(""))
            minYazTextField.setText("0");
        if(maxOpenLoansTextField.getText().equals(""))
            maxOpenLoansTextField.setText("0");
        if(maxLoanOwnTextField.getText().equals(""))
            maxLoanOwnTextField.setText("0");

        assert currClient != null;
        if(currClient.getMoney() < Double.parseDouble(amountTextField.getText())){
            MenuItem string = new MenuItem("Not enough money in your account. Current Balance: " + currClient.getMoney());
            amountContext.getItems().clear();
            amountContext.getItems().add(string);

            amountContext.show(scrambleTabComponent, amountTextField.localToScreen(amountTextField.getBoundsInLocal()).getMinX(),
                    amountTextField.localToScreen(amountTextField.getBoundsInLocal()).getMaxY());
            return;
        }

        if(Double.parseDouble(maxLoanOwnTextField.getText()) > 100){
            MenuItem string = new MenuItem("Ownership is by percentage, please enter a value between 0 and 100");
            ownershipContext.getItems().clear();
            ownershipContext.getItems().add(string);

            ownershipContext.show(scrambleTabComponent,maxLoanOwnTextField.localToScreen(maxLoanOwnTextField.getBoundsInLocal()).getMinX(),
                    maxLoanOwnTextField.localToScreen(maxLoanOwnTextField.getBoundsInLocal()).getMaxY());
            return;
        }

        //TODO: other text field ifs
        List<Category> categoryList = createCategoryList();
        List <Loan> possibleLoans = Investment.fillList(Loans.getLoans(), categoryList, Integer.parseInt(minYazTextField.getText()), Integer.parseInt(minYazTextField.getText()),
                Clients.getClientByName(clientController.getChosenClient()), Integer.parseInt(maxOpenLoansTextField.getText()));
        possibleLoansController = new PossibleLoansController();
        PossibleLoansController.setScrambleTabController(this);
        PossibleLoansController.setSumToInvest(Double.parseDouble(amountTextField.getText()));
        PossibleLoansController.setPossibleLoanList(possibleLoans);
        possibleLoansController.popUp();
    }

    private List<Category> createCategoryList(){
        List<Category> categoryList = new ArrayList<>();
        for (int i = 0; i < Categories.getNumOfCategories(); i++) {
            if(categoryCheckList.getCheckModel().isChecked(i)){
                categoryList.add(Categories.getCategoryByName(categoryCheckList.getCheckModel().getItem(i)));
            }
        }
        return categoryList;
    }

    public void initializeCategoryCheckList(){
        List<Category> catList = Categories.getCategoryList();

        ObservableList<String> strings = FXCollections.observableArrayList();
        for (Category category : catList) {
            strings.add(category.getCategory());
        }

        categoryCheckList.setItems(strings);

    }

    public void setCurrentClient(String currentClient) {
        this.currentClient = currentClient;
    }

    public void resetUI() {
        categoryCheckList.getItems().clear();
    }

    public void createTrees() {
        clientController.createLoanTreeForClient(clientController.getChosenClient());
        clientController.createInvestmentTreeForClient(clientController.getChosenClient());
    }
}
