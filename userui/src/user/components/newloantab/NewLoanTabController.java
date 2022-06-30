package user.components.newloantab;

import client.Client;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import load.LoadFile;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.components.scrambletab.CategoryCheckListRefresher;
import user.components.userapp.UserAppController;
import user.utils.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

public class NewLoanTabController {


    private TimerTask categoryComboBoxRefresh;
    private Timer timer;

    private Client client;
    private UserAppController userAppController;

    @FXML
    private TextField loanIDTextField;

    @FXML
    private ContextMenu loanIDContext;

    @FXML
    private ScrollPane newLoanTabComponent;

    @FXML
    private TextField capitalTextField;

    @FXML
    private TextField totalYAZTextField;

    @FXML
    private TextField yazIntervalTextField;

    @FXML
    private ContextMenu yazIntervalContext;

    @FXML
    private TextField interestTextField;

    @FXML
    private Button createLoanButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Button loadFileBtn;

    @FXML
    void createLoanBtnOnAction(ActionEvent event) {
        if(loanIDTextField.getText().equals("")){
            MenuItem string = new MenuItem("Please enter an id for your loan, it can be anything.");
            loanIDContext.getItems().clear();
            loanIDContext.getItems().add(string);

            loanIDContext.show(newLoanTabComponent,loanIDTextField.localToScreen(loanIDTextField.getBoundsInLocal()).getMinX(),
                    loanIDTextField.localToScreen(loanIDTextField.getBoundsInLocal()).getMaxY());

            return;
        }

        if(capitalTextField.getText().equals(""))
            capitalTextField.setText("0");
        if(interestTextField.getText().equals(""))
            interestTextField.setText("0");
        if(totalYAZTextField.getText().equals(""))
            totalYAZTextField.setText("0");
        if(yazIntervalTextField.getText().equals(""))
            yazIntervalTextField.setText("0");

        if(Loans.getLoanByID(loanIDTextField.getText()) != null){
            MenuItem string = new MenuItem("This loan id is taken, please choose another one.");
            loanIDContext.getItems().clear();
            loanIDContext.getItems().add(string);

            loanIDContext.show(newLoanTabComponent,loanIDTextField.localToScreen(loanIDTextField.getBoundsInLocal()).getMinX(),
                    loanIDTextField.localToScreen(loanIDTextField.getBoundsInLocal()).getMaxY());

            return;
        }

        if((Double.parseDouble(totalYAZTextField.getText()))%(Double.parseDouble(yazIntervalTextField.getText())) != 0){
            MenuItem string = new MenuItem("Yaz interval can't be bigger than total Yaz, and it needs to divide total Yaz.");
            yazIntervalContext.getItems().clear();
            yazIntervalContext.getItems().add(string);

            yazIntervalContext.show(newLoanTabComponent,yazIntervalTextField.localToScreen(yazIntervalTextField.getBoundsInLocal()).getMinX(),
                    yazIntervalTextField.localToScreen(yazIntervalTextField.getBoundsInLocal()).getMaxY());

            return;
        }

        /*Double capital = Double.parseDouble(capitalTextField.getText()),
                totalYaz = Double.parseDouble(totalYAZTextField.getText()),
                yazInterval = Double.parseDouble(yazIntervalTextField.getText()),
                interest = Double.parseDouble(interestTextField.getText());*/

        String capital = capitalTextField.getText(),
                totalYaz = totalYAZTextField.getText(),
                yazInterval = yazIntervalTextField.getText(),
                interest = interestTextField.getText(),
                category = categoryComboBox.getValue(),
                loanID = loanIDTextField.getText();

        String url = HttpClientUtil.createNewLoanUrl(userAppController.getChosenClient(), loanID,
                capital, totalYaz, yazInterval, interest, category);
        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("Loan added!");
                Loans.printLoans();
            }
        });
    }

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
                    System.out.println(response.body().string());
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
    public void initialize() { initializeTextFields();
        startComboBoxRefresh();
        initializeComboBox();

    }

    private void startComboBoxRefresh() {
        categoryComboBoxRefresh = new CategoryCheckListRefresher(this::refreshCategoryComboBox);
        timer = new Timer();
        timer.schedule(categoryComboBoxRefresh, 2000, 2000);
    }

    private void refreshCategoryComboBox(List<Category> categories) {
        if(categories.size() == categoryComboBox.getItems().size()){
            return;
        }
        categoryComboBox.getItems().clear(); //TODO: is the combobox here null?
        for (Category category : categories) {
            categoryComboBox.getItems().add(category.getCategory());
        }

    }

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

    private void initializeComboBox(){
        categoryComboBox.getItems().clear();
        Categories.addCategory(new Category("Default"));

        for(Category cat : Categories.getCategoryList()){
            categoryComboBox.getItems().add(cat.getCategory());
        }
    }

}
