package Admin;

import AppController.AppController;
import client.Client;
import client.Clients;
import client.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import javafx.util.Pair;
import load.LoadFile;
import loan.Loan;
import loan.Loans;
import loan.Status;
import time.Yaz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private AppController mainController;

    @FXML
    private Button increaseYazBtn;

    @FXML
    private Button loadFileBtn;

    @FXML
    private TreeView<String> loansTreeView;

    @FXML
    private TreeView<String> clientsTreeView;

    @FXML
    void IncreaseYaz(ActionEvent event) {
        Yaz.advanceYaz(1);
        mainController.updateYazLabel();

    }

    @FXML
    void ReadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            LoadFile.setPath(selectedFile.getPath());
            LoadFile.readFile();
            if(LoadFile.isFileLoaded()){
                mainController.updatePathLabel();
                createClientTree();
                createLoansTree();
            }

        }

    }

    private void createLoansTree() {
        List<Loan> loansList = new ArrayList<>(Loans.getLoans());
        TreeItem<String> loans = new TreeItem<>("Loans");

        for (Loan loan : loansList) {
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
                loanID.getChildren().add(4, total);//TODO: check the index part.

            }
            else if(loan.getStatus().equals(Status.ACTIVE)){
                TreeItem<String> payments =new TreeItem<>("Total Loan paid (with no interest): " + loan.getLoanPaid() + " | Total interest paid: " + loan.getInterestPaid() + "\n"
                        + "Loan left to pay(with no interest): " + (loan.getLoan() - loan.getLoanPaid()) + " | Interest left to pay: " + (loan.getInterest()- loan.getInterestPaid()));
                loanID.getChildren().add(4, payments);
            }
            loanID.getChildren().addAll(owner, category, status, loanAmountAndTotalYaz);
            loans.getChildren().add(loanID);



        }
        loansTreeView.setRoot(loans);
    }




    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    private void createClientTree(){

        List<Client> clientList = new ArrayList<>(Clients.getClientsList());
        TreeItem <String> clients = new TreeItem<>("clients");
        for (Client client : clientList) {
            List<Transaction> transactions = new ArrayList<>(client.getTransactions());
            TreeItem<String> name = new TreeItem<>(client.getName());
            TreeItem<String> money = new TreeItem<>("Money in the bank:" + (client.getMoney()));
            TreeItem<String> transactionsText = new TreeItem<>("Transaction: ");
            for (Transaction transaction:transactions){
                TreeItem<String> moneychange;
                if(transaction.getMoneyChange() > 0){
                    moneychange = new TreeItem<>("Money deposited: " + transaction.getMoneyChange() +
                            " | " + "Yaz of transaction: " + transaction.getYazOfTransaction());
                }
                else{
                    moneychange = new TreeItem<>("Money withdrawn: "+ -1*transaction.getMoneyChange() + " | " +
                            "Yaz of transaction: " + transaction.getYazOfTransaction());
                }
                transactionsText.getChildren().addAll(moneychange);

            }
            name.getChildren().addAll(money, transactionsText);
            clients.getChildren().addAll(name);
        }
        clientsTreeView.setRoot(clients);
        mainController.updateComboBox();
    }
    @FXML
    void initialize() {





    }
}
