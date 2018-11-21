package hereticpurge.chuckjoker.icndb;

import android.support.annotation.Nullable;

import java.io.IOException;

import hereticpurge.chuckjoker.jsonutils.JsonUtils;
import hereticpurge.chuckjoker.model.JokeItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@Deprecated
public final class ApiCalls {

    private ApiCalls() {
    }

    public interface ApiCallback<T> {
        void response(int responseCode, @Nullable T t);
    }

    public static void get(OkHttpClient client,
                           HttpUrl httpUrl,
                           ApiCallback<String> apiCallback) {

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // This represents an IO failure of some kind and not just a failure to get a
                // response from a server.
                Timber.d("OkHttp Callback onFailure");
                Timber.e(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                apiCallback.response(response.code(), response.body().string());
            }
        });
    }

    public static void getSingleJokeItem(int jokeNum, ApiCallback<JokeItem> paramCallback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.get(ApiReference.SINGLE_JOKE_URL + jokeNum);

        get(client, url, (responseCode, s) -> paramCallback.response(responseCode, JsonUtils.unpackJoke(s)));
    }

    public static void getRandomJokeItem(ApiCallback<JokeItem> paramCallback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.get(ApiReference.RANDOM_JOKE_URL);

        get(client, url, (responseCode, s) -> paramCallback.response(responseCode, JsonUtils.unpackJoke(s)));
    }

    public static void getTotalJokes(ApiCallback<Integer> paramCallback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.get(ApiReference.ALL_JOKES_COUNT_URL);

        get(client, url, (responseCode, s) -> paramCallback.response(responseCode, JsonUtils.unpackTotalJokesCount(s)));
    }
}
