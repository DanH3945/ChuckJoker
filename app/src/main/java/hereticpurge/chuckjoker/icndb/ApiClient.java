package hereticpurge.chuckjoker.icndb;

import hereticpurge.chuckjoker.model.JokeItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiClient {

    @GET(ApiReference.SINGLE_JOKE_URL + "{jokeNum}")
    Call<JokeItem> getJoke(@Path("jokeNum") String jokeNum);


}
