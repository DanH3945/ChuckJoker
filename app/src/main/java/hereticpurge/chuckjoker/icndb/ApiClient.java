package hereticpurge.chuckjoker.icndb;

import hereticpurge.chuckjoker.model.JokeItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiClient {

    @GET("/jokes/{jokeNum}")
    Call<JokeItem> getSingleJoke(@Path("jokeNum") String jokeNumber);


}
