package hereticpurge.chuckjoker.logging;

import timber.log.Timber;

public final class DebugAssistant {

    private DebugAssistant() {
    }

    private static final String TAG = "DebugAssistant";

    public static void nullityCheck(Object object) {
        if (object == null) Timber.d("OBJECT WAS NULL");
        else Timber.d("OBJECT WAS NOT NULL");
    }

    public static void callCheck() {
        callCheck("METHOD WAS CALLED");
    }

    public static void callCheck(String string) {
        Timber.d("CALL CHECK %s", string);
    }
}
