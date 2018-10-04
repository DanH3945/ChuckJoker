package hereticpurge.chuckjoker.fragments;

import android.os.Bundle;
import android.support.annotation.IntRange;
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
import java.util.stream.IntStream;

import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.model.JokeItem;
import hereticpurge.chuckjoker.model.JokeRepository;
import timber.log.Timber;

public class JokeDisplayFragment extends Fragment {

    private TextView mJokeBodyTextView;

    private JokeRepository mJokeRepository;

    private Button mRandomJokeButton;

    public static JokeDisplayFragment createInstance() {
        return new JokeDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_display_fragment_layout, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mJokeBodyTextView = view.findViewById(R.id.joke_display_joke_body_text);

        mJokeRepository = JokeRepository.getJokeRepository();

        mRandomJokeButton = view.findViewById(R.id.joke_display_fragment_random_joke_button);
        mRandomJokeButton.setOnClickListener(v -> {
            List<JokeItem> jokeItemList = mJokeRepository.getAllJokes();
        });

        return view;
    }

    private boolean setJokeBodyTextViewText(String jokeString) {
        // Boolean return values here just in case they are needed later.
        if (jokeString != null && !jokeString.equals("")) {
            mJokeBodyTextView.setText(jokeString);
            return true;
        }
        mJokeBodyTextView.setText(getActivity().getResources().getString(R.string.joke_body_error));
        return false;
    }
}
