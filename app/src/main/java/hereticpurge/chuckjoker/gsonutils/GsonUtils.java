package hereticpurge.chuckjoker.gsonutils;

import com.google.gson.Gson;

import java.util.List;

import hereticpurge.chuckjoker.model.JokeItem;
import timber.log.Timber;

public final class GsonUtils {

    public static final int UNPACK_FAILED = -1;

    public static JokeItem unpackJoke(String jsonString) {
        return null;
    }

    public static List<JokeItem> unpackJokes(String jsonString) {
        return null;
    }

    public static int unpackTotalJokesCount(String jsonString) {
        Gson gson = new Gson();
        JokeTotalCountObject countObject = gson.fromJson(jsonString, JokeTotalCountObject.class);
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
