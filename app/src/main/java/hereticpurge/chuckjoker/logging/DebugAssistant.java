package hereticpurge.chuckjoker.logging;

import timber.log.Timber;

public final class DebugAssistant {

    // Methods from this class are intended for debugging only and are expected to be removed when
    // done working on the section of code.   If you need to leave a normal logging message somewhere
    // long term use the regular Timber methods.

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

    public static void threadCheck() {
        Timber.d("ACTIVE THREAD IS: %s", Thread.currentThread().getName());
    }
}
