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
import javafx.fxml.FXML;
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
    private Label headerSkinLabel;

    @FXML
    private ContextMenu skinContext;

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

        // initializeSkinContextMenu();
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
        currentYazLabel.setText("Current Yaz: 0");
    }

    /* public void initializeSkinContextMenu(){
        MenuItem string = new MenuItem("Test skin");
        skinContext.getItems().clear();
        skinContext.getItems().add(string);

    }

    public void openSkinContextMenu(){
        skinContext.show(headerSkinLabel, headerSkinLabel.localToScreen(headerSkinLabel.getBoundsInLocal()).getMinX(),
                headerSkinLabel.localToScreen(headerSkinLabel.getBoundsInLocal()).getMaxY());
    }*/
}

