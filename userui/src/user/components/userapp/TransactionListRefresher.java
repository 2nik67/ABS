package user.components.userapp;

import client.Client;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TransactionListRefresher extends TimerTask {

    private final Consumer<Client> transactionRefresh;
    String client;

    public TransactionListRefresher(Consumer<Client> transactionRefresh, String client) {
        this.transactionRefresh = transactionRefresh;
        this.client = client;
    }


    @Override
    public void run() {
        HttpClientUtil.runAsync(HttpClientUtil.createGetClientUrl(client), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200){
                    return;
                }
                String clientStr = response.body().string();
                if (!clientStr.equals("[]" + System.lineSeparator())){
                    Client client = new Gson().fromJson(clientStr, Client.class);
                    if (client == null){
                        return;
                    }
                    transactionRefresh.accept(client);
                }
            }
        });
    }
}
