package admin.components.adminapp;

import admin.components.main.AdminAppMainController;
import admin.utils.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import time.Yaz;

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
    private Button decreaseYaxBtn;

    @FXML
    private CheckBox rewindCheckBox;

    public void setAdminAppMainController(AdminAppMainController adminAppMainController){
        this.adminAppMainController = adminAppMainController;
    }

    @FXML
    void IncreaseYaz(ActionEvent event) {
        HttpClientUtil.runAsyncHead(HttpClientUtil.getYazUrl(), null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}
