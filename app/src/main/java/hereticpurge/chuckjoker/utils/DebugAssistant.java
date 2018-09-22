package hereticpurge.chuckjoker.utils;

import android.util.Log;

public final class DebugAssistant {

    private DebugAssistant(){}

    private static final String TAG = "DebugAssistant";

    public static void nullityCheck(Object object) {
        if (object == null) Log.e(TAG, "nullityCheck: OBJECT WAS NULL");
        else Log.e(TAG, "nullityCheck: OBJECT WAS NOT NULL");
    }

    public static void callCheck() {
        callCheck("METHOD WAS CALLED");
    }

    public static void callCheck(String string) {
        Log.e(TAG, "callCheck: " + string);
    }
}
