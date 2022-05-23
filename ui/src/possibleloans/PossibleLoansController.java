package possibleloans;

import Investment.Investment;
import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loan.Loan;
import loan.Loans;
import loan.category.Categories;
import loan.category.Category;
import org.controlsfx.control.CheckListView;
import scrambletab.ScrambleTabController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PossibleLoansController {

    @FXML
    private ScrollPane possibleLoansComponent;

    private static double sumToInvest;

    private static ScrambleTabController scrambleTabController;

    private Client currentClient;

    @FXML
    private CheckListView<String> loansCheckList;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button okBtn;

    private static List<Loan> possibleLoanList;

    @FXML
    public void initialize(){
        initializeLoanCheckList();
    }

    @FXML
    void okBtnOnAction(ActionEvent event) {

        List<Loan> chosenLoans = new ArrayList<>();
        List<Loan> loans = Loans.getLoans();
        for (int i = 0; i < loans.size(); i++) {
            if(loansCheckList.getCheckModel().isChecked(i)){
                chosenLoans.add(loans.get(i));
            }
        }


        Investment.investmentAssigning(chosenLoans, sumToInvest);

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }

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

    private void initializeLoanCheckList(){

        ObservableList<String> strings = FXCollections.observableArrayList();
        for (Loan loan : possibleLoanList) {
            strings.add(loan.getId());
        }

        loansCheckList.setItems(strings);
    }

    @FXML
    void cancelBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public static void setScrambleTabController(ScrambleTabController scrambleTabController) {
        PossibleLoansController.scrambleTabController = scrambleTabController;
    }

    public static void setPossibleLoanList(List<Loan> possibleLoanList) {
        PossibleLoansController.possibleLoanList = possibleLoanList;
    }

    public static void setSumToInvest(double sumToInvest) {
        PossibleLoansController.sumToInvest = sumToInvest;
    }
}
