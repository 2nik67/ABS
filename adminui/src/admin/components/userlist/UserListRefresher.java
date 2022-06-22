package admin.components.userlist;

import admin.utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UserListRefresher  extends TimerTask {

   // private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<String>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public UserListRefresher(BooleanProperty shouldUpdate, Consumer<List<String>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {



        final int finalRequestNumber = ++requestNumber;
       // httpRequestLoggerConsumer.accept("About to invoke: " +"http://localhost:8080/web_Web/servlets/UsersList"+ " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync("http://localhost:8080/web_Web/servlets/UserList", new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               // httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUsersNames);
                String[] usersNames = new Gson().fromJson(jsonArrayOfUsersNames, String[].class);
                usersListConsumer.accept(Arrays.asList(usersNames));
            }
        });
    }
}
