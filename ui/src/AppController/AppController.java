package AppController;

import Admin.AdminController;
import Header.HeaderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AppController {
    @FXML
    private ScrollPane headerComponent;

    @FXML
    private HeaderController headerController;

    @FXML
    private ScrollPane adminBodyComponent;

    @FXML
    private AdminController bodyAdminController;

    @FXML
    public void initialize(){
        if (headerController != null && adminBodyComponent != null){
            headerController.setMainController(this);
            bodyAdminController.setMainController(this);
        }
    }

    public void setHeaderController(HeaderController headerController) {
        this.headerController = headerController;
        headerController.setMainController(this);
    }

    public void setBodyAdminController(AdminController bodyAdminController) {
        this.bodyAdminController = bodyAdminController;
        bodyAdminController.setMainController(this);
    }

    public void updateYazLabel(){
        headerController.updateYazLabel();
    }

    public void updatePathLabel(){
        headerController.updatePathLabel();
    }

    public void updateComboBox(){
        headerController.updateComboBox();
    }
}
