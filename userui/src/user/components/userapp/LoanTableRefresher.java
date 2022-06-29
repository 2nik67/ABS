package user.components.userapp;

import com.google.gson.Gson;
import loan.Loan;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class LoanTableRefresher extends TimerTask {

    private final Consumer<List<Loan>> loanOfClients;

    public LoanTableRefresher(Consumer<List<Loan>> loanConsumer) {
        this.loanOfClients = loanConsumer;
    }


    @Override
    public void run() {
        HttpClientUtil.runAsync(HttpClientUtil.createLoanListUrl(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
//
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200){
                    return;
                }
                String jsonArrayOfUsersNames = response.body().string();
                if (!jsonArrayOfUsersNames.equals("[]" + System.lineSeparator())){
                    Loan[] loans = new Gson().fromJson(jsonArrayOfUsersNames, Loan[].class);
                    if (loans == null){
                        return;
                    }
                    try{
                        loanOfClients.accept(Arrays.asList(loans));
                    }catch (Exception e){

                    }

                }

            }
        });
    }
}
