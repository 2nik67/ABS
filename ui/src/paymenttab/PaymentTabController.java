package paymenttab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;

public class PaymentTabController {

    @FXML
    private TreeView<String> loansTreeView;

    @FXML
    private Button closeLoanBtn;

    @FXML
    private Button autoPayBtn;

    @FXML
    private ListView<String> loansListView;

    @FXML
    void autoPayOnAction(ActionEvent event) {

    }

    @FXML
    void closeLoanOnAction(ActionEvent event) {

    }
    @FXML
    void initialize() {
        loansListView.getItems().add("Hello");
        loansListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                autoPayBtn.setDisable(false);
                closeLoanBtn.setDisable(false);
            }
        });

    }
}