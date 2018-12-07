package hereticpurge.chuckjoker.daggermodules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import hereticpurge.chuckjoker.apiservice.ApiClient;
import hereticpurge.chuckjoker.apiservice.ApiReference;
import hereticpurge.chuckjoker.model.JokeController;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Provides
    public ApiClient apiClient(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiReference.ICNDB_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiClient.class);
    }

    @Provides
    public JokeController jokeController(ApiClient apiClient) {
        JokeController jokeController = JokeController.getJokeController();
        jokeController.setApiClient(apiClient);
        jokeController.setTotalJokeCount();
        return jokeController;
    }

    @Provides Context applicationContext() {

    }

}
