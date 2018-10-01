package hereticpurge.chuckjoker.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

public class JokeRepository implements LifecycleOwner {

    private LifecycleOwner parent;

    private LiveData<List<JokeItem>> allJokesLive;
    private List<JokeItem> allJokes;
    private JokeViewModel jokeViewModel;

    private boolean setupFinished = false;

    private static JokeRepository jokeRepository;

    public static JokeRepository initRepository(FragmentActivity fragmentActivity) {
        if (jokeRepository == null) {
            jokeRepository = new JokeRepository(fragmentActivity);
        }
        return jokeRepository;
    }

    public static @Nullable
    JokeRepository getJokeRepository() {
        if (jokeRepository != null) {
            return jokeRepository;
        } else {
            return null;
        }
    }

    private JokeRepository(FragmentActivity parent) {
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
