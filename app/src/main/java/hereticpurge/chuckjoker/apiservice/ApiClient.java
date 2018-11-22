package hereticpurge.chuckjoker.apiservice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiClient {

    @GET(ApiReference.SINGLE_JOKE_URL + "{jokeNum}")
    Call<ApiJokeItem> getJoke(@Path("jokeNum") String jokeNum);

    @GET(ApiReference.RANDOM_JOKE_URL)
    Call<ApiJokeItem> getRandomJoke();

    @GET(ApiReference.ALL_JOKES_COUNT_URL)
    Call<ApiJokeCountItem> getJokeCount();

}
