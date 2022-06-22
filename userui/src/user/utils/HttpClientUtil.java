package user.utils;

import okhttp3.*;

import java.util.function.Consumer;

public class HttpClientUtil {

    private final static String loadMoneyFinalUrl = "http://localhost:8080/web_Web/servlets/LoadMoney";

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static String createLoadMoneyUrl(boolean isDeposit, String money, String name){
        if (!isDeposit) {
            money = "-" + money;
        }
        return HttpUrl.parse(loadMoneyFinalUrl)
                .newBuilder()
                .addQueryParameter("ClientName", name)
                .addQueryParameter("Money", money)
                .build().toString();

    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}

