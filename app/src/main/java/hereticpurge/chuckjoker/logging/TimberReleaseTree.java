package hereticpurge.chuckjoker.logging;

import android.support.annotation.NonNull;
import android.util.Log;

import timber.log.Timber;

public class TimberReleaseTree extends Timber.Tree {
    @Override
    protected boolean isLoggable(String tag, int priority) {
        return priority != Log.VERBOSE && priority != Log.DEBUG;
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (isLoggable(tag, priority)) {
            Log.println(priority, tag, message);
        }
    }
}
