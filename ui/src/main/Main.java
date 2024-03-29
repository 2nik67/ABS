package main;

import adminbody.AdminController;
import appcontroller.AppController;
import header.HeaderController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
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
        AppController appController= new AppController();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/appcontroller/App.fxml");
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());
        Scene scene =new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add("resources/DefaultStyle.css");
        primaryStage.getIcons().add(new Image("resources/coins.png"));
    }
}
