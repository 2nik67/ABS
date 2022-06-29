package user.components.scrambletab;

import com.google.gson.Gson;
import loan.Loan;
import loan.category.Category;
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

public class CategoryCheckListRefresher extends TimerTask {
    private final Consumer<List<Category>> categoryConsumer;

    public CategoryCheckListRefresher(Consumer<List<Category>> loanConsumer) {
        this.categoryConsumer = loanConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(HttpClientUtil.getCategoryList(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfCategories = response.body().string();
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUsersNames);
                Category[] categories = new Gson().fromJson(jsonArrayOfCategories, Category[].class);
                categoryConsumer.accept(Arrays.asList(categories));
            }
        });
    }
}
