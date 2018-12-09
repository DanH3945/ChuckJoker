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

import java.util.Observable;
import java.util.Observer;

import hereticpurge.chuckjoker.ChuckJokerApplication;
import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.dagger.components.DaggerJokeControllerComponent;
import hereticpurge.chuckjoker.dagger.components.JokeControllerComponent;
import hereticpurge.chuckjoker.dagger.modules.ContextModule;
import hereticpurge.chuckjoker.model.JokeController;
import hereticpurge.chuckjoker.model.JokeItem;
import hereticpurge.chuckjoker.view.viewutils.LoadingSpinner;
import hereticpurge.chuckjoker.view.viewutils.SimpleSwipeListener;

public class JokeDisplayFragment extends Fragment implements Observer {

    private TextView mJokeBodyTextView;
    private TextView mCurrentJokeNumText;

    private LoadingSpinner mLoadingSpinner;

    private Tracker mTracker;
    private static final String TAG = "JokeDisplayFragment";

    private JokeController mJokeController;

    public static JokeDisplayFragment createInstance() {
        return new JokeDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_display_fragment_layout, container, false);

        mLoadingSpinner = new LoadingSpinner(this);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mJokeBodyTextView = view.findViewById(R.id.joke_display_joke_body_text);
        mCurrentJokeNumText = view.findViewById(R.id.joke_display_joke_number_text);

        initCheckPreferences();

        Button mRandomJokeButton = view.findViewById(R.id.joke_display_fragment_random_joke_button);
        mRandomJokeButton.setOnClickListener(v -> mJokeController.loadRandomJoke(getContext()));

        if (getActivity() != null) {
            // Google Analytics tracker.
            mTracker = ((ChuckJokerApplication) getActivity().getApplication()).getDefaultTracker();
        }

        // mJokeController = JokeController.getJokeController(getContext());

        JokeControllerComponent jokeControllerComponent = DaggerJokeControllerComponent.builder()
                .contextModule(new ContextModule(getContext()))
                .build();

        mJokeController = jokeControllerComponent.getJokeController();

        view.setOnTouchListener(new SimpleSwipeListener(new SimpleSwipeListener.Callback() {
            @Override
            public void onSwipeLeftToRight() {
                mJokeController.previousJoke(getContext());
            }

            @Override
            public void onSwipeRightToLeft() {
                mJokeController.nextJoke(getContext());
            }
        }));

        view.findViewById(R.id.joke_display_next_joke_button).setOnClickListener(v -> mJokeController.nextJoke(getContext()));

        view.findViewById(R.id.joke_display_previous_joke_button).setOnClickListener(v -> mJokeController.previousJoke(getContext()));

        SearchView searchView = view.findViewById(R.id.joke_display_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    int jokeNum = Integer.parseInt(query);
                    searchView.clearFocus();
                    searchView.setQuery("", false);
                    mJokeController.loadJoke(getContext(), jokeNum);
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

        mLoadingSpinner.hideLoadingSpinner();

        return view;
    }

    private void initCheckPreferences() {
        String jokeNumKey = getResources().getString(R.string.pref_show_joke_num_key);

        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(jokeNumKey, true)) {
            mCurrentJokeNumText.setVisibility(View.VISIBLE);
        } else {
            mCurrentJokeNumText.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onResume() {
        mJokeController.addObserver(this);
//        need a new tracking id for this app before enabling the actual analytics tracking.
//        mTracker.setScreenName(TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    @Override
    public void onPause() {
        mJokeController.deleteObserver(this);
        super.onPause();
    }

    private void showJoke(JokeItem jokeItem) {
        String jokeBody = jokeItem.getJoke();
        int jokeNumber = jokeItem.getId();

        if (jokeBody != null && !jokeBody.equals("")) {
            String jokeBodyFromHtml = android.text.Html.fromHtml(jokeBody).toString();
            mCurrentJokeNumText.setText(String.valueOf(jokeNumber));
            mJokeBodyTextView.setText(jokeBodyFromHtml);
            mLoadingSpinner.hideLoadingSpinner();
            return;
        }
        mJokeBodyTextView.setText(getActivity().getResources().getString(R.string.joke_body_error));
        mLoadingSpinner.hideLoadingSpinner();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof JokeItem) {
            showJoke((JokeItem) arg);
        }
    }
}
