package hereticpurge.chuckjoker.jsonutils;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Date;

import hereticpurge.chuckjoker.model.JokeItem;
import timber.log.Timber;

public final class JsonUtils {

    public static final int UNPACK_FAILED = -1;

    public static @Nullable
    JokeItem unpackJoke(String jsonString) {
        Gson gson = new Gson();

        JokeItemGsonResult gsonResult = gson.fromJson(jsonString, JokeItemGsonResult.class);

        if (gsonResult.type.equals("success")) {
            JokeItem jokeItem = new JokeItem();
            jokeItem.setJokeBody(gsonResult.getJokeBody());
            jokeItem.setId(Integer.valueOf(gsonResult.getJokeId()));
            jokeItem.setDateAdded(new Date());
            return jokeItem;
        }
        return null;
    }

    public static int unpackTotalJokesCount(String jsonString) {
        Gson gson = new Gson();
        JokeTotalCountGsonObject countObject = gson.fromJson(jsonString, JokeTotalCountGsonObject.class);
        int returnValue;
        try {
            returnValue = Integer.valueOf(countObject.value);
        } catch (NullPointerException | NumberFormatException e) {
            returnValue = UNPACK_FAILED;
            Timber.d("Null Pointer Exception while trying to unpack countObject.value() : %s", countObject.value);
        }
        return returnValue;
    }
}
