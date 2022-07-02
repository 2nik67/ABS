package admin.components.login;

import admin.utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import admin.components.main.AdminAppMainController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AdminLoginController {

    private AdminAppMainController adminAppMainController;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField adminNameTextField;

    @FXML
    private Label label;

    private static final String LOGIN_URL = "http://localhost:8080/web_Web/servlets/AdminLoginServlet";

    @FXML
    void loginBtnOnAction(ActionEvent event) {
        String userName= adminNameTextField.getText();
        if(userName.isEmpty()){
            return;
        }
        String finalUrl = HttpUrl.parse((LOGIN_URL))
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

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
                        if(responseBody.contains("WELCOME")) {
                            adminAppMainController.switchToAdminApp(adminNameTextField.getText());
                        }
                        else {
                            label.setText(responseBody);
                        }
                    });
                }

            }
        });

    }

    public void setAdminAppMainController(AdminAppMainController adminAppMainController){
        this.adminAppMainController = adminAppMainController;
    }
}
