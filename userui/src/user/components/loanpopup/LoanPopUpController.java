package user.components.loanpopup;

import client.Client;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import javafx.stage.Stage;

import javafx.util.Pair;
import loan.Loan;
import user.components.userapp.UserAppController;
import user.utils.HttpClientUtil;

import java.net.URL;
import java.util.List;

public class LoanPopUpController {

    private static Loan thisLoan;

    private static UserAppController userAppController;

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
    private Label totalPaidLabel;

    @FXML
    private ListView<String> investorsListView;


    @FXML
    void initialize() {
        fillLoanInfo();
        //loanIDLabel.setText("Loan ID: " + thisLoan.getId());
    }

    public void fillLoanInfo() {
        loanIDLabel.setText("Loan ID: " + thisLoan.getId());
        loanerLabel.setText("Loaner: " + thisLoan.getOwner().getName());
        statusLabel.setText("Status: " + thisLoan.getStatus().name());
        categoryLabel.setText("Category: " + thisLoan.getLoanCategory().getCategory());
        capitalLabel.setText("Capital: " + thisLoan.getLoan());
        totalPaidLabel.setText("Total paid: " + thisLoan.getLoanPaid());
        for (int i = 0; i < thisLoan.getLoaners().size(); i++) {
            String str = thisLoan.getLoaners().get(i).getKey().getName() + " invested " + thisLoan.getLoaners().get(i).getValue();
            investorsListView.getItems().add(str);
        }
    }

    public void popUp() throws Exception{
        try{

            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/user/components/loanpopup/loanPopUp.fxml");
            fxmlLoader.setLocation(url);
            ScrollPane root = fxmlLoader.load(url.openStream());
            Scene scene =new Scene(root, 1000 , 600);
            newStage.setResizable(true);
            newStage.setScene(scene);
            newStage.show();

        }catch (Exception e){
            System.out.println(e);
        }


        //newStage.getIcons().add(new Image("resources/coins.png"));

       /* if(!style.isEmpty())
            scene.getStylesheets().add(style.get(0));*/
    }

    public void setLoan(Loan loan) {
        this.thisLoan = loan;
    }
}
