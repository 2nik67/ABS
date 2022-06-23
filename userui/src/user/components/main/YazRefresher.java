package user.components.main;

import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class YazRefresher extends TimerTask {
    private final Consumer<String> yazConsumer;

    public YazRefresher(Consumer<String> usersListConsumer) {
        this.yazConsumer = usersListConsumer;
    }


    @Override
    public void run() {
        HttpClientUtil.runAsync(HttpClientUtil.getYazUrl(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                yazConsumer.accept(response.body().string());
            }
        });
    }
}
