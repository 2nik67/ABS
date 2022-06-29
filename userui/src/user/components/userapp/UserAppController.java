package user.components.userapp;

import client.Client;
import client.Clients;
import client.Transaction;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import loan.Loan;
import loan.Loans;
import loan.Status;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.components.depositwithdraw.DepositWithdrawController;
import user.components.loanpopup.LoanPopUpController;
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

    private boolean firstTimeTree = true;

    private UserAppMainController userAppMainController;

    private PossibleLoansController possibleLoansController;

    private DepositWithdrawController depositWithdrawController;

    private LoanPopUpController loanPopUpController;

    private Client chosenClient;

    @FXML
    private Tab informationTab;

    private InvestmentsTableRefresher investmentsTableRefresher;

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

    final static String getClientFinalUrl = "http://localhost:8080/web_Web/servlets/GetUserByName";

    @FXML
    public void initialize(){
        if (scrambleTabComponentController != null) {
            scrambleTabComponentController.setUserAppController(this);
            paymentTabComponentController.setUserAppController(this);
            newLoanTabComponentController.setUserAppController(this);
        }

        startLoanClientListRefresher();
        startInvestmentsClientListRefresher();
    }

    public void startInvestmentsClientListRefresher() {
        investmentsTableRefresher = new InvestmentsTableRefresher(
                this::updateInvestmentsTable);
        timer = new Timer();
        timer.schedule(investmentsTableRefresher, 2000, 2000);
    }

    public void updateInvestmentsTable(List<Loan> loansOfClient) {
        HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(userAppMainController.getClientsName()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

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
            for (int i = 0; i < loansOfClient.get(i).getLoaners().size(); i++) {
                if(loan.getLoaners().get(i).getKey().getName().equals(chosenClient.getName())){
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
        HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(userAppMainController.getClientsName()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

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
            if (loan.getOwner().getName().equals(chosenClient.getName())) {
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
            loanPopUpController.popUp(loan);
            loanPopUpController.fillLoanInfo(loan);
            //add style
        }
    }
//
    @FXML
    public void onMouseClickedInvestment(MouseEvent event)
    {
        if (event.getClickCount() == 2) //Checking double click
        {
            System.out.println(loansTableView.getSelectionModel().getSelectedItems().get(0).getOwner().getName());

        }
    }
    private boolean checkForChange(List<Loan> loansOfClient){
        int loanOfClientSize = getNumberOfLoans(loansOfClient);
        if (loanOfClientSize < investmentsTreeView.getRoot().getChildren().size()){
            return true;
        }
        for (Loan loan : loansOfClient) {
            if(loan.getOwner().getName().equals(chosenClient.getName())){

                TreeItem loanId =getTreeViewItem(investmentsTreeView.getRoot(), loan.getId());
                TreeItem status = getTreeViewItem(loanId, "Status");
                if (!status.toString().contains(loan.getStatus().toString())){
                    return true;
                }
                String totalPaid = getTreeViewItem(loanId, "Total paid: ").toString();
                Double paid = Double.parseDouble(totalPaid.split("Total paid: ")[1].split(" | ")[0]);
                if (paid != loan.getLoanPaid()){
                    return true;
                }

            }
        }
        return false;
    }



    private static TreeItem getTreeViewItem(TreeItem<String> item , String value) {
        if (item != null && item.getValue().contains(value))
            return  item;

        for (TreeItem<String> child : item.getChildren()){
            TreeItem<String> s=getTreeViewItem(child, value);
            if(s!=null)
                return s;

        }
        return null;
    }

    private void updateLoanTree(List<Loan> loansOfClient){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TreeItem<String> loans = new TreeItem<>("Loans");
                for (Loan loan : loansOfClient) {
                    if(loan.getOwner().getName().equals(chosenClient.getName())){
                        TreeItem<String> loanID = new TreeItem<>(loan.getId());
                        TreeItem<String> owner = new TreeItem<>("Loaner: " + loan.getOwner().getName());
                        TreeItem<String> status = new TreeItem<>("Status: " + loan.getStatus().toString());
                        TreeItem<String> category= new TreeItem<>("Category: " + loan.getLoanCategory().getCategory());
                        TreeItem<String> loanAmountAndTotalYaz = new TreeItem<>("Loan amount: " + loan.getLoan() + " | " + "Total YAZ: " + loan.getTotalYaz());

                        if(loan.getStatus().equals(Status.PENDING) || loan.getStatus().equals(Status.NEW)){
                            TreeItem<String> loaners = new TreeItem<>("Loaners");
                            List<Pair<Client,Double>> loanersList = loan.getLoaners();

                            for (Pair<Client, Double> clientDoublePair : loanersList) {
                                TreeItem<String> loaner = new TreeItem<>("Name: " + clientDoublePair.getKey().getName() +
                                        "| " + "Invested: " + clientDoublePair.getValue());
                                loaners.getChildren().add(loaner);
                            }
                            TreeItem<String> total =new TreeItem<>("Total paid: " +(loan.getLoan() - loan.getMissingToActive()) + " | " +
                                    "Missing: " + loan.getMissingToActive());
                            loanID.getChildren().add(total);

                        }
                        else if(loan.getStatus().equals(Status.ACTIVE)){
                            TreeItem<String> payments =new TreeItem<>("Total Loan paid (with no interest): " + loan.getLoanPaid() + " | Total interest paid: " + loan.getInterestPaid() + "\n"
                                    + "Loan left to pay(with no interest): " + (loan.getLoan() - loan.getLoanPaid()) + " | Interest left to pay: " + (loan.getInterest()- loan.getInterestPaid()));
                            loanID.getChildren().add(payments);
                        }
                        loanID.getChildren().add(owner);
                        loanID.getChildren().add(category);
                        loanID.getChildren().add(status);
                        loanID.getChildren().add(loanAmountAndTotalYaz);
                        loans.getChildren().add(loanID);
                    }
                }
                investmentsTreeView.setRoot(loans);

            }
        });
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
        infoTabSelected();
    }

    public void infoTabSelected(){

        transactionTableView.getItems().clear();
        String name = userAppMainController.getChosenClient();
        String finalUrl = getClientFinalUrl + "?ClientName=" + name;
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.exit(0);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOfClient = response.body().string();
                System.out.println(jsonOfClient);
                Client client = new Gson().fromJson(jsonOfClient, Client.class);
                if (!client.getTransactions().isEmpty()){
                    yazTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("yazOfTransaction"));
                    beforeChangeTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("original"));
                    moneyTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("moneyChange"));
                    afterChangeTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("afterChange"));
                    paymentTabComponentController.clearListView();
                    paymentTabComponentController.createLoansListView();
                    transactionTableView.getItems().addAll(client.getTransactions());
                }
            }
        });

    }

    public void refreshCategoriesInScramble(){
        scrambleTabComponentController.initializeCategoryCheckList();
    }

    public void setCurrentClient(String currentClient) {
        scrambleTabComponentController.setCurrentClient(currentClient);
    }

    public String getChosenClient(){
        return userAppMainController.getClientsName();
    }

    public void createLoanTreeForClient(String name){
        chosenClient = Clients.getClientByName(userAppMainController.getChosenClient());
        List<Loan> loansList = new ArrayList<>(Loans.getLoans());
        TreeItem<String> loans = new TreeItem<>("Loans");

        for (Loan loan : loansList) {
            if(loan.getOwner().getName().equals(chosenClient.getName())){
                TreeItem<String> loanID = new TreeItem<>(loan.getId());
                TreeItem<String> owner = new TreeItem<>("Loaner: " + loan.getOwner().getName());
                TreeItem<String> status = new TreeItem<>("Status: " + loan.getStatus().toString());
                TreeItem<String> category= new TreeItem<>("Category: " + loan.getLoanCategory().getCategory());
                TreeItem<String> loanAmountAndTotalYaz = new TreeItem<>("Loan amount: " + loan.getLoan() + " | " + "Total YAZ: " + loan.getTotalYaz());
                if(loan.getStatus().equals(Status.PENDING)){
                    TreeItem<String> loaners = new TreeItem<>("Loaners");
                    List<Pair<Client,Double>> loanersList = loan.getLoaners();

                    for (Pair<Client, Double> clientDoublePair : loanersList) {
                        TreeItem<String> loaner = new TreeItem<>("Name: " + clientDoublePair.getKey().getName() +
                                "| " + "Invested: " + clientDoublePair.getValue());
                        loaners.getChildren().add(loaner);
                    }
                    TreeItem<String> total =new TreeItem<>("Total paid: " +(loan.getLoan() - loan.getMissingToActive()) + " | " +
                            "Missing: " + loan.getMissingToActive());
                    loanID.getChildren().add(total);

                }
                else if(loan.getStatus().equals(Status.ACTIVE)){
                    TreeItem<String> payments =new TreeItem<>("Total Loan paid (with no interest): " + loan.getLoanPaid() + " | Total interest paid: " + loan.getInterestPaid() + "\n"
                            + "Loan left to pay(with no interest): " + (loan.getLoan() - loan.getLoanPaid()) + " | Interest left to pay: " + (loan.getInterest()- loan.getInterestPaid()));
                    loanID.getChildren().add(payments);
                }
                loanID.getChildren().add(owner);
                loanID.getChildren().add(category);
                loanID.getChildren().add(status);
                loanID.getChildren().add(loanAmountAndTotalYaz);


                loans.getChildren().add(loanID);
            }

        }
        investmentsTreeView.setRoot(loans);
    }

    public void createInvestmentTreeForClient(String name){
        List<Loan> loansList = new ArrayList<>(Loans.getLoans());
        TreeItem<String> loans = new TreeItem<>("Investments");

        for (Loan loan : loansList) {
            if(chosenClient.isInvestor(loan)){
                TreeItem<String> loanID = new TreeItem<>(loan.getId());
                TreeItem<String> owner = new TreeItem<>("Loaner: " + loan.getOwner().getName());
                TreeItem<String> status = new TreeItem<>("Status: " + loan.getStatus().toString());
                TreeItem<String> category= new TreeItem<>("Category: " + loan.getLoanCategory().getCategory());
                TreeItem<String> loanAmountAndTotalYaz = new TreeItem<>("Loan amount: " + loan.getLoan() + " | " + "Total YAZ: " + loan.getTotalYaz());
                if(loan.getStatus().equals(Status.PENDING)){
                    TreeItem<String> loaners = new TreeItem<>("Loaners");
                    List<Pair<Client,Double>> loanersList = loan.getLoaners();

                    for (Pair<Client, Double> clientDoublePair : loanersList) {
                        TreeItem<String> loaner = new TreeItem<>("Name: " + clientDoublePair.getKey().getName() +
                                "| " + "Invested: " + clientDoublePair.getValue());
                        loaners.getChildren().add(loaner);
                    }
                    TreeItem<String> total =new TreeItem<>("Total paid: " +(loan.getLoan() - loan.getMissingToActive()) + " | " +
                            "Missing: " + loan.getMissingToActive());
                    loanID.getChildren().add(total);

                }
                else if(loan.getStatus().equals(Status.ACTIVE)){
                    TreeItem<String> payments =new TreeItem<>("Total Loan paid (with no interest): " + loan.getLoanPaid() + " | Total interest paid: " + loan.getInterestPaid() + "\n"
                            + "Loan left to pay(with no interest): " + (loan.getLoan() - loan.getLoanPaid()) + " | Interest left to pay: " + (loan.getInterest()- loan.getInterestPaid()));
                    loanID.getChildren().add(payments);
                }
                else if(loan.getStatus().equals(Status.RISK)){
                    TreeItem<String> payments =new TreeItem<>("Total Loan paid (with no interest): " + loan.getLoanPaid() + " | Total interest paid: " + loan.getInterestPaid() + "\n"
                            + "Loan left to pay(with no interest): " + (loan.getLoan() - loan.getLoanPaid()) + " | Interest left to pay: " + (loan.getInterest()- loan.getInterestPaid()));
                    TreeItem<String> missedPayments = new TreeItem<>("Total amount missed: " + loan.getTotalAmountMissed());

                    loanID.getChildren().add(payments);
                    loanID.getChildren().add(missedPayments);
                }
                else if(loan.getStatus().equals(Status.FINISHED)){
                    TreeItem<String> finished = new TreeItem<>("Loan finished in Yaz: " + loan.getFinishYaz());
                    loanID.getChildren().add(finished);
                }
                loanID.getChildren().add(owner);
                loanID.getChildren().add(category);
                loanID.getChildren().add(status);
                loanID.getChildren().add(loanAmountAndTotalYaz);

                //loanID.getChildren().addAll(owner, category, status, loanAmountAndTotalYaz);
                loans.getChildren().add(loanID);
            }
        }

        loansTreeView.setRoot(loans);
    }

    public void createTransactionInfo(String name){
        Clients.getClientByName(name);
    }

    public void resetUI() {
        investmentsTreeView.setRoot(null);
        loansTreeView.setRoot(null);
        scrambleTabComponentController.resetUI();
    }

    public void createLoanTreeForClientForPaymentTab() {
        paymentTabComponentController.createLoanTreeForClientForPaymentTab();
    }

    public void createCategoryList() {
        scrambleTabComponentController.initializeCategoryCheckList();
    }

    public void resetUIforClient() {
        investmentsTreeView.setRoot(null);
        loansTreeView.setRoot(null);
    }

    public ObservableList<String> getCurrentStyle(){
        return loansTreeView.getScene().getStylesheets();
    }

    public void setUserAppMainController(UserAppMainController userAppMainController) {
        this.userAppMainController = userAppMainController;
    }
}
