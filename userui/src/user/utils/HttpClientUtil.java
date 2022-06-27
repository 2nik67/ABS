package user.utils;

import client.Client;
import okhttp3.*;

import java.util.function.Consumer;

public class HttpClientUtil {

    private final static String loadMoneyFinalUrl = "http://localhost:8080/web_Web/servlets/LoadMoney";
    private final static String getClientUrl = "http://localhost:8080/web_Web/servlets/GetUserByName";
    private final static String loadLoanUrl = "http://localhost:8080/web_Web/servlets/NewLoan";
    private final static String yazUrl = "http://localhost:8080/web_Web/servlets/Yaz";
    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static String getYazUrl(){
        return yazUrl;
    }
    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static String createLoadLoanUrl(String name){
        return HttpUrl.parse(loadLoanUrl)
                .newBuilder()
                .addQueryParameter("ClientName", name)
                .build().toString();
    }

    public static String createNewLoanUrl(String clientName, String loanID, String capital, String totalYaz,
                                          String yazInterval, String interest, String category) {
        return HttpUrl.parse(loadLoanUrl)
                .newBuilder()
                .addQueryParameter("LoanID", loanID)
                .addQueryParameter("ClientName", clientName)
                .addQueryParameter("Capital", capital)
                .addQueryParameter("TotalYaz", totalYaz)
                .addQueryParameter("YazInterval", yazInterval)
                .addQueryParameter("InterestPerPayment", interest)
                .addQueryParameter("Category", category)
                .build().toString();
    }

    public static String createGetClientUrl(String name){
        return HttpUrl.parse(getClientUrl)
                .newBuilder()
                .addQueryParameter("ClientName", name)

                .build().toString();

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

    public static void runAsyncPost(String finalUrl, RequestBody requestBody, Callback callback){

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(requestBody)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
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
