package user.components.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.components.main.UserAppMainController;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

public class UserLoginController {

    private UserAppMainController userAppMainController;

    private String currentClient;

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


        this.currentClient = userName;

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
                    Platform.runLater(() -> {
                        label.setText("Something went wrong" + responseBody);
                        System.out.println(responseBody);
                    });
                }
                else{
                    String responseBody = response.body().string();

                   Platform.runLater(() -> {
                       if(responseBody.contains("NEW")) {
                           userAppMainController.switchToUserApp(userNameTextField.getText());
                       }
                       else {
                           label.setText("Client already signed in!");
                       }
                    });
                }

            }
        });


    }

    public static void main(String[] args) {
    }

    public void setUserAppMainController(UserAppMainController userAppMainController)
    {
        this.userAppMainController = userAppMainController;
    }

    public String getCurrentClient() {
        return currentClient;
    }
}