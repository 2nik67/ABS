package user.components.userapp;

import client.Client;
import client.Clients;
import client.Transaction;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import loan.Loan;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.components.depositwithdraw.DepositWithdrawController;
import user.components.loanpopup.LoanPopUpController;
import user.components.loantrade.LoanTradeController;
import user.components.main.UserAppMainController;
import user.components.newloantab.NewLoanTabController;
import user.components.paymenttab.PaymentTabController;
import user.components.possibleloans.PossibleLoansController;
import user.components.scrambletab.ScrambleTabController;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.util.*;

public class UserAppController {

    private TimerTask loanOfClientRefresher;
    private Timer timer;

    private TimerTask transactionListRefresher;

    private boolean firstTimeTree = true;

    private String clientName;

    private static boolean isRewind = false;



    private UserAppMainController userAppMainController;

    private PossibleLoansController possibleLoansController;

    private DepositWithdrawController depositWithdrawController;

    private LoanPopUpController loanPopUpController;

    private Client chosenClient;

    @FXML
    private Tab informationTab;

    private TimerTask investmentsTableRefresher;

    //TODO: remove trees
    @FXML
    private TreeView<String> loansTreeView;

    @FXML
    private TreeView<String> investmentsTreeView;

    @FXML
    private TableView<Loan> loansTableView;

    @FXML
    private TableView<Loan> investmentsTableView;

    @FXML
    private Button depositBtn;

    @FXML
    private TableView<Transaction> transactionTableView;

    @FXML
    private TableColumn<Transaction, Integer> yazTableColumn;

    @FXML
    private TableColumn<Transaction, Double> beforeChangeTableColumn;

    @FXML
    private TableColumn<Transaction, Double> moneyTableColumn;

    @FXML
    private TableColumn<Transaction, Double> afterChangeTableColumn;

    @FXML
    private ScrollPane scrambleTabComponent;

    @FXML
    private ScrambleTabController scrambleTabComponentController;

    @FXML
    private ScrollPane paymentTabComponent;

    @FXML
    private PaymentTabController paymentTabComponentController;

    @FXML
    private ScrollPane newLoanTabComponent;

    @FXML
    private NewLoanTabController newLoanTabComponentController;

    @FXML
    private Button withdrawBtn;

    @FXML
    private Tab scrambleTab;

    @FXML
    private TableColumn <Loan, String> investmentsTableColumn;

    @FXML
    private TableColumn <Loan, String> loansTableColumn;

    @FXML
    private Tab paymentTab;

    @FXML
    private Tab newLoanTab;

    @FXML
    private Tab loanTradeTab;

    private LoanTradeController loanTradeComponentController;

    @FXML
    private ScrollPane loanTradeTabComponent;

    final static String getClientFinalUrl = "http://localhost:8080/web_Web/servlets/GetUserByName";

    @FXML
    public void initialize(){
        if (scrambleTabComponentController != null) {
            scrambleTabComponentController.setUserAppController(this);
            paymentTabComponentController.setUserAppController(this);
            newLoanTabComponentController.setUserAppController(this);
            //loanTradeComponentController.setUserAppController(this);
        }

        startLoanClientListRefresher();
        startInvestmentsClientListRefresher();


    }

    public void setRewind(boolean isRewindNow){
        if(isRewindNow){
            isRewind = true;
            scrambleTab.setDisable(true);
            paymentTab.setDisable(true);
            newLoanTab.setDisable(true);
            loanTradeTab.setDisable(true);
            depositBtn.setDisable(true);
        }
        else if(isRewind){
            isRewind = false;
            scrambleTab.setDisable(false);
            paymentTab.setDisable(false);
            newLoanTab.setDisable(false);
            loanTradeTab.setDisable(false);
            depositBtn.setDisable(false);
        }
    }

    private void startTransactionListRefresher() {
        transactionListRefresher = new TransactionListRefresher(this::transactionRefresh, clientName);
        timer = new Timer();
        timer.schedule(transactionListRefresher, 2000, 2000);
    }

