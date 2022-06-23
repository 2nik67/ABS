package user.components.newloantab;

import client.Client;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import load.LoadFile;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.components.userapp.UserAppController;
import user.utils.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class NewLoanTabController {

    private Client client;
    private UserAppController userAppController;

    @FXML
    private TextField capitalTextField;

    @FXML
    private TextField totalYAZTextField;

    @FXML
    private TextField yazIntervalTextField;

    @FXML
    private TextField interestTextField;

    @FXML
    private Button createLoanButton;

    @FXML
    private ComboBox<?> categoryComboBox;

    @FXML
    private Button loadFileBtn;

    @FXML
    void loadFileOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            String url = HttpClientUtil.createLoadLoanUrl(userAppController.getChosenClient(),selectedFile.getPath().toString());
            HttpClientUtil.runAsyncPost(url, createBodyRequestForLoadFile(selectedFile.getPath()), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    System.out.println(response.body().string());
                }
            });
        }
    }

    private RequestBody createBodyRequestForLoadFile(String path) {
        return new FormBody.Builder().add("Path", path).build();
    }

    @FXML
    public void initialize() { initializeTextFields(); }

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([0-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    private void initializeTextFields(){
        capitalTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        interestTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        totalYAZTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        yazIntervalTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));
    }

    public void setUserAppController(UserAppController userAppController) {
        this.userAppController = userAppController;
    }

}
