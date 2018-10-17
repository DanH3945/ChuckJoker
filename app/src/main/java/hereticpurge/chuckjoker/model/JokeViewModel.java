package hereticpurge.chuckjoker.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;

import hereticpurge.chuckjoker.database.DatabaseThreadManager;
import hereticpurge.chuckjoker.database.JokeDatabase;

public class JokeViewModel extends AndroidViewModel {

    private JokeDatabase jokeDatabase;
    private Handler jokeThreadHandler;

    public JokeViewModel(@NonNull Application application) {
        super(application);

        jokeDatabase = JokeDatabase.getDatabase(this.getApplication());
        jokeThreadHandler = DatabaseThreadManager.getManager().getHandler();
    }

    LiveData<List<JokeItem>> getAllJokes() {
        return jokeDatabase.jokeDao().getAllJokes();
    }

    public void insertJoke(JokeItem jokeItem) {
        jokeThreadHandler.post(() -> jokeDatabase.jokeDao().insertJoke(jokeItem));
    }

    public void deleteSingleJoke(JokeItem jokeItem) {
        jokeThreadHandler.post(() -> jokeDatabase.jokeDao().deleteSingleJoke(jokeItem));
    }

    public void deleteAllJokes() {
        jokeThreadHandler.post(() -> jokeDatabase.jokeDao().deleteAllJokeItems());
    }
}
