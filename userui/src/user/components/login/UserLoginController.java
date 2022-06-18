package user.components.login;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

public class UserLoginController extends Application {

    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginBtn;

    @FXML
    private Label label;

    private static final String LOGIN_URL = "http://localhost:8080/web_Web/servlets/UserLogin";

    @FXML
    void loginBtnOnAction(ActionEvent event) throws IOException {



        String userName= userNameTextField.getText();
        if(userName.isEmpty()){
            return;
        }
        String finalUrl = HttpUrl.parse((LOGIN_URL))
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();
        //updatehttpline

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        label.setText("Something went wrong " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200){
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            label.setText("Something went wrong" + responseBody)
                    );
                }else{
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            label.setText(responseBody)
                    );
                }

            }
        });


    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/user/components/login/login.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane root = fxmlLoader.load(url.openStream());
        Scene scene =new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}