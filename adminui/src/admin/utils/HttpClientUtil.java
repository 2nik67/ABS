package admin.utils;

import okhttp3.*;

import java.util.function.Consumer;

public class HttpClientUtil {

    private final static String yazUrl = "http://localhost:8080/web_Web/servlets/Yaz";


    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static String getYazUrl(){
        return yazUrl;
    }

    public static String createYazRewindUrl(String isRewind){
        return HttpUrl.parse(yazUrl)
                .newBuilder()
                .addQueryParameter("REWIND", isRewind)
                .build().toString();
    }

    public static void runAsyncHead(String finalUrl, RequestBody requestBody, Callback callback){

        Request request = new Request.Builder()
                .url(finalUrl)
                .head()
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncPost(String finalUrl, RequestBody requestBody, Callback callback){

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(requestBody)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
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

