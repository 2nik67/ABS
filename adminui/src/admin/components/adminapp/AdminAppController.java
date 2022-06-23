package admin.components.adminapp;

import admin.components.main.AdminAppMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminAppController {

    private AdminAppMainController adminAppMainController;

    @FXML
    private ScrollPane scrollPaneAdmin;

    @FXML
    private TreeView<?> loansTreeView;

    @FXML
    private ListView<?> clientsListView;

    @FXML
    private Button increaseYazBtn;

    @FXML
    private Button decreaseYaxBtn;

    @FXML
    private CheckBox rewindCheckBox;

    public void setAdminAppMainController(AdminAppMainController adminAppMainController){
        this.adminAppMainController = adminAppMainController;
    }

    @FXML
    void IncreaseYaz(ActionEvent event) {

    }
}
