package admin.components.adminapp;

import admin.components.main.AdminAppMainController;
import admin.userlist.UserListRefresher;
import admin.utils.HttpClientUtil;
import client.Clients;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import loan.Loan;
import loan.Status;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import user.components.userapp.LoanTableRefresher;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminAppController {

    private TimerTask loanRefresher;
    private TimerTask clientRefresher;
    private Timer timer;

    private boolean isRewind = false;

    private int currentYaz = 1;
    private int rewindYaz = 1;

    private AdminAppMainController adminAppMainController;

    @FXML
    private ScrollPane scrollPaneAdmin;

    @FXML
    private ListView<Loan> loansListView;

    @FXML
    private ListView<String> clientsListView;

    @FXML
    private Button increaseYazBtn;

    @FXML
    private Button decreaseYazBtn;

    @FXML
    private CheckBox rewindCheckBox;

    public void setAdminAppMainController(AdminAppMainController adminAppMainController){
        this.adminAppMainController = adminAppMainController;
    }

    @FXML
    void IncreaseYaz(ActionEvent event) {
        if (isRewind && this.rewindYaz < this.currentYaz) {
            adminAppMainController.increaseYaz();
            this.rewindYaz++;
            if(decreaseYazBtn.isDisabled())
                decreaseYazBtn.setDisable(false);
            if(this.rewindYaz == this.currentYaz)
                increaseYazBtn.setDisable(true);

            HttpClientUtil.runAsync(HttpClientUtil.createRewindUrl(this.rewindYaz), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    response.close();
                }
            });
            return;
        } else {
            adminAppMainController.increaseYaz();
            this.currentYaz++;
            this.rewindYaz++;
        }

        RequestBody body = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/plain");
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

            }
        };
        HttpClientUtil.runAsyncHead(HttpClientUtil.getYazUrl(), body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    @FXML
    void decreaseYazOnAction(ActionEvent event) {
        if(rewindYaz == 1)
            return;

        rewindYaz--;
        adminAppMainController.decreaseYaz();
        if(rewindYaz == 1)
            decreaseYazBtn.setDisable(true);
        if(this.rewindYaz < this.currentYaz)
            increaseYazBtn.setDisable(false);

        HttpClientUtil.runAsync(HttpClientUtil.createRewindUrl(rewindYaz), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                response.close();
            }
        });
    }

    @FXML
    void rewindCheckBoxOnAction(ActionEvent event) {
        String isRewind;
        if(rewindCheckBox.isSelected()) {
            isRewind = "true";
            this.isRewind = true;
            if(this.rewindYaz == this.currentYaz)
                increaseYazBtn.setDisable(true);

            adminAppMainController.setRewindMessage("REWIND ENGAGED!");
        } else {
            isRewind = "false";
            this.isRewind = false;
            this.rewindYaz = this.currentYaz;
            adminAppMainController.setCurrentYaz(this.currentYaz);
            adminAppMainController.resetRewindMessage();
            HttpClientUtil.runAsync(HttpClientUtil.createRewindUrl(this.currentYaz), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    response.close();
                }
            });
        }

        HttpClientUtil.runAsync(HttpClientUtil.createYazRewindUrl(isRewind), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                response.close();
                decreaseYazBtn.setDisable(!rewindCheckBox.isSelected());
                increaseYazBtn.setDisable(rewindCheckBox.isSelected());
            }
        });
    }

    @FXML
    void initialize() {
        startLoanListRefresher();
        startClientListRefresher();
        loansListView.setCellFactory(lv -> new ListCell<Loan>(){
            @Override
            protected void updateItem(Loan item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    return;
                } else {
                    setText(item.getId());
                    if (item.getStatus().equals(Status.ACTIVE)){
                        setTextFill(Color.GREEN);
                    }
                    else if(item.getStatus().equals(Status.PENDING)){
                        setTextFill(Color.BLUE);
                    }
                    else if(item.getStatus().equals(Status.RISK)){
                        setTextFill(Color.RED);
                    }
                    else if (item.getStatus().equals(Status.NEW)){
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });
    }

    private void startClientListRefresher() {
        clientRefresher = new UserListRefresher(this::updateClientList);
        timer = new Timer();
        timer.schedule(clientRefresher, 2000, 2000);
    }

    private void updateClientList(List<String> clientList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientsListView.getItems().clear();
                for (String s : clientList) {
                    clientsListView.getItems().add(s);
                }
            }
        });
    }


    public void startLoanListRefresher() {
        loanRefresher = new LoanTableRefresher(this::updateLoanList);
        timer = new Timer();
        timer.schedule(loanRefresher, 2000, 2000);
    }

    private void updateLoanList(List<Loan> loans) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loansListView.getItems().clear();
                for (Loan loan : loans) {
                    loansListView.getItems().add(loan);
                }
            }
        });
    }

}
