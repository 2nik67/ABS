package appcontroller;

import adminbody.AdminController;
import header.HeaderController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.net.URL;


public class AppController {


    private ScrollPane adminBody;
    private Parent headerBody;
    private BorderPane root;

    private Parent clientBody;

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




    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public void setHeaderController(HeaderController headerController) {
        this.headerController = headerController;
        headerController.setMainController(this);
    }

    public void setBodyAdminController(AdminController bodyAdminController) {
        this.bodyAdminController = bodyAdminController;
        bodyAdminController.setMainController(this);
    }


    public void setAdminBody(ScrollPane adminBody) {
        this.adminBody = adminBody;
    }

    public void setHeaderBody(Parent headerBody) {
        this.headerBody = headerBody;
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




    public void changeScreen(String value) {
        if(value.equals("Admin")){
            root.setCenter(adminBody);
        }
        else {
            root.setCenter(clientBody);
        }
    }
}
