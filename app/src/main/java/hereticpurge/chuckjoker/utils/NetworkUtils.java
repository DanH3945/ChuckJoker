package hereticpurge.chuckjoker.utils;

import java.io.IOException;

import hereticpurge.chuckjoker.icndb.ApiReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public final class NetworkUtils {

    public String getJokeCountJson() {
        OkHttpClient client = new OkHttpClient();
        String url = ApiReference.ICNDB_BASE_URL + ApiReference.ALL_JOKES_COUNT_URL;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
