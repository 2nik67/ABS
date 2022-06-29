package user.components.depositwithdraw;

import client.Client;
import client.Clients;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.components.userapp.UserAppController;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.function.UnaryOperator;

public class DepositWithdrawController {

    private static UserAppController userAppController;
    private Client currentClient;
//
    @FXML
    private Button cancelBtn;

    @FXML
    private Button okBtn;

    @FXML
    private TextField moneyTextField;

    @FXML
    private ChoiceBox<String> withdrawDepositChoiceBox;

    private final static String getClientFinalUrl = "http://localhost:8080/web_Web/servlets/GetUserByName";



    @FXML
    void cancelBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void okBtnOnAction(ActionEvent event) {
        String url;
        if(moneyTextField.getText().equals("Deposit")){
            url = HttpClientUtil.createLoadMoneyUrl(true, moneyTextField.getText(), userAppController.getChosenClient());
        }else{
            url = HttpClientUtil.createLoadMoneyUrl(false, moneyTextField.getText(), userAppController.getChosenClient());
        }
        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });

        userAppController.infoTabSelected();
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();


    }

    @FXML
    void initialize() {
        withdrawDepositChoiceBox.getItems().add("Withdraw");
        withdrawDepositChoiceBox.getItems().add("Deposit");
        currentClient= Clients.getClientByName(userAppController.getChosenClient());

        withdrawDepositChoiceBox.getSelectionModel().selectFirst();
        initializeMoneyTextField();
    }

    public void popUp(String name, ObservableList<String> style) throws Exception{
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/user/components/depositwithdraw/depositAndWithdraw.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane root = fxmlLoader.load(url.openStream());
        Scene scene =new Scene(root, 400 , 200);
        newStage.setResizable(false);
        newStage.setScene(scene);
        newStage.show();

        //newStage.getIcons().add(new Image("resources/coins.png"));

        if(!style.isEmpty())
            scene.getStylesheets().add(style.get(0));
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public static void setUserAppController(UserAppController userAppController) {
        DepositWithdrawController.userAppController = userAppController;
    }


    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([0-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    private void initializeMoneyTextField(){
        moneyTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));
    }


}
