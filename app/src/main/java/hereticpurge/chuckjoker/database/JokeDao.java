package hereticpurge.chuckjoker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import hereticpurge.chuckjoker.model.JokeItem;

@Dao
public interface JokeDao {

    @Query("SELECT * FROM JokeItem")
    LiveData<List<JokeItem>> getAllJokes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertJoke(JokeItem jokeItem);

    @Delete
    long deleteSingleJoke(JokeItem jokeItem);

    @Query("DELETE FROM jokeitem")
    void deleteAllJokeItems();
}
