package admin.components.main;

import admin.components.adminapp.AdminAppController;
import admin.components.login.AdminLoginController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class AdminAppMainController {

    private int currentYaz = 1;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Label currentYazLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label rewindLabel;

    private ScrollPane adminLoginComponent;
    private AdminLoginController adminLoginComponentController;

    private ScrollPane adminAppComponent;
    private AdminAppController adminAppComponentController;

    private String adminName = "ANON";

    @FXML
    public void initialize() {
        welcomeLabel.setText("");
        loadLoginPage();
        loadAdminAppPage();
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource("/admin/components/login/login.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            adminLoginComponent = fxmlLoader.load();
            adminLoginComponentController = fxmlLoader.getController();
            adminLoginComponentController.setAdminAppMainController(this);
            setMainPanelTo(adminLoginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAdminAppPage() {
        URL userAppPageUrl = getClass().getResource("/admin/components/adminapp/AdminAppWindow.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(userAppPageUrl);
            adminAppComponent = fxmlLoader.load();
            adminAppComponentController = fxmlLoader.getController();
            adminAppComponentController.setAdminAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public void switchToAdminApp(String name) {
        adminName = name;
        setMainPanelTo(adminAppComponent);
        // *.setActive();
    }

    public void switchToLogin() {
        Platform.runLater(() -> {
            setMainPanelTo(adminLoginComponent);
        });
    }

    public void increaseYaz(){
        currentYaz++;
        currentYazLabel.setText("Current Yaz: " + currentYaz);
    }

    public void decreaseYaz(){
        currentYaz--;
        currentYazLabel.setText("Current Yaz: " + currentYaz);
    }

    public int getCurrentYaz() {
        return currentYaz;
    }

    public void setCurrentYaz(int currentYaz) {
        this.currentYaz = currentYaz;
        currentYazLabel.setText("Current Yaz: " + currentYaz);
    }

    public void setWelcomeMessage(String value){
        welcomeLabel.setText(value);
    }

    public void setRewindMessage(String value){
        rewindLabel.setText(value);
        rewindLabel.setStyle("-fx-text-fill: WHITE; -fx-background-color: RED;" +
                "-fx-font-style: BOLD; -fx-font-size: 25px");
    }

    public void resetRewindMessage(){
        rewindLabel.setText("");
        /*rewindLabel.setStyle("-fx-text-fill: DEFAULT; -fx-background-color: DEFAULT;" +
                "-fx-font-style: DEFAULT; -fx-font-size: 15px");*/
    }
}
