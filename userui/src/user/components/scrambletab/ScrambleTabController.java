package user.components.scrambletab;

import client.Client;
import client.Clients;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;
import Investment.Investment;
import okhttp3.*;
import org.controlsfx.control.CheckListView;
import org.jetbrains.annotations.NotNull;
import user.components.possibleloans.PossibleLoansController;
import user.components.userapp.UserAppController;
import user.utils.HttpClientUtil;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static client.Clients.*;


public class ScrambleTabController {

    private PossibleLoansController possibleLoansController;

    private UserAppController userAppController;

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

    public void setUserAppController(UserAppController userAppController) {
        this.userAppController = userAppController;
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
        /*Client currClient = getClientByName(currentClient);

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

        //TODO: other text field ifs*/

//
        List<Category> categoryList = createCategoryList();
        String bodyRequest = HttpClientUtil.createCategoriesForBody(categoryList);
        RequestBody requestBody = RequestBody.create(bodyRequest.getBytes());
        String url = HttpClientUtil.createPostPossibleLoansListUrl(userAppController.getChosenClient(), amountTextField.getText(),
                interestTextField.getText(), minYazTextField.getText(), maxOpenLoansTextField.getText(), maxLoanOwnTextField.getText());
        HttpClientUtil.runAsyncPost(url, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("IM HERE" + response.code() + call.toString());
            }
        });


        List <Loan> possibleLoans = Investment.fillList(Loans.getLoans(), categoryList, Integer.parseInt(minYazTextField.getText()), Integer.parseInt(minYazTextField.getText()),
                Clients.getClientByName(userAppController.getChosenClient()), Integer.parseInt(maxOpenLoansTextField.getText()));
        //possibleLoansController = new PossibleLoansController();
        //PossibleLoansController.setScrambleTabController(this);
        //PossibleLoansController.setSumToInvest(Double.parseDouble(amountTextField.getText()));
        //PossibleLoansController.setPossibleLoanList(possibleLoans);
        //possibleLoansController.popUp();
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
        userAppController.createLoanTreeForClient(userAppController.getChosenClient());
        userAppController.createInvestmentTreeForClient(userAppController.getChosenClient());
    }
}
