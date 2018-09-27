package hereticpurge.chuckjoker.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hereticpurge.chuckjoker.R;
import hereticpurge.chuckjoker.model.JokeItem;
import hereticpurge.chuckjoker.utils.NetworkUtils;
import timber.log.Timber;

public class JokeDisplayFragment extends Fragment {

    private List<JokeItem> jokeItemList;
    TextView jokeBodyTextView;

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

        jokeBodyTextView = view.findViewById(R.id.joke_display_joke_body_text);
        jokeBodyTextView.setText("Temp Text");

        return view;
    }
}
