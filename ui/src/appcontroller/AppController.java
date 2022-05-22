package appcontroller;

import adminbody.AdminController;
import clientbody.ClientController;
import header.HeaderController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.net.URL;


public class AppController {


    @FXML
    private BorderPane root;

    @FXML private TabPane clientBodyComponent;

    @FXML private ClientController clientBodyComponentController;

    @FXML
    private ScrollPane headerComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private ScrollPane adminBodyComponent;

    @FXML
    private AdminController adminBodyComponentController;


    @FXML
    public void initialize(){
        if (headerComponentController != null && adminBodyComponentController != null && clientBodyComponentController != null){
            headerComponentController.setMainController(this);
            adminBodyComponentController.setMainController(this);
            clientBodyComponentController.setMainController(this);
        }
        root.setLeft(null);
    }

    public void updateYazLabel(){
        headerComponentController.updateYazLabel();
    }

    public void updatePathLabel(){
        headerComponentController.updatePathLabel();
    }

    public void updateComboBox(){
        headerComponentController.updateComboBox();
    }

    public void changeScreen(String value) {
        if(value.equals("Admin")){
            root.setCenter(adminBodyComponent);
        }
        else {
            root.setCenter(clientBodyComponent);
        }
    }

    public void refreshCategoriesInScramble(){
        clientBodyComponentController.refreshCategoriesInScramble();
    }

    public void setCurrentClient(String currentClient) {
        clientBodyComponentController.setCurrentClient(currentClient);
    }
}
