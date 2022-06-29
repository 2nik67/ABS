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
    private MenuBar headerMenuBar;

    @FXML
    private Menu skinMenu;

    @FXML
    private Label currentYazLabel;

    private AnchorPane adminLoginComponent;
    private AdminLoginController adminLoginComponentController;

    private ScrollPane adminAppComponent;
    private AdminAppController adminAppComponentController;

    private String adminName = "ANON";

    @FXML
    public void initialize() {
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
            //currentUserName.set(JHON_DOE);
            //userAppComponentController.setInActive();
            setMainPanelTo(adminLoginComponent);
        });
    }

    public void increaseYaz(){
        currentYaz++;
        currentYazLabel.setText("Current Yaz: " + currentYaz);
    }

    @FXML
    void initializeSkinContextMenu(ActionEvent event) {

    }
}
