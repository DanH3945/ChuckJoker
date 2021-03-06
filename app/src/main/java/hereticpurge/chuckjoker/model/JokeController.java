package hereticpurge.chuckjoker.model;

import android.content.Context;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.api.ApiClient;
import hereticpurge.chuckjoker.api.apimodel.ApiJokeCountItem;
import hereticpurge.chuckjoker.api.apimodel.ApiJokeItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class JokeController extends Observable {

    private int mCurrentJokeId;

    private int mTotalJokesAvailable;

    private JokeItem mCurrentJoke;

    private Map<Integer, JokeItem> jokeCache;

    private ApiClient mApiClient;

    private static JokeController sJokeController;

    private JokeController(Context context, ApiClient apiClient) {
        mApiClient = apiClient;

        jokeCache = new TreeMap<>();

        setTotalJokeCount();
        loadJoke(context, 1);
    }

    public static JokeController getJokeController(Context context, ApiClient apiClient) {
        if (sJokeController == null) {
            sJokeController = new JokeController(context, apiClient);
        }

        return sJokeController;
    }

    private void setTotalJokeCount() {

        Call<ApiJokeCountItem> call = mApiClient.getJokeCount();
        call.enqueue(new Callback<ApiJokeCountItem>() {
            @Override
            public void onResponse(Call<ApiJokeCountItem> call, Response<ApiJokeCountItem> response) {
                mTotalJokesAvailable = response.body().getValue();
                Timber.d("Setting joke count: %s", response.body().getValue());
            }

            @Override
            public void onFailure(Call<ApiJokeCountItem> call, Throwable t) {
                Timber.d(t);
            }
        });

    }

    public void loadJoke(Context context, int id) {

        if (jokeCache.containsKey(id)) {
            setCurrentJoke(jokeCache.get(id));
            Timber.d("Cache contains joke #: " + id + " loading!!");
            return;
        }

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

        if (!jokeCache.containsKey(jokeItem.getId())) {
            jokeCache.put(jokeItem.getId(), jokeItem);
        }

        mCurrentJoke = jokeItem;
        mCurrentJokeId = mCurrentJoke.getId();
        setChanged();
        notifyObservers(mCurrentJoke);
    }

    public JokeItem getCurrentJoke() {
        return mCurrentJoke;
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
