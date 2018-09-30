package hereticpurge.chuckjoker.database;

import android.os.Handler;
import android.os.HandlerThread;

public class DatabaseThreadManager {

    private static final String THREAD_NAME = "DatabaseThread";

    private HandlerThread databaseThread;
    private Handler databaseHandler;
    private static DatabaseThreadManager databaseThreadManager;

    private boolean isReady = false;


    private DatabaseThreadManager() {
    }

    public static DatabaseThreadManager getManager() {
        if (databaseThreadManager == null) {
            databaseThreadManager = new DatabaseThreadManager();
        }

        return databaseThreadManager;
    }

    public Handler getHandler() {
        return databaseHandler;
    }

    public void initDatabaseThread() {
        if (databaseThread == null) {
            databaseThread = new HandlerThread(THREAD_NAME);
        }
        databaseThread.start();
        databaseHandler = new Handler(databaseThread.getLooper());
        isReady = true;
    }

    public boolean isReady() {
        return isReady;
    }
}

