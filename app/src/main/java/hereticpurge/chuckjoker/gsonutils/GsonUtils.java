package hereticpurge.chuckjoker.gsonutils;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import hereticpurge.chuckjoker.model.JokeItem;
import timber.log.Timber;

public final class GsonUtils {

    public static final int UNPACK_FAILED = -1;

    public static @Nullable
    JokeItem unpackJoke(String jsonString) {
        try {
            JSONObject baseObject = new JSONObject(jsonString);
            if (baseObject.getString("type").equalsIgnoreCase("Success")) {
                JSONObject jsonObject = baseObject.getJSONObject("value");
                JokeItem jokeItem = new JokeItem();
                jokeItem.setDateAdded(new Date());
                jokeItem.setId(Integer.parseInt(jsonObject.getString("id")));
                jokeItem.setJokeBody(jsonObject.getString("joke"));
                return jokeItem;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.d("Json Exception Thrown");
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
