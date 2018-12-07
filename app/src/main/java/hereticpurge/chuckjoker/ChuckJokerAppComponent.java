package hereticpurge.chuckjoker;

import android.content.Context;

import dagger.Component;
import hereticpurge.chuckjoker.apiservice.ApiClient;
import hereticpurge.chuckjoker.daggermodules.NetworkModule;
import hereticpurge.chuckjoker.model.JokeController;
import okhttp3.OkHttpClient;

@Component(modules = NetworkModule.class)
public interface ChuckJokerAppComponent {

    OkHttpClient getOkHttpClient();

    ApiClient getRetrofitApiClient();

    JokeController getJokeController();
}
