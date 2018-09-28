package hereticpurge.chuckjoker.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class JokeRepository implements LifecycleOwner{

    private LifecycleOwner parent;

    private LiveData<List<JokeItem>> allJokesLive;
    private List<JokeItem> allJokes;
    private JokeViewModel jokeViewModel;

    private boolean setupFinished = false;

    private static JokeRepository jokeRepository;

    public static JokeRepository getJokeRepository(AppCompatActivity appCompatActivity) {
        if (jokeRepository == null) {
            jokeRepository = new JokeRepository(appCompatActivity);
        }
        return jokeRepository;
    }

    private JokeRepository(AppCompatActivity parent) {
        this.parent = parent;
        jokeViewModel = ViewModelProviders.of(parent).get(JokeViewModel.class);
        allJokesLive = jokeViewModel.getAllJokes();
        allJokesLive.observe(this, jokeItems -> {
            allJokes = jokeItems;
            setupFinished = true;
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return parent.getLifecycle();
    }

    public LiveData<List<JokeItem>> getAllJokesLive() {
        return allJokesLive;
    }

    public List<JokeItem> getAllJokes() {
        return allJokes;
    }

    public boolean isReady() {
        return setupFinished;
    }
}
