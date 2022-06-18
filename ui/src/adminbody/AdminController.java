package adminbody;

import appcontroller.AppController;
import client.Client;
import client.Clients;
import header.HeaderController;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import javafx.util.Pair;
import load.LoadFile;
import loan.Loan;
import loan.Loans;
import loan.Status;
import time.Yaz;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController {

    private DoubleProperty doubleProperty;
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
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    void IncreaseYaz(ActionEvent event) {
        Yaz.advanceYaz(1);
        mainController.updateYazLabel();
        createClientTree();
        createLoansTree();
    }


    @FXML
    void ReadFile(ActionEvent event) throws Exception{

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    LoadFile.setPath(selectedFile.getPath());
                    for (double i = 0; i < 6; i++) {
                        progressBar.setProgress(i/10);
                        Thread.sleep(200);
                    }
                    progressBar.setProgress(0.5);
                    for (double i = 6; i < 10; i++) {
                        progressBar.setProgress(i/10);
                        Thread.sleep(200);
                    }
                    //LoadFile.readFile();
                    progressBar.setProgress(1);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(LoadFile.isFileLoaded()){
                                mainController.resetUI();
                                mainController.updatePathLabel();
                                createClientTree();
                                createLoansTree();
                                mainController.refreshCategoriesInScramble();
                                mainController.updateComboBox();
                                mainController.initializePathToolTip();


                            }
                        }
                    });
                    return null;
                }
            };
            Thread t= new Thread(task);
            t.start();
            //progressBar.progressProperty().bind(task.progressProperty());

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
        loansTreeView.setRoot(loans);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void createClientTree(){

        List<Client> clientList = new ArrayList<>(Clients.getClientsList());
        TreeItem <String> clients = new TreeItem<>("Clients");
        for (Client client : clientList) {
            TreeItem<String> name = new TreeItem<>(client.getName());
            TreeItem<String> money = new TreeItem<>("Money in the bank:" + (client.getMoney()));
            Map<Status, List<String>> loansAsInvestor = getInvestorMap(client);
            Map<Status, List<String>> loansAsLoaner = getLoansMap(client);
            TreeItem<String> investments = new TreeItem<>("Investments");
            for (Map.Entry<Status, List<String>> set : loansAsInvestor.entrySet()){
                TreeItem <String> status = new TreeItem<>(set.getKey().toString());
                for (int j = 0; j < set.getValue().size(); j++) {
                    TreeItem<String> investment = new TreeItem<>(set.getValue().get(j));
                    status.getChildren().add(investment);
                }
                investments.getChildren().add(status);
            }

            TreeItem<String> loans = new TreeItem<>("Loans");
            for (Map.Entry<Status, List<String>> set : loansAsLoaner.entrySet()){
                TreeItem <String> status = new TreeItem<>(set.getKey().toString());
                for (int j = 0; j < set.getValue().size(); j++) {
                    TreeItem<String> investment = new TreeItem<>("Loan ID: " + set.getValue().get(j));
                    status.getChildren().add(investment);
                }
                loans.getChildren().add(status);
            }

            name.getChildren().add(money);
            name.getChildren().add(investments);
            name.getChildren().add(loans);
            clients.getChildren().add(name);

        }
        clientsTreeView.setRoot(clients);
        //mainController.updateComboBox();
    }

    private Map<Status, List<String>> getLoansMap(Client client) {
        Map<Status, List<String>> clientsLoans = new HashMap<>();
        List<Loan> loans = Loans.getLoans();
        for (Loan loan : loans) {
            if (loan.getOwner().getName().equals(client.getName())) {
                if (!clientsLoans.containsKey(loan.getStatus())) {
                    clientsLoans.put(loan.getStatus(), new ArrayList<String>());
                }
                clientsLoans.get(loan.getStatus()).add(loan.getId());
            }
        }


        return clientsLoans;
    }

    private Map<Status, List<String>> getInvestorMap(Client client) {
        Map<Status, List<String>> loansAsInvestor = new HashMap<>();
        List<Loan> loans = new ArrayList<>(Loans.getLoans());
        for (Loan loan : loans) {
            List<Pair<Client, Double>> investors = new ArrayList<>(loan.getLoaners());
            for (Pair<Client, Double> investor : investors) {
                if (investor.getKey().getName().equals(client.getName())) {
                    if (!loansAsInvestor.containsKey(loan.getStatus())) {
                        loansAsInvestor.put(loan.getStatus(), new ArrayList<String>());

                    }
                    loansAsInvestor.get(loan.getStatus()).add(loan.getId());
                }
            }
        }
        return loansAsInvestor;
    }

    @FXML
    void initialize() {
    }


}
