package hereticpurge.chuckjoker.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import hereticpurge.chuckjoker.model.JokeItem;

@Database(entities = {JokeItem.class}, version = 1)
public abstract class JokeDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "chuck_joker_database";

    private static JokeDatabase jokeDatabase;

    public abstract JokeDao jokeDao();

    public static JokeDatabase getDatabase(Context context) {
        if (jokeDatabase == null) {
            jokeDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    JokeDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return jokeDatabase;
    }

    public static void destroyInstance() {
        jokeDatabase = null;
    }

}
