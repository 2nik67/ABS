package admin.components.adminapp;

import admin.components.main.AdminAppMainController;
import admin.utils.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

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
    private Button decreaseYazBtn;

    @FXML
    private CheckBox rewindCheckBox;

    public void setAdminAppMainController(AdminAppMainController adminAppMainController){
        this.adminAppMainController = adminAppMainController;
    }

    @FXML
    void IncreaseYaz(ActionEvent event) {
        adminAppMainController.increaseYaz();

        RequestBody body = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/plain");
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

            }
        };
        HttpClientUtil.runAsyncHead(HttpClientUtil.getYazUrl(), body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    @FXML
    void rewindCheckBoxOnAction(ActionEvent event) {
        String isRewind;
        if(rewindCheckBox.isSelected())
            isRewind = "true";
        else
            isRewind = "false";

        HttpClientUtil.runAsync(HttpClientUtil.createYazRewindUrl(isRewind), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                response.close();
                decreaseYazBtn.setDisable(!rewindCheckBox.isSelected());
            }
        });
    }
}
