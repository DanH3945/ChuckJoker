package hereticpurge.chuckjoker.model;

import android.content.Context;

import java.util.Observable;
import java.util.Observer;

import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.apiservice.ApiClient;
import hereticpurge.chuckjoker.apiservice.ApiJokeCountItem;
import hereticpurge.chuckjoker.apiservice.ApiJokeItem;
import hereticpurge.chuckjoker.apiservice.ApiReference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class JokeController extends Observable {

    private static JokeController sJokeController;

    private int mCurrentJokeId;

    private int mTotalJokesAvailable;

    private JokeItem mCurrentJoke;

    private ApiClient mApiClient;

    private JokeController() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiReference.ICNDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiClient = retrofit.create(ApiClient.class);

        setTotalJokeCount();
    }

    public static JokeController getJokeController(Context context) {
        if (sJokeController == null) {
            sJokeController = new JokeController();
            sJokeController.loadJoke(context, 1);
        }
        return sJokeController;
    }

    private void setTotalJokeCount() {
        Call<ApiJokeCountItem> call = mApiClient.getJokeCount();
        call.enqueue(new Callback<ApiJokeCountItem>() {
            @Override
            public void onResponse(Call<ApiJokeCountItem> call, Response<ApiJokeCountItem> response) {
                mTotalJokesAvailable = response.body().getValue();
            }

            @Override
            public void onFailure(Call<ApiJokeCountItem> call, Throwable t) {
                Timber.d(t);
            }
        });
    }

    public JokeItem getCurrentJoke() {
        return mCurrentJoke;
    }

    public int getCurrentJokeId() {
        return mCurrentJokeId;
    }

    public void loadJoke(Context context, int id) {
        Call<ApiJokeItem> call = mApiClient.getJoke(String.valueOf(id));
        call.enqueue(new Callback<ApiJokeItem>() {
            @Override
            public void onResponse(Call<ApiJokeItem> call, Response<ApiJokeItem> response) {
                setCurrentJoke(response.body().getValue());
            }

            @Override
            public void onFailure(Call<ApiJokeItem> call, Throwable t) {
                Timber.d("JOKE LOAD FAILURE");
                JokeItem errorJokeItem = new JokeItem();
                errorJokeItem.setId(id);
                errorJokeItem.setJoke(context.getResources().getString(R.string.joke_error_missing_joke));
                setCurrentJoke(errorJokeItem);
                Timber.d(t);
            }
        });
    }

    public void loadRandomJoke(Context context) {
        Call<ApiJokeItem> call = mApiClient.getRandomJoke();
        call.enqueue(new Callback<ApiJokeItem>() {
            @Override
            public void onResponse(Call<ApiJokeItem> call, Response<ApiJokeItem> response) {
                setCurrentJoke(response.body().getValue());
            }

            @Override
            public void onFailure(Call<ApiJokeItem> call, Throwable t) {
                JokeItem errorJokeItem = new JokeItem();
                errorJokeItem.setId(mCurrentJokeId);
                errorJokeItem.setJoke(context.getResources().getString(R.string.joke_error_missing_joke));
                setCurrentJoke(errorJokeItem);
                Timber.d(t);
            }
        });
    }

    private void setCurrentJoke(JokeItem jokeItem) {
        mCurrentJoke = jokeItem;
        mCurrentJokeId = mCurrentJoke.getId();
        setChanged();
        notifyObservers(mCurrentJoke);
    }

    public void nextJoke(Context context) {
        if (mCurrentJokeId < mTotalJokesAvailable) {
            loadJoke(context, ++mCurrentJokeId);
        }
    }

    public void previousJoke(Context context) {
        if (mCurrentJokeId > 1) {
            loadJoke(context, --mCurrentJokeId);
        }
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);

        if (mCurrentJoke != null) {
            o.update(this, mCurrentJoke);
        }
    }
}
