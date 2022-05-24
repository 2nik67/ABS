package paymenttab;

import client.Client;
import clientbody.ClientController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import loan.Loan;
import loan.Loans;
import loan.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

public class PaymentTabController {

    private ClientController clientController;

    @FXML
    private TreeView<String> loansTreeView;

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
        autoPayBtn.setDisable(true);
        ObservableList<String> observableList = loansListView.getItems();
        observableList.remove(loansListView.getSelectionModel().getSelectedItem());
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
        }

    }

    @FXML
    private void initialize() {
        loansListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null && Loans.getLoanByID(newValue).timeToPay() && (Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem()).isActive() ||
                    (Loans.getLoanByID(loansListView.getSelectionModel().getSelectedItem()).getStatus().equals(Status.RISK)))){
                autoPayBtn.setDisable(false);
            }
            else
                autoPayBtn.setDisable(true);

            closeLoanBtn.setDisable(false);
        });

    }


    public void createLoansListView() {

        loansListView.getItems().clear();
        List<Loan> loanList= Loans.getLoans();
        for (Loan loan : loanList) {
            if (loan.getOwner().getName().equals(clientController.getChosenClient()) && (loan.getStatus().equals(Status.RISK) || loan.getStatus().equals(Status.ACTIVE))) {
                loansListView.getItems().add(loan.getId());
            }
        }

    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void clearListView(){
        loansListView.getItems().clear();
    }
}