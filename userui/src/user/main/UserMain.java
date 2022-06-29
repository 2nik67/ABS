package user.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.components.main.UserAppMainController;

import java.io.IOException;
import java.net.URL;

public class UserMain extends Application {

    private UserAppMainController userAppMainController;
//
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("ABS Customer Client");
        URL loginPage = getClass().getResource("/user/components/main/UserAppMain.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            userAppMainController = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
