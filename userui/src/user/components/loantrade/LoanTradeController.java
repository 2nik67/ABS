package user.components.loantrade;

import client.Client;
import client.InvestmentForSale;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import loan.Loan;
import loan.Status;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.components.userapp.LoanTableRefresher;
import user.components.userapp.UserAppController;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.util.*;

public class LoanTradeController {

    private static UserAppController userAppController;

    @FXML
    private ScrollPane loanTradeTabComponent;

    @FXML
    private ListView<Loan> sellLoansListView;

    @FXML
    private Button sellLoanBtn;

    @FXML
    private ListView<String> buyLoansListView;

    @FXML
    private Button buyLoanBtn;

    private LoanTableRefresher loanOfClientRefresher;
    private Timer timer;

    @FXML
    private Label priceTagLabel;

    private Client client;
    private Client sellerOfLoan;


    public static void setUserAppController(UserAppController userAppController1) {
        userAppController = userAppController1;
    }


    @FXML
    private void initialize() {
        startLoanClientListRefresher();
        buyLoansListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue!=null){
                    String[] arr =newValue.split(" ", 2);
                    String seller = arr[0];
                    String loanId = arr[1];
                    HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(seller), new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            System.out.println(e);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String jsonArrayOfUsersNames = response.body().string();
                            if (!jsonArrayOfUsersNames.equals("[]" + System.lineSeparator())){
                                sellerOfLoan = new Gson().fromJson(jsonArrayOfUsersNames, Client.class);
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    priceTagLabel.setText("Price: " + sellerOfLoan.getInvestments().get(loanId).getInvestment());
                                    System.out.println("FFS WORK " + "Price: " + sellerOfLoan.getInvestments().get(loanId).getInvestment());
                                }
                            });
                        }
                    });
                }
            }
        });


    }
    public void startLoanClientListRefresher() {
        loanOfClientRefresher = new LoanTableRefresher(
                this::updateDataLoanTable);
        timer = new Timer();
        timer.schedule(loanOfClientRefresher, 2000, 2000);
    }

    private void updateDataLoanTable(List<Loan> loans) {
        if (userAppController.getChosenClient() == null){
            return;
        }
        HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(userAppController.getChosenClient()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                if (!jsonArrayOfUsersNames.equals("[]" + System.lineSeparator())){
                    client = new Gson().fromJson(jsonArrayOfUsersNames, Client.class);
                }
            }
        });

        if (client == null){
            return;
        }
        Map<String, InvestmentForSale> investments = client.getInvestments();
        List<Loan> sellLoans = getSellLoan(investments, loans);
        Set<String> buyLoan = getBuyLoanSet(investments, loans);


        if (sellLoans.isEmpty()){
            Platform.runLater(() -> {
                sellLoansListView.getItems().clear();
            });
        }else if(sellLoans.size() != sellLoansListView.getItems().size()){
            Platform.runLater(() -> {
                sellLoansListView.getItems().clear();
                sellLoansListView.getItems().addAll(sellLoans);
            });
        }


        if (buyLoan.isEmpty()){
            Platform.runLater(() -> {
                buyLoansListView.getItems().clear();
            });
        }
        else if (buyLoan.size() != buyLoansListView.getItems().size()){
            Platform.runLater(() -> {
                buyLoansListView.getItems().clear();
                buyLoansListView.getItems().addAll(buyLoan);
            });
        }









    }

    private Set<String> getBuyLoanSet(Map<String, InvestmentForSale> investments, List<Loan> loans) {
        Set<String> buyLoan = new HashSet<>();
        for (Loan loan : loans) {
            if (!loan.getOwner().getName().equals(client.getName()) && loan.getStatus().equals(Status.ACTIVE)) {
                for (int i = 0; i < loan.getUpdatedLoaners().size(); i++) {
                    if (!loan.getUpdatedLoaners().get(i).getKey().getName().equals(client.getName()) &&
                            loan.getUpdatedLoaners().get(i).getKey().getInvestments().get(loan.getId()).isForSale()) {
                        buyLoan.add(loan.getUpdatedLoaners().get(i).getKey().getName() + " " + loan.getId());
                    }
                }
            }
        }
        return buyLoan;
    }

    private List<Loan> getSellLoan(Map<String, InvestmentForSale> investments, List<Loan> loans) {
        List<Loan> sellLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (investments.containsKey(loan.getId())) {
                if (loan.getStatus().equals(Status.ACTIVE) && !investments.get(loan.getId()).isForSale()) {
                    sellLoans.add(loan);
                }
            }
        }
        return sellLoans;
    }


    @FXML
    void buyLoanOnAction(ActionEvent event) {
        String[] arr = buyLoansListView.getSelectionModel().getSelectedItem().split(" ", 2);
        String buyFrom = arr[0];
        String loanId= arr[1];
        if (client.getMoney() < sellerOfLoan.getInvestments().get(loanId).getInvestment()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Not enough money in the bank");
            alert.showAndWait();
            return;
        }
        HttpClientUtil.runAsync(HttpClientUtil.createTradeLoanUrl(buyFrom, loanId, false, userAppController.getChosenClient()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    @FXML
    void sellBtnOnAction(ActionEvent event) {
        HttpClientUtil.runAsync(HttpClientUtil.createTradeLoanUrl("", sellLoansListView.getSelectionModel().getSelectedItem().getId(), true, userAppController.getChosenClient()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }


}