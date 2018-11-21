package hereticpurge.chuckjoker.icndb;

import hereticpurge.chuckjoker.model.JokeItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiClient {

    @GET(ApiReference.ICNDB_BASE_URL + "{url}")
    Call<JokeItem> getJoke(@Path("url") String url);


}
