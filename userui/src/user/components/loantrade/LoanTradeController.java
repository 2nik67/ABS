package user.components.loantrade;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    public static void setUserAppController(UserAppController userAppController1) {
        userAppController = userAppController1;
    }


    @FXML
    private void initialize() {
        startLoanClientListRefresher();

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
            sellLoansListView.getItems().clear();
            sellLoansListView.getItems().addAll(sellLoans);
        }

        for (Loan loan : loans) {
            if (!loan.getOwner().getName().equals(userAppController.getChosenClient())) {
                if (loan.getStatus().equals(Status.ACTIVE) && loan.isForSale()) {
                    buyLoan.add(loan);
                }
            }
        }

        if (buyLoan.size() != buyLoansListView.getItems().size()){
            buyLoansListView.getItems().clear();
            buyLoansListView.getItems().addAll(buyLoan);
        }


    }

    @FXML
    void buyLoanOnAction(ActionEvent event) {
        HttpClientUtil.runAsync(HttpClientUtil.createTradeLoanUrl(sellLoansListView.getSelectionModel().getSelectedItem().getId(), false), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("Sold!");
            }
        });
    }

    @FXML
    void sellBtnOnAction(ActionEvent event) {
        HttpClientUtil.runAsync(HttpClientUtil.createTradeLoanUrl(sellLoansListView.getSelectionModel().getSelectedItem().getId(), true), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("Sold!");
            }
        });
    }


}