package user.components.possibleloans;

import client.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import loan.Loan;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.controlsfx.control.CheckListView;
import org.jetbrains.annotations.NotNull;
import user.components.scrambletab.ScrambleTabController;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PossibleLoansController {
//
    @FXML
    private ScrollPane possibleLoansComponent;

    private static double sumToInvest;

    private static ScrambleTabController scrambleTabController;

    private Client currentClient;

    @FXML
    private CheckListView<String> loansCheckList;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button okBtn;

    private static List<Loan> possibleLoanList;

    @FXML
    public void initialize(){
        initializeLoanCheckList();
    }

    @FXML
    private ProgressIndicator scrambleProgressIndicator;

    @FXML
    void okBtnOnAction(ActionEvent event) {
        List<String> checkedLoans = getCheckedLoans();
        String bodyRequest = HttpClientUtil.createLoansForBody(checkedLoans);
        RequestBody requestBody = RequestBody.create(bodyRequest.getBytes());
        HttpClientUtil.runAsyncPost(HttpClientUtil.createInvestmentUrl(sumToInvest, scrambleTabController.getChosenClient()), requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                advanceProgressBarAndClose(response.body().string());


            }
        });

    }

    private void advanceProgressBarAndClose(String resp){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (double i = 0; i < 6; i++) {
                    scrambleProgressIndicator.setProgress(i/10);
                    Thread.sleep(100);
                }

                for (double i = 6; i <= 10; i++) {
                    scrambleProgressIndicator.setProgress(i/10);
                    Thread.sleep(100);
                }
                Thread.sleep(500);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (Double.parseDouble(resp) != 0){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Attention!");
                            alert.setContentText("Could only invest" + resp);
                            alert.showAndWait();
                            Stage stage = (Stage) cancelBtn.getScene().getWindow();
                            stage.close();
                        }else{
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Money was invested");
                            alert.setContentText("Money was invested");
                            alert.showAndWait();
                            Stage stage = (Stage) cancelBtn.getScene().getWindow();
                            stage.close();
                        }

                    }
                });
                return null;
            }
        };
        Thread t= new Thread(task);

        t.start();
    }

    private List<String> getCheckedLoans() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < loansCheckList.getItems().size(); i++) {
            for (Loan loan : possibleLoanList) {
                if (loansCheckList.getCheckModel().isChecked(i) && loan.getId().equals(loansCheckList.getCheckModel().getItem(i))) {
                    res.add(loan.getId());
                }
            }

        }
        return res;
    }

    public void popUp() throws Exception{
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/user/components/possibleloans/PossibleLoans.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        Scene scene =new Scene(root, 600 , 600);
        newStage.setScene(scene);
        newStage.show();

        //newStage.getIcons().add(new Image("resources/coins.png"));
    }

    private void initializeLoanCheckList(){

        ObservableList<String> strings = FXCollections.observableArrayList();
        for (Loan loan : possibleLoanList) {
            strings.add(loan.getId());
        }

        loansCheckList.setItems(strings);
    }

    @FXML
    void cancelBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public static void setScrambleTabController(ScrambleTabController scrambleTabController) {
        PossibleLoansController.scrambleTabController = scrambleTabController;
    }

    public static void setPossibleLoanList(List<Loan> possibleLoanList) {
        PossibleLoansController.possibleLoanList = possibleLoanList;
    }

    public static void setSumToInvest(double sumToInvest) {
        PossibleLoansController.sumToInvest = sumToInvest;
    }
}
