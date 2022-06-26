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
import loan.Loans;
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
            String url = HttpClientUtil.createLoadLoanUrl(userAppController.getChosenClient());
            HttpClientUtil.runAsyncPost(url, createBodyRequestForLoadFile(selectedFile), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    System.out.println("responded" + response.body().string());
                    System.out.println(Loans.getLoans().isEmpty());
                }
            });
        }
    }

    private RequestBody createBodyRequestForLoadFile(File f) {
        return  new MultipartBody.Builder()
                .addFormDataPart("file1", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
                .build();
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
