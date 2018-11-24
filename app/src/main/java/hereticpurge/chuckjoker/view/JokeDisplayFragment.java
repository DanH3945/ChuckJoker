package hereticpurge.chuckjoker.view;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import hereticpurge.chuckjoker.ChuckJokerApplication;
import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.apiservice.ApiClient;
import hereticpurge.chuckjoker.apiservice.ApiJokeCountItem;
import hereticpurge.chuckjoker.apiservice.ApiJokeItem;
import hereticpurge.chuckjoker.apiservice.ApiReference;
import hereticpurge.chuckjoker.model.JokeItem;
import hereticpurge.chuckjoker.view.viewutils.LoadingSpinner;
import hereticpurge.chuckjoker.view.viewutils.SimpleSwipeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class JokeDisplayFragment extends Fragment {

    private TextView mJokeBodyTextView;
    private TextView mCurrentJokeNumText;

    private Button mRandomJokeButton;

    private int mCurrentDisplayIndex;

    private int DEFAULT_INDEX = 1;

    private int mTotalJokesAvailable = 0;

    private String INDEX_SAVE_KEY = "indexSaveKey";

    private LoadingSpinner mLoadingSpinner;

    ApiClient mApiClient;

    private Tracker mTracker;
    private static final String TAG = "JokeDisplayFragment";

    public static JokeDisplayFragment createInstance() {
        return new JokeDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_display_fragment_layout, container, false);

        setRetainInstance(true);

        mCurrentDisplayIndex = DEFAULT_INDEX;

        mLoadingSpinner = new LoadingSpinner(this);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiReference.ICNDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiClient = retrofit.create(ApiClient.class);

        mJokeBodyTextView = view.findViewById(R.id.joke_display_joke_body_text);
        mCurrentJokeNumText = view.findViewById(R.id.joke_display_joke_number_text);

        initCheckPreferences();
        initGetTotalJokes();

        mRandomJokeButton = view.findViewById(R.id.joke_display_fragment_random_joke_button);
        mRandomJokeButton.setOnClickListener(v -> getRandomJoke());

        if (savedInstanceState == null) {
            getRandomJoke();
        }

        if (getActivity() != null) {
            // Google Analytics tracker.
            mTracker = ((ChuckJokerApplication) getActivity().getApplication()).getDefaultTracker();
        }

        view.setOnTouchListener(new SimpleSwipeListener(new SimpleSwipeListener.Callback() {
            @Override
            public void onSwipeLeftToRight() {
                previousJoke();
            }

            @Override
            public void onSwipeRightToLeft() {
                nextJoke();
            }
        }));

        view.findViewById(R.id.joke_display_next_joke_button).setOnClickListener(v -> nextJoke());

        view.findViewById(R.id.joke_display_previous_joke_button).setOnClickListener(v -> previousJoke());

        SearchView searchView = view.findViewById(R.id.joke_display_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    int jokeNum = Integer.parseInt(query);
                    searchView.clearFocus();
                    searchView.setQuery("", false);
                    moveToJoke(jokeNum);
                    return true;
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.joke_error_number_format_exception, Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void initGetTotalJokes() {
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

    private void initCheckPreferences() {
        String jokeNumKey = getResources().getString(R.string.pref_show_joke_num_key);
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(jokeNumKey, false)) {
            mCurrentJokeNumText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
//        need a new tracking id for this app before enabling the actual analytics tracking.
//        mTracker.setScreenName(TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            if (savedInstanceState != null) {
                mCurrentDisplayIndex = savedInstanceState.getInt(INDEX_SAVE_KEY);
                getJoke(mCurrentDisplayIndex);
            }
        } catch (NullPointerException e) {
            // Error loading the save state so we log and do nothing while letting the default state load
            Timber.d("NullPointerException while loading saved instance state");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INDEX_SAVE_KEY, mCurrentDisplayIndex);
        super.onSaveInstanceState(outState);
    }

    private boolean showJoke(String jokeBody) {
        // Boolean return values here just in case they are needed later.
        if (jokeBody != null && !jokeBody.equals("")) {
            String jokeBodyFormatted = jokeBody.replace("&quot;", "\"");
            mCurrentJokeNumText.setText(String.valueOf(mCurrentDisplayIndex));
            mJokeBodyTextView.setText(jokeBodyFormatted);
            mLoadingSpinner.hideLoadingSpinner();
            return true;
        }
        mJokeBodyTextView.setText(getActivity().getResources().getString(R.string.joke_body_error));
        mLoadingSpinner.hideLoadingSpinner();
        return false;
    }

    private void returnToFirstJoke() {
        getJoke(1);
    }

    private void moveToJoke(int jokeNum) {
        mCurrentDisplayIndex = jokeNum;
        if (jokeNum < mTotalJokesAvailable && jokeNum > 0) {
            getJoke(jokeNum);
        } else {
            missingJokeError();
        }
    }

    private void nextJoke() {
        if (mCurrentDisplayIndex < mTotalJokesAvailable) {
            getJoke(++mCurrentDisplayIndex);
        }
    }

    private void previousJoke() {
        if (mCurrentDisplayIndex > 1) {
            getJoke(--mCurrentDisplayIndex);
        }
    }

    private void getJoke(int jokeNum) {
        mLoadingSpinner.showLoadingSpinner();
        Call<ApiJokeItem> call = mApiClient.getJoke(String.valueOf(jokeNum));
        call.enqueue(new Callback<ApiJokeItem>() {
            @Override
            public void onResponse(Call<ApiJokeItem> call, Response<ApiJokeItem> response) {
                JokeItem jokeItem = response.body().getValue();

                if (jokeItem != null) {
                    mCurrentDisplayIndex = jokeItem.getId();
                    showJoke(jokeItem.getJoke());
                }
            }

            @Override
            public void onFailure(Call<ApiJokeItem> call, Throwable t) {
                Timber.d(t);
                missingJokeError();
            }
        });
    }

    private void getRandomJoke() {
        Call<ApiJokeItem> call = mApiClient.getRandomJoke();
        call.enqueue(new Callback<ApiJokeItem>() {
            @Override
            public void onResponse(Call<ApiJokeItem> call, Response<ApiJokeItem> response) {
                JokeItem jokeItem = response.body().getValue();

                if (jokeItem != null) {
                    mCurrentDisplayIndex = jokeItem.getId();
                    showJoke(jokeItem.getJoke());
                }
            }

            @Override
            public void onFailure(Call<ApiJokeItem> call, Throwable t) {
                Timber.d(t);
                missingJokeError();
            }
        });
    }

    private void missingJokeError() {
        mJokeBodyTextView.setText(R.string.joke_error_missing_joke);
        mCurrentJokeNumText.setText(String.valueOf(mCurrentDisplayIndex));
        mLoadingSpinner.hideLoadingSpinner();
    }
}
