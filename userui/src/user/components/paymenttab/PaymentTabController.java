package user.components.paymenttab;

import client.Client;
import client.Clients;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import loan.Loan;
import loan.Loans;
import loan.Status;
import loan.category.Category;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.components.userapp.LoanTableRefresher;
import user.components.userapp.UserAppController;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

public class PaymentTabController{

    private TimerTask loanOfClientRefresher;
    private Timer timer;

    private Client chosenClient;

    private UserAppController userAppController;

    @FXML
    private TextField moneyTextFiled;

    @FXML
    private Button payBtn;

    @FXML
    private TreeView<String> loansTreeView;

    @FXML
    private Button closeLoanBtn;

    @FXML
    private Button autoPayBtn;

    @FXML
    private ListView<Loan> loansListView;

    @FXML
    void autoPayOnAction(ActionEvent event) {
        Loan loan = loansListView.getSelectionModel().getSelectedItem();
        String body = "";
        RequestBody requestBody = RequestBody.create(body.getBytes());
        HttpClientUtil.runAsyncPost(HttpClientUtil.createCloseLoanOrAutoPayUrl(loan), requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("Not Enough Money" + System.lineSeparator())){
                    System.out.println("Not enough money in the bank account");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("");
                            alert.setContentText("Not enough money in the bank account");
                            alert.show();
                        }
                    });
                }else{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //closeLoanBtn.setDisable(true);
                            //payBtn.setDisable(true);
                            //moneyTextFiled.setDisable(true);
                            autoPayBtn.setDisable(true);


                        }
                    });
                }
            }
        });
/*
        Loan loanToPay = loansListView.getSelectionModel().getSelectedItem();
        loanToPay.payLoan();
        payBtn.setDisable(true);
        moneyTextFiled.setDisable(true);
        autoPayBtn.setDisable(true);
        closeLoanBtn.setDisable(true);
        ObservableList<Loan> observableList = loansListView.getItems();
        observableList.remove(loansListView.getSelectionModel().getSelectedItem());*/
    }

    @FXML
    void payBtnOnAction(ActionEvent event) {
        //TODO: check that the text field is less than the amount left to pay
        Loan loan = loansListView.getSelectionModel().getSelectedItem();
        HttpClientUtil.runAsyncHead(HttpClientUtil.createManualPayLoanUrl(loan, moneyTextFiled.getText()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("Not Enough Money" + System.lineSeparator())){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("");
                            alert.setContentText("Not enough money in the bank account");
                            alert.show();
                        }
                    });
                }else{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            closeLoanBtn.setDisable(true);
                            payBtn.setDisable(true);
                            //moneyTextFiled.setDisable(true);
                            autoPayBtn.setDisable(true);
                        }
                    });
                }
            }
        });


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
            ObservableList<Loan> observableList = loansListView.getItems();
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
        Loan loan = loansListView.getSelectionModel().getSelectedItem();

        HttpClientUtil.runAsync(HttpClientUtil.createCloseLoanOrAutoPayUrl(loan), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("Not Enough Money" + System.lineSeparator())){
                    System.out.println("Not enough money in the bank account");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("");
                            alert.setContentText("Not enough money in the bank account");
                            alert.show();
                        }
                    });
                }else{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            closeLoanBtn.setDisable(true);
                            payBtn.setDisable(true);
                            moneyTextFiled.setDisable(true);
                            autoPayBtn.setDisable(true);
                            closeLoanBtn.setDisable(true);
                        }
                    });
                }
            }
        });


    }



    @FXML
    private void initialize() {

        startLoanClientListRefresher();
        initializeTextFields();

        loansListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Loan>() {
            @Override
            public void changed(ObservableValue<? extends Loan> observable, Loan oldValue, Loan newValue) {
                if (observable == null || newValue == null){
                    autoPayBtn.setDisable(true);
                    moneyTextFiled.setDisable(true);
                    closeLoanBtn.setDisable(true);
                    payBtn.setDisable(true);
                    return;
                }
                if (newValue.getStatus().equals(Status.RISK)){
                    autoPayBtn.setDisable(true);

                }else{
                    autoPayBtn.setDisable(!newValue.timeToPay());
                }
                moneyTextFiled.setDisable(false);
                closeLoanBtn.setDisable(false);
                payBtn.setDisable(false);


            }
        });

        loansListView.setCellFactory(lv -> new ListCell<Loan>(){
            @Override
            protected void updateItem(Loan item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    return;
                } else {
                    setText(item.getId());
                    if (item.timeToPay() && item.getStatus().equals(Status.ACTIVE)){
                        setTextFill(Color.GREEN);
                    }
                    else if(item.getStatus().equals(Status.ACTIVE)){
                        setTextFill(Color.BLUE);
                    }
                    else if(item.getStatus().equals(Status.RISK)){
                        setTextFill(Color.RED);
                    }
                }
            }
        });



    }

    private void startLoanClientListRefresher() {
        loanOfClientRefresher = new LoanTableRefresher(
                this::updateDataLoanTable);
        timer = new Timer();
        timer.schedule(loanOfClientRefresher, 2000, 2000);
    }

    private void updateDataLoanTable(List<Loan> loansOfClient) {
        HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(userAppController.getChosenClient()), new Callback() {
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


        List <Loan> loans1 = new ArrayList<>();
        for (Loan loan : loansOfClient) {
            if (loan.getOwner().getName().equals(chosenClient.getName())) {
                if (loan.getStatus().equals(Status.RISK) || loan.getStatus().equals(Status.ACTIVE))
                    loans1.add(loan);
            }
        }
        if (loans1.isEmpty()){
            loansListView.getItems().clear();
            return;
        }

        if (loans1.size() > loansListView.getItems().size() || statusWasChanged(loans1, loansListView.getItems())){
            loansListView.getItems().clear();
            loansListView.getItems().addAll(loans1);
        }

    }

    private boolean statusWasChanged(List<Loan> loans1, ObservableList<Loan> items) {
        for (Loan loan : loans1) {
            for (Loan item : items) {
                if (loan.getId().equals(item.getId())) {
                    return !loan.getStatus().equals(item.getStatus());
                }
            }
        }
        return true;
    }


    public void setUserAppController(UserAppController userAppController) {
        this.userAppController = userAppController;
    }


    public void clearListView(){
        loansListView.getItems().clear();
    }
}