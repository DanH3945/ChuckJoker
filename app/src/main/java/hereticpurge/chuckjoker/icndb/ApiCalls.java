package hereticpurge.chuckjoker.icndb;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public final class ApiCalls {

    private ApiCalls() {}

    public interface ApiCallback<T> {
        void response(int responseCode, @Nullable T t);
    }

    public static void GET(OkHttpClient client,
                    HttpUrl httpUrl,
                    ApiCallback<String> apiCallback) {

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Timber.d("OkHttp Callback onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                apiCallback.response(response.code(), response.body().string());
            }
        });
    }
}
