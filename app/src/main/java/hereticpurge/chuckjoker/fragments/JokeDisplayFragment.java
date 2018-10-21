package hereticpurge.chuckjoker.fragments;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ThreadLocalRandom;

import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.database.DatabaseThreadManager;
import hereticpurge.chuckjoker.gsonutils.GsonUtils;
import hereticpurge.chuckjoker.gsonutils.JokeTotalCountGsonObject;
import hereticpurge.chuckjoker.icndb.ApiCalls;
import hereticpurge.chuckjoker.icndb.ApiReference;
import hereticpurge.chuckjoker.model.JokeItem;
import hereticpurge.chuckjoker.model.JokeRepository;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class JokeDisplayFragment extends Fragment {

    private TextView mJokeBodyTextView;
    private TextView mJokeCountTextView;

    private JokeRepository mJokeRepository;

    private Button mRandomJokeButton;

    private int mCurrentDisplayIndex;

    private int DEFAULT_INDEX = 1;

    private String INDEX_SAVE_KEY = "indexSaveKey";

    public static JokeDisplayFragment createInstance() {
        return new JokeDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_display_fragment_layout, container, false);

        this.setRetainInstance(true);

        mCurrentDisplayIndex = DEFAULT_INDEX;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mJokeBodyTextView = view.findViewById(R.id.joke_display_joke_body_text);
        mJokeCountTextView = view.findViewById(R.id.joke_display_joke_count);

        mJokeRepository = JokeRepository.getJokeRepository();

        mRandomJokeButton = view.findViewById(R.id.joke_display_fragment_random_joke_button);
        mRandomJokeButton.setOnClickListener(v -> getRandomJoke());

        getJoke(mCurrentDisplayIndex);

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            if (savedInstanceState != null) {
                mCurrentDisplayIndex = savedInstanceState.getInt(INDEX_SAVE_KEY);
            }
        } catch (NullPointerException e) {
            // Error loading the save state so we log and do nothing while letting the default state load
            Timber.d("NullPointerException while loading saved instance state");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INDEX_SAVE_KEY, mCurrentDisplayIndex);
    }

    private boolean showJoke(String jokeBody) {
        // Boolean return values here just in case they are needed later.
        if (jokeBody != null && !jokeBody.equals("")) {
            mJokeBodyTextView.setText(jokeBody);
            return true;
        }
        mJokeBodyTextView.setText(getActivity().getResources().getString(R.string.joke_body_error));
        return false;
    }

    private void getJoke(int jokeNum) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.get(ApiReference.SINGLE_JOKE_URL + jokeNum);

        ApiCalls.GET(client, url, new ApiCalls.ApiCallback<String>() {
            @Override
            public void response(int responseCode, @Nullable String s) {
                JokeItem jokeItem = GsonUtils.unpackJoke(s);
                Handler handler = new Handler(getActivity().getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showJoke(jokeItem.getJokeBody());
                    }
                });
            }
        });
    }

    private void getRandomJoke() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.get(ApiReference.ALL_JOKES_COUNT_URL);

        ApiCalls.GET(client, url, new ApiCalls.ApiCallback<String>() {
            @Override
            public void response(int responseCode, @Nullable String s) {
                int maxJokeCount = GsonUtils.unpackTotalJokesCount(s);
                int randomJokeNumber = ThreadLocalRandom.current().nextInt(0, maxJokeCount);
                getJoke(randomJokeNumber);
            }
        });
    }
}
