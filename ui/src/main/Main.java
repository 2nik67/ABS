package main;

import Admin.AdminController;
import AppController.AppController;
import Header.HeaderController;
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
        URL url  = getClass().getResource("/Header/Head.fxml");
        fxmlLoader.setLocation(url);
        Parent header =fxmlLoader.load(url.openStream());
        HeaderController headerController = fxmlLoader.getController();

        //admin body
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/Admin/Admin.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane adminBody = fxmlLoader.load(url.openStream());
        AdminController adminController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/AppController/App.fxml");
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());
        AppController appController = fxmlLoader.getController();

        root.setCenter(adminBody);
        root.setTop(header);

        appController.setBodyAdminController(adminController);
        appController.setHeaderController(headerController);


        Scene scene =new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
