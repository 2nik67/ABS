package user.components.scrambletab;

import client.Client;
import client.Clients;
import com.google.gson.Gson;
import javafx.application.Platform;
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
import java.util.*;
import java.util.List;
import java.util.function.UnaryOperator;

import static client.Clients.*;


public class ScrambleTabController {

    private ScrambleTabController scrambleTabController = this;

    private Timer timer;
    private TimerTask listRefresher;

    private List <Category> categoryList;

    private List <Loan> possibleLoans;

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
        startCategoryCheckListRefresh();
    }

    private void startCategoryCheckListRefresh() {
        listRefresher = new CategoryCheckListRefresher(this::categoryListRefresher);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    private void categoryListRefresher(List<Category> categories) {
        categoryList = categories;
        if (categories.size() == categoryCheckList.getItems().size()){
            return;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<String> strings = FXCollections.observableArrayList();
                for (Category category : categories) {
                    strings.add(category.getCategory());
                }
                categoryCheckList.setItems(strings);
            }
        });

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



        List<Category> categoryList = createCategoryList();
        String bodyRequest = HttpClientUtil.createCategoriesForBody(categoryList);
        RequestBody requestBody = RequestBody.create(bodyRequest.getBytes());
        String url = HttpClientUtil.createPostPossibleLoansListUrl(userAppController.getChosenClient(), amountTextField.getText(),
                interestTextField.getText(), minYazTextField.getText(), maxOpenLoansTextField.getText(), maxLoanOwnTextField.getText());

        possibleLoans = new ArrayList<>();
        HttpClientUtil.runAsyncPost(url, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200){
                    return;
                }
                String jsonArrayOfUsersNames = response.body().string();
                if (!jsonArrayOfUsersNames.equals("[]" + System.lineSeparator())){
                    Loan[] loans = new Gson().fromJson(jsonArrayOfUsersNames, Loan[].class);
                    possibleLoans = Arrays.asList(loans);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            possibleLoansController = new PossibleLoansController();
                            PossibleLoansController.setScrambleTabController(scrambleTabController);
                            PossibleLoansController.setSumToInvest(Double.parseDouble(amountTextField.getText()));
                            PossibleLoansController.setPossibleLoanList(possibleLoans);
                            try {
                                possibleLoansController.popUp();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


    }

    private List<Category> createCategoryList(){
        List<Category> res = new ArrayList<>();
        for (int i = 0; i < categoryCheckList.getItems().size(); i++) {
            if(categoryCheckList.getCheckModel().isChecked(i)){
                for (int j = 0; j < categoryList.size(); j++) {
                    if (categoryList.get(j).getCategory().equals(categoryCheckList.getCheckModel().getItem(i))){
                        res.add(categoryList.get(j));
                    }
                }
            }
        }
        return res;
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
