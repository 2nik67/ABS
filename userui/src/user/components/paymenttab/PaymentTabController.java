package user.components.paymenttab;

import client.Client;
import client.Clients;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import loan.Loan;
import loan.Loans;
import loan.Status;
import user.components.userapp.UserAppController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class PaymentTabController{

    private UserAppController userAppController;

    @FXML
    private TextField moneyTextFiled;

    @FXML
    private Button payBtn;

    @FXML
    private TreeView<String> loansTreeView;
//
    @FXML
    private Button closeLoanBtn;

    @FXML
    private Button autoPayBtn;

    @FXML
    private ListView<String> loansListView;

    @FXML
    void autoPayOnAction(ActionEvent event) {
        Loan loanToPay = Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem());
        loanToPay.payLoan();
        payBtn.setDisable(true);
        moneyTextFiled.setDisable(true);
        autoPayBtn.setDisable(true);
        closeLoanBtn.setDisable(true);
        ObservableList<String> observableList = loansListView.getItems();
        observableList.remove(loansListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    void payBtnOnAction(ActionEvent event) {
        Loan loan = Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem());
        if (loan.getOwner().getMoney() < Double.parseDouble(moneyTextFiled.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("");
            alert.setContentText("Not enough money in your balance.");
            alert.show();
        }
        else {
            loan.payPartOfLoan(Double.parseDouble(moneyTextFiled.getText()));
            payBtn.setDisable(true);
            moneyTextFiled.setDisable(true);
            autoPayBtn.setDisable(true);
            closeLoanBtn.setDisable(true);
            ObservableList<String> observableList = loansListView.getItems();
            observableList.remove(loansListView.getSelectionModel().getSelectedItem());
        }
    }

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([0-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    public void initializeTextFields(){
        moneyTextFiled.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));
    }

    @FXML
    void closeLoanOnAction(ActionEvent event) {
        Loan loan = Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem());
        if(!loan.payFullLoan()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("");
            alert.setContentText("Not enough money in the bank account");
        }
        else{
            createLoansListView();
            ObservableList<String> observableList = loansListView.getItems();
            observableList.remove(loansListView.getSelectionModel().getSelectedItem());
            closeLoanBtn.setDisable(true);
            payBtn.setDisable(true);
            moneyTextFiled.setDisable(true);
            autoPayBtn.setDisable(true);
            closeLoanBtn.setDisable(true);
        }


    }

    @FXML
    private void initialize() {
        initializeTextFields();
        loansListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null &&
                    Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem()).getStatus().equals(Status.RISK)){
                payBtn.setDisable(false);
                moneyTextFiled.setDisable(false);
                closeLoanBtn.setDisable(false);
            }
            else{
                payBtn.setDisable(true);
                moneyTextFiled.setDisable(true);
                closeLoanBtn.setDisable(false);
            }
            if (newValue!=null && Loans.getLoanByID(newValue).timeToPay() && (Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem()).isActive() ||
                    (Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem()).getStatus().equals(Status.RISK)))){

                autoPayBtn.setDisable(false);
                closeLoanBtn.setDisable(false);
            }
            else{
                autoPayBtn.setDisable(true);

                closeLoanBtn.setDisable(false);
            }

        });

        loansListView.setCellFactory(lv -> new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {

                } else {
                    setText(item);
                    if (Loans.getLoanByID(item).timeToPay() && Loans.getLoanByID(item).getStatus().equals(Status.ACTIVE)){
                        setTextFill(Color.GREEN);
                    }
                    else if(Loans.getLoanByID(item).getStatus().equals(Status.ACTIVE)){
                        setTextFill(Color.BLUE);
                    }
                    else if(Loans.getLoanByID(item).getStatus().equals(Status.RISK)){
                        setTextFill(Color.RED);
                    }



                }
            }
        });



    }



    public void createLoansListView() {

        loansListView.getItems().clear();
        List<Loan> loanList= Loans.getLoans();
        for (Loan loan : loanList) {
            if (loan.getOwner().getName().equals(userAppController.getChosenClient()) && (loan.getStatus().equals(Status.RISK) || loan.getStatus().equals(Status.ACTIVE))) {
                loansListView.getItems().add(loan.getId());
            }
        }

    }

    public void setUserAppController(UserAppController userAppController) {
        this.userAppController = userAppController;
    }

    public void createLoanTreeForClientForPaymentTab(){
        Client chosenClient = Clients.getClientByName(userAppController.getChosenClient());
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

    public void clearListView(){
        loansListView.getItems().clear();
    }
}