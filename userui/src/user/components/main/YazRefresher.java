package user.components.main;

import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class YazRefresher extends TimerTask {
    private final BiConsumer<String, String> yazConsumer;

    public YazRefresher(BiConsumer<String, String> usersListConsumer) {
        this.yazConsumer = usersListConsumer;
    }

//
    @Override
    public void run() {
        HttpClientUtil.runAsync(HttpClientUtil.getYazUrl(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resp[] = response.body().string().split(System.lineSeparator());
                yazConsumer.accept(resp[0], resp[1]);
            }
        });
    }
}
