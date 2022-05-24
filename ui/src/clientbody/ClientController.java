package clientbody;

import appcontroller.AppController;
import client.Client;
import client.Clients;
import client.Transaction;
import depositwithdraw.DepositWithdrawController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import loan.Loan;
import loan.Loans;
import loan.Status;
import paymenttab.PaymentTabController;
import possibleloans.PossibleLoansController;
import scrambletab.ScrambleTabController;
import time.Yaz;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class ClientController {
    private PossibleLoansController possibleLoansController;
    private DepositWithdrawController depositWithdrawController;
    private Client chosenClient;

    @FXML
    private ScrollPane paymentTabComponent;

    @FXML
    private PaymentTabController paymentTabComponentController;

    @FXML
    private TableColumn<Transaction, Double> beforeChangeTableColumn;

    @FXML
    private TableView<Transaction> transactionTableView;

    @FXML
    private TableColumn<Transaction, Integer> yazTableColumn;

    @FXML
    private TableColumn<Transaction, Double> moneyTableColumn;

    @FXML
    private TableColumn<Transaction, Double> afterChangeTableColumn;

    @FXML
    private ScrollPane scrambleTabComponent;

    @FXML
    private ScrambleTabController scrambleTabComponentController;

    @FXML
    private AppController mainController;

    @FXML
    private Tab informationTab;

    @FXML
    private TreeView<String> loansTreeView;

    @FXML
    private TreeView<String> investmentsTreeView;

    @FXML
    private Button depositBtn;

    @FXML
    private Button withdrawBtn;

    @FXML
    private Tab scrambleTab;

    @FXML
    private Tab paymentTab;

    @FXML
    public void initialize(){
        if (scrambleTabComponentController != null) {
            System.out.println("Banana");
            scrambleTabComponentController.setClientController(this);
            paymentTabComponentController.setClientController(this);
        }
    }



    @FXML
    public void depositBtnOnAction (ActionEvent event) throws Exception{
        depositWithdrawController = new DepositWithdrawController();
        depositWithdrawController.setCurrentClient(Clients.getClientByName(mainController.getChosenClient()));
        DepositWithdrawController.setClientController(this);
        depositWithdrawController.popUp(mainController.getChosenClient());
        infoTabSelected();
    }

    @FXML
    public void paymentTabSelected(ActionEvent event) throws Exception{

    }


    public void infoTabSelected(){
        transactionTableView.getItems().clear();
        String name = mainController.getChosenClient();
        Client client = Clients.getClientByName(name);
        yazTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("yazOfTransaction"));
        beforeChangeTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("original"));
        moneyTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("moneyChange"));
        afterChangeTableColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("afterChange"));
        paymentTabComponentController.clearListView();
        paymentTabComponentController.createLoansListView();
        transactionTableView.getItems().addAll(client.getTransactions());

    }



    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void refreshCategoriesInScramble(){
        scrambleTabComponentController.initializeCategoryCheckList();
    }

    public void setCurrentClient(String currentClient) {
        scrambleTabComponentController.setCurrentClient(currentClient);
    }
    public String getChosenClient(){
        return mainController.getChosenClient();
    }

    public void createLoanTreeForClient(String name){
        chosenClient = Clients.getClientByName(mainController.getChosenClient());
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
}