    private void transactionRefresh(Client client) {
        transactionTableView.getItems().clear();
        yazTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("yazOfTransaction"));
        beforeChangeTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("original"));
        moneyTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("moneyChange"));
        afterChangeTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("afterChange"));
        transactionTableView.getItems().addAll(client.getTransactions());
    }

    public void startInvestmentsClientListRefresher() {
        investmentsTableRefresher = new InvestmentsTableRefresher(
                this::updateInvestmentsTable);
        timer = new Timer();
        timer.schedule(investmentsTableRefresher, 2000, 2000);
    }

    public void updateInvestmentsTable(List<Loan> loansOfClient) {
        HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(clientName), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                if (!jsonArrayOfUsersNames.equals("[]" + System.lineSeparator())){
                    Client client = new Gson().fromJson(jsonArrayOfUsersNames, Client.class);
                    chosenClient = client;
                }
            }
        });
        List <Loan> loans = new ArrayList<>();
        for (Loan loan : loansOfClient) {
            for (int i = 0; i <loan.getLoaners().size(); i++) {
                if(loan.getLoaners().get(i).getKey().getName().equals(clientName)){
                    loans.add(loan);
                }
            }
        }
        if (loans.isEmpty()){
            return;
        }

        investmentsTableView.getItems().clear();
        investmentsTableColumn.setCellValueFactory(new PropertyValueFactory<Loan, String> ("id"));
        investmentsTableView.getItems().addAll(loans);
    }

    public void startLoanClientListRefresher() {
        loanOfClientRefresher = new LoanTableRefresher(
                this::updateDataLoanTable);
        timer = new Timer();
        timer.schedule(loanOfClientRefresher, 2000, 2000);
    }


    private void updateDataLoanTable(List<Loan> loansOfClient) {
        HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(clientName), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                if (!jsonArrayOfUsersNames.equals("[]" + System.lineSeparator())){
                    chosenClient = new Gson().fromJson(jsonArrayOfUsersNames, Client.class);
                }
            }
        });


        List <Loan> loans = new ArrayList<>();
        for (Loan loan : loansOfClient) {
            if (loan.getOwner().getName().equals(clientName)) {
                loans.add(loan);
            }
        }

        if (loans.isEmpty()){
            return;
        }
        loansTableView.getItems().clear();
        loansTableColumn.setCellValueFactory(new PropertyValueFactory<Loan, String> ("id"));
        loansTableView.getItems().addAll(loans);

    }

    @FXML
    public void onMouseClickedLoans(MouseEvent event) throws Exception{
        if(event.getClickCount() == 2){
            loanPopUpController = new LoanPopUpController();
            Loan loan = loansTableView.getSelectionModel().getSelectedItems().get(0);
            if (loan == null){
                return;
            }
            loanPopUpController.setLoan(loan);
            loanPopUpController.popUp("LOAN");

        }
    }

    @FXML
    public void onMouseClickedInvestment(MouseEvent event) throws Exception {
        if (event.getClickCount() == 2) //Checking double click
        {
            loanPopUpController = new LoanPopUpController();
            Loan loan = investmentsTableView.getSelectionModel().getSelectedItems().get(0);
            if (loan == null){
                return;
            }
            loanPopUpController.setLoan(loan);
            loanPopUpController.popUp("INVESTMENT");
        }
    }

    private int getNumberOfLoans(List <Loan> loans) {
        int res = 0;
        for (Loan loan : loans) {
            if (loan.getOwner().getName().equals(chosenClient.getName()))
                res++;
        }
        return res;
    }

    @FXML
    public void depositBtnOnAction (ActionEvent event) throws Exception{
        depositWithdrawController = new DepositWithdrawController();
        depositWithdrawController.setCurrentClient(Clients.getClientByName(userAppMainController.getChosenClient()));
        DepositWithdrawController.setUserAppController(this);
        depositWithdrawController.popUp(userAppMainController.getChosenClient());
        //infoTabSelected();
    }


    public void setCurrentClient(String currentClient) {
        clientName = currentClient;
        startTransactionListRefresher();
    }

    public String getChosenClient(){
        return userAppMainController.getClientsName();
    }

    public void createTransactionInfo(String name){
        Clients.getClientByName(name);
    }

    public ObservableList<String> getCurrentStyle(){
        return loansTreeView.getScene().getStylesheets();
    }

    public void setUserAppMainController(UserAppMainController userAppMainController) {
        this.userAppMainController = userAppMainController;
    }
}
