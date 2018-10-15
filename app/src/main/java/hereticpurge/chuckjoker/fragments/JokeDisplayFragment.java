package hereticpurge.chuckjoker.fragments;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.database.DatabaseThreadManager;
import hereticpurge.chuckjoker.logging.DebugAssistant;
import hereticpurge.chuckjoker.model.JokeItem;
import hereticpurge.chuckjoker.model.JokeRepository;
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
        mRandomJokeButton.setOnClickListener(v -> {
            mCurrentDisplayIndex = ThreadLocalRandom.current().nextInt(0, mJokeRepository.getAllJokes().size());
            showJoke();
        });

        // All done with the setup.  Load the first joke as soon as the repository is ready.
        // Here we're putting the call to sleep until the repo is ready into a runnable and sending
        // it off onto our second thread (in this case the database thread since it's easily accessible
        // and creating another thread just for this one call is a bit excessive.
        DatabaseThreadManager.getManager().getHandler().post(new Runnable() {
            @Override
            public void run() {
                while (!mJokeRepository.isReady()) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        // Do nothing since we're just waiting for the repo to be ready.
                    }
                }

                // The repo is now ready so we get the main Thread's handler and push the call
                // to show back onto the main thread so we can manipulate views.
                Handler handler = new Handler(getActivity().getMainLooper());
                handler.post(() -> {
                    if (JokeDisplayFragment.this.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        showJoke();
                        bindJokeCount();
                    }
                });
            }
        });

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

    private boolean showJoke() {
        String jokeString = null;
        if (mJokeRepository.getAllJokes().size() > 0) {
            jokeString = mJokeRepository.getAllJokes().get(mCurrentDisplayIndex).getJokeBody();
        }
        // Boolean return values here just in case they are needed later.
        if (jokeString != null && !jokeString.equals("")) {
            mJokeBodyTextView.setText(jokeString);
            return true;
        }
        mJokeBodyTextView.setText(getActivity().getResources().getString(R.string.joke_body_error));
        return false;
    }

    private void bindJokeCount() {
        mJokeRepository.getAllJokesLive().observe(this, jokeItems -> {
            if (jokeItems != null) {
                mJokeCountTextView.setText(Integer.toString(jokeItems.size()));
            }
        });
    }
}
