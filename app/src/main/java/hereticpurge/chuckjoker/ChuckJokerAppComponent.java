package hereticpurge.chuckjoker;

import dagger.Component;
import hereticpurge.chuckjoker.apiservice.ApiClient;
import hereticpurge.chuckjoker.model.JokeController;
import okhttp3.OkHttpClient;

@Component
public interface ChuckJokerAppComponent {

    OkHttpClient getOkHttpClient();

    ApiClient getRetrofitClient();

    JokeController getJokeController();
}
