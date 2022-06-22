package user.components.newloantab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import user.components.userapp.UserAppController;

public class NewLoanTabController {

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

    public void setUserAppController(UserAppController userAppController) {
        this.userAppController = userAppController;
    }

}
