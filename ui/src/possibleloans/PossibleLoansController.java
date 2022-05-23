package possibleloans;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;

import java.net.URL;

public class PossibleLoansController {


    private Client currentClient;

    @FXML
    private CheckListView<?> possibleLoansCheckListView;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button okBtn;

    public void popUp() throws Exception{
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/possibleloans/PossibleLoans.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        Scene scene =new Scene(root, 600 , 600);
        newStage.setScene(scene);
        newStage.show();
    }
    @FXML
    void cancelBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }
}
