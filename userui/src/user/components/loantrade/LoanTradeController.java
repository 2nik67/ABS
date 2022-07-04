package user.components.loantrade;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class LoanTradeController {

    private static UserAppController userAppController;

    @FXML
    private ScrollPane loanTradeTabComponent;

    @FXML
    private ListView<Loan> sellLoansListView;

    @FXML
    private Button sellLoanBtn;

    @FXML
    private ListView<Loan> buyLoansListView;

    @FXML
    private Button buyLoanBtn;

    private LoanTableRefresher loanOfClientRefresher;
    private Timer timer;

    @FXML
    private Label priceTagLabel;

    public static void setUserAppController(UserAppController userAppController1) {
        userAppController = userAppController1;
    }


    @FXML
    private void initialize() {
        startLoanClientListRefresher();
        buyLoansListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Loan>() {
            @Override
            public void changed(ObservableValue<? extends Loan> observable, Loan oldValue, Loan newValue) {
                if (newValue == null){
                    priceTagLabel.setVisible(false);
                }else{
                    priceTagLabel.setVisible(true);
                    priceTagLabel.setText(newValue.getId() + " costs " + (newValue.getLoan()-newValue.getLoanPaid()));
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
        List<Loan> sellLoans = new ArrayList<>();
        List<Loan> buyLoan = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getOwner().getName().equals(userAppController.getChosenClient()) && !loan.isForSale()) {
                if (loan.getStatus().equals(Status.ACTIVE)) {
                    sellLoans.add(loan);
                }
            }
        }
        if (sellLoans.size() != sellLoansListView.getItems().size()){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    sellLoansListView.getItems().clear();
                    sellLoansListView.getItems().addAll(sellLoans);
                }
            });
        }

        for (Loan loan : loans) {
            if (!loan.getOwner().getName().equals(userAppController.getChosenClient())) {
                if (loan.getStatus().equals(Status.ACTIVE) && loan.isForSale()) {
                    buyLoan.add(loan);
                }
            }
        }

        if (buyLoan.size() != buyLoansListView.getItems().size()){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    buyLoansListView.getItems().clear();
                    buyLoansListView.getItems().addAll(buyLoan);
                }
            });
        }


    }

    @FXML
    void buyLoanOnAction(ActionEvent event) {
        HttpClientUtil.runAsync(HttpClientUtil.createTradeLoanUrl(buyLoansListView.getSelectionModel().getSelectedItem().getId(), false, userAppController.getChosenClient()), new Callback() {
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
        HttpClientUtil.runAsync(HttpClientUtil.createTradeLoanUrl(sellLoansListView.getSelectionModel().getSelectedItem().getId(), true, userAppController.getChosenClient()), new Callback() {
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