package Header;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import AppController.AppController;
import client.Client;
import client.Clients;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import load.LoadFile;
import time.Yaz;

public class HeaderController{



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
    void chosenValue(ActionEvent event) {
        //TODO: change the screen
        System.out.println(comboBoxUser.getValue());
    }
}

