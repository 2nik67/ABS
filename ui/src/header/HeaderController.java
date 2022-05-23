package header;

import java.util.ArrayList;
import java.util.List;

import appcontroller.AppController;
import client.Client;
import client.Clients;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import load.LoadFile;
import time.Yaz;

public class HeaderController{

    private static String chosenClient;
    private  AppController mainController;

    @FXML
    private ComboBox<String> comboBoxUser;

    @FXML
    private Label pathLabel;

    @FXML
    private Label currentYazLabel;


    @FXML
    void initialize() {
        comboBoxUser.getItems().add("Admin");
        comboBoxUser.getSelectionModel().selectFirst();

    }
    public void updateYazLabel(){
        currentYazLabel.setText("Current YAZ: " + Yaz.getYaz());
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void updatePathLabel(){
        pathLabel.setText(LoadFile.getPath());
    }

    public void updateComboBox(){
        List<Client> clients = new ArrayList<>(Clients.getClientsList());
        for (Client client : clients) {
            comboBoxUser.getItems().add(client.getName());
        }
    }

    @FXML
    public void chosenValue(ActionEvent event) {
        String value = comboBoxUser.getValue();
        chosenClient = value;
        if(!value.equals("Admin")){
            mainController.changeScreen(value);
            mainController.setCurrentClient(value);
            mainController.createLoanTreeForClient(value);
            mainController.createInvestmentTreeForClient(value);

        }

    }

    public static String getChosenClient() {
        return chosenClient;
    }


}

