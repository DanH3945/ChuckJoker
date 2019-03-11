package hereticpurge.chuckjoker.model.dagger.modules;

import android.content.Context;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import hereticpurge.chuckjoker.api.ApiClient;
import hereticpurge.chuckjoker.api.ApiReference;
import hereticpurge.chuckjoker.model.JokeController;
import hereticpurge.chuckjoker.model.dagger.qualifiers.ActivityContextQualifier;
import hereticpurge.chuckjoker.model.dagger.scopes.JokeControllerScope;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class JokeControllerModule {

    @Provides
    @JokeControllerScope
    public Cache okHttpCache(@ActivityContextQualifier Context context) {
        File cacheFile = new File(context.getCacheDir(), "okhttpcache");
        cacheFile.mkdirs();

        // 10MB cache
        return new Cache(cacheFile, 10 * 10 * 1000);
    }

    @Provides
    @JokeControllerScope
    public OkHttpClient okHttpClient(Cache cache) {

        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @JokeControllerScope
    public ApiClient apiClient(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiReference.ICNDB_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiClient.class);
    }

    @Provides
    @JokeControllerScope
    public JokeController jokeController(@ActivityContextQualifier Context context, ApiClient apiClient) {
        return JokeController.getJokeController(context, apiClient);
    }

}
