package depositwithdraw;

import adminbody.AdminController;
import client.Client;
import client.Clients;
import clientbody.ClientController;
import header.HeaderController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.function.UnaryOperator;

public class DepositWithdrawController {
//
    private static ClientController clientController;
    private Client currentClient;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button okBtn;

    @FXML
    private TextField moneyTextField;

    @FXML
    private ChoiceBox<String> withdrawDepositChoiceBox;

    @FXML
    void cancelBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void okBtnOnAction(ActionEvent event) {
        if (withdrawDepositChoiceBox.getValue().equals("Withdraw")){
            currentClient.loadMoney(Double.parseDouble(moneyTextField.getText()) * -1, currentClient.getMoney());
        }
        else {
            currentClient.loadMoney(Double.parseDouble(moneyTextField.getText()), currentClient.getMoney());
        }
        clientController.infoTabSelected();

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();


    }

    @FXML
    void initialize() {
        withdrawDepositChoiceBox.getItems().add("Withdraw");
        withdrawDepositChoiceBox.getItems().add("Deposit");
        currentClient= Clients.getClientByName(HeaderController.getChosenClient());

        withdrawDepositChoiceBox.getSelectionModel().selectFirst();
        initializeMoneyTextField();
    }

    public void popUp(String name, ObservableList<String> style) throws Exception{
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/depositwithdraw/depositAndWithdraw.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane root = fxmlLoader.load(url.openStream());
        Scene scene =new Scene(root, 400 , 200);
        newStage.setResizable(false);
        newStage.setScene(scene);
        newStage.show();

        newStage.getIcons().add(new Image("resources/coins.png"));

        if(!style.isEmpty())
            scene.getStylesheets().add(style.get(0));
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public static void setClientController(ClientController clientController) {
        DepositWithdrawController.clientController = clientController;
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
