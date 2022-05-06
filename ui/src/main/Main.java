package main;

import adminbody.AdminController;
import appcontroller.AppController;
import header.HeaderController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);

       /* while(!Menu.isExit()){
            Menu.printMenu();
            Menu.getUsersInput();
            Menu.methodToCall();
        }*/
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Header
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url  = getClass().getResource("/header/Head.fxml");
        fxmlLoader.setLocation(url);
        Parent header =fxmlLoader.load(url.openStream());
        HeaderController headerController = fxmlLoader.getController();

        //admin body
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/adminbody/Admin.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane adminBody = fxmlLoader.load(url.openStream());
        AdminController adminController = fxmlLoader.getController();





        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/appcontroller/App.fxml");
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());
        AppController appController = fxmlLoader.getController();

        root.setCenter(adminBody);
        root.setTop(header);



        appController.setBodyAdminController(adminController);
        appController.setHeaderController(headerController);
        appController.setRoot(root);
        appController.setHeaderBody(header);
        appController.setAdminBody(adminBody);

        Scene scene =new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
