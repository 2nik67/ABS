package user.components.loantrade;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import user.components.userapp.UserAppController;

public class LoanTradeController {

    //private UserAppController userAppController;

    @FXML
    private ScrollPane loanTradeTabComponent;

    @FXML
    private ListView<?> sellLoansListView;

    @FXML
    private Button sellLoanBtn;

    @FXML
    private ListView<?> buyLoansListView;

    @FXML
    private Button buyLoanBtn;

    /*public void setUserAppController(UserAppController userAppController) {
        this.userAppController = userAppController;
    }*/
}