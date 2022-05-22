package clientbody;

import appcontroller.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeView;
import scrambletab.ScrambleTabController;


public class ClientController {


    @FXML
    private ScrollPane scrambleTabComponent;

    @FXML
    private ScrambleTabController scrambleTabComponentController;

    @FXML
    private AppController mainController;

    @FXML
    private Tab informationTab;

    @FXML
    private TreeView<?> loansTreeView;

    @FXML
    private TreeView<?> investmentsTreeView;

    @FXML
    private Button depositBtn;

    @FXML
    private Button withdrawBtn;

    @FXML
    private Tab scrambleTab;

    @FXML
    private Tab paymentTab;

    @FXML
    public void initialize(){
        if (scrambleTabComponentController != null) {
            System.out.println("Banana");
            scrambleTabComponentController.setClientController(this);
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void refreshCategoriesInScramble(){
        scrambleTabComponentController.initializeCategoryCheckList();
    }

    public void setCurrentClient(String currentClient) {
        scrambleTabComponentController.setCurrentClient(currentClient);
    }
}
