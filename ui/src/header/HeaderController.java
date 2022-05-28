package header;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import appcontroller.AppController;
import client.Client;
import client.Clients;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;
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
    private Tooltip pathToolTip;

    @FXML
    private Label currentYazLabel;

    @FXML
    private MenuBar headerMenuBar;

    @FXML
    private Menu skinMenu;


    @FXML
    void initialize() {
        comboBoxUser.getItems().add("Admin");
        comboBoxUser.getSelectionModel().selectFirst();

        hackTooltipStartTiming(pathToolTip);
        pathToolTip.setText("After you load a file, its path will appear here!");

        skinMenu.getItems().add(new MenuItem());
        skinMenu.setOnAction(e -> initializeSkinContextMenu());

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

        if(value != null){
            chosenClient = value;
            if(!value.equals("Admin")){
                mainController.changeScreen(value);
                mainController.setCurrentClient(value);
                mainController.createLoanTreeForClient(value);
                mainController.createInvestmentTreeForClient(value);
                mainController.createLoanTreeForClientForPaymentTab();
                mainController.createCategoryList();
            }
            mainController.changeScreen(value);
        }

    }

    public static String getChosenClient() {
        return chosenClient;
    }

    public void initializePathToolTip(){
        pathToolTip.setText(LoadFile.getPath());
    }

    public static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUI() {
        comboBoxUser.getItems().clear();
        comboBoxUser.getItems().add("Admin");
        comboBoxUser.getSelectionModel().selectFirst();
        currentYazLabel.setText("Current YAZ: 0");
    }

    public void initializeSkinContextMenu(){
        if(skinMenu.getItems().size() > 1)
            return;

        Scene thisScene = headerMenuBar.getScene();
        skinMenu.getItems().clear();

        MenuItem defSkin = new MenuItem("Default");
        defSkin.setOnAction(e -> { thisScene.getStylesheets().clear(); thisScene.getStylesheets().add("resources/DefaultStyle.css");});
        skinMenu.getItems().add(defSkin);

        MenuItem sakuraSkin = new MenuItem("Sakura");
        sakuraSkin.setOnAction(e -> {thisScene.getStylesheets().clear(); thisScene.getStylesheets().add("resources/SakuraStyle.css");});
        skinMenu.getItems().add(sakuraSkin);

        MenuItem basicSkin = new MenuItem("BasicFX");
        basicSkin.setOnAction(e -> thisScene.getStylesheets().clear());
        skinMenu.getItems().add(basicSkin);
    }
}

