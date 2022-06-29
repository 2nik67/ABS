package user.components.loanpopup;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.stage.Stage;
import loan.Loan;
import user.utils.HttpClientUtil;

import java.net.URL;

public class LoanPopUpController {

    private Loan thisLoan;

    @FXML
    private Label loanIDLabel;

    @FXML
    private Label loanerLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label capitalLabel;

    @FXML
    private Label investorsLabel;

    @FXML
    private Label totalPaidLabel;

    @FXML
    void initialize() {
        //fillLoanInfo();
        //loanIDLabel.setText("Loan ID: " + thisLoan.getId());
    }

    public void fillLoanInfo(Loan thisLoan) {
        loanIDLabel.setText("Loan ID: " + thisLoan.getId());
        loanerLabel.setText("Loaner: " + thisLoan.getOwner().getName());
        statusLabel.setText("Status: " + thisLoan.getStatus().name());
        categoryLabel.setText("Category: " + thisLoan.getLoanCategory());
        capitalLabel.setText("Capital: " + thisLoan.getLoan());
    }

    public void popUp(Loan loan) throws Exception{
        this.thisLoan = loan;
        try{

            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/user/components/loanpopup/loanPopUp.fxml");
            fxmlLoader.setLocation(url);
            ScrollPane root = fxmlLoader.load(url.openStream());
            Scene scene =new Scene(root, 400 , 200);
            newStage.setResizable(false);
            newStage.setScene(scene);
            newStage.show();

        }catch (Exception e){
            System.out.println(e);
        }


        //newStage.getIcons().add(new Image("resources/coins.png"));

       /* if(!style.isEmpty())
            scene.getStylesheets().add(style.get(0));*/
    }
}