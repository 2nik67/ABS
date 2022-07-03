package user.components.main;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import time.Yaz;
import user.components.login.UserLoginController;
import user.components.userapp.UserAppController;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserAppMainController {

    private TimerTask yazRefresher;
    private Timer timer;

    @FXML
    private MenuBar headerMenuBar;

    @FXML
    private Menu skinMenu;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Label currentYazLabel;

    private String clientsName;

    private AnchorPane userLoginComponent;
    private UserLoginController userLoginComponentController;

    private TabPane userAppComponent;
    private UserAppController userAppComponentController;


    @FXML
    public void initialize() {
        loadLoginPage();
        loadUserAppPage();
        startYazRefresher();
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource("/user/components/login/login.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            userLoginComponent = fxmlLoader.load();
            userLoginComponentController = fxmlLoader.getController();
            userLoginComponentController.setUserAppMainController(this);
            setMainPanelTo(userLoginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserAppPage() {
        URL userAppPageUrl = getClass().getResource("/user/components/userapp/UserAppWindow.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(userAppPageUrl);
            userAppComponent = fxmlLoader.load();
            userAppComponentController = fxmlLoader.getController();
            userAppComponentController.setUserAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public String getClientsName() {
        return clientsName;
    }

    public void startYazRefresher() {
        yazRefresher = new YazRefresher(
                this::updateYazLabel);
        timer = new Timer();
        timer.schedule(yazRefresher, 2000, 2000);
    }

    private void updateYazLabel(String currentYaz, String isRewind) {
        Platform.runLater(() -> {
            currentYazLabel.setText("Current yaz: " + currentYaz);
            if(isRewind.equals("true"))
                setRewind(true);
            else if(isRewind.equals("false"))
                setRewind(false);
        });
    }

    private void setRewind(boolean isRewind){
        userAppComponentController.setRewind(isRewind);
    }

    public String getChosenClient(){
        return userLoginComponentController.getCurrentClient();
    }

    public void switchToUserApp(String name) {
        clientsName = name;
        userAppComponentController.setCurrentClient(name);
        setMainPanelTo(userAppComponent);

        // *.setActive();
    }

    public void switchToLogin() {
        Platform.runLater(() -> {
            //currentUserName.set(JHON_DOE);
            //userAppComponentController.setInActive();
            setMainPanelTo(userLoginComponent);
        });
    }

    @FXML
    void initializeSkinContextMenu(ActionEvent event) {

    }
}
