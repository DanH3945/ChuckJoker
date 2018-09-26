package hereticpurge.chuckjoker.database;

import android.os.Handler;
import android.os.Looper;

public class DatabaseThreadManager {

    private Thread databaseThread;
    private Handler databaseHandler;
    private static DatabaseThreadManager databaseThreadManager;

    private DatabaseThreadManager(){}

    public static DatabaseThreadManager getManager() {
        if (databaseThreadManager == null) {
            databaseThreadManager = new DatabaseThreadManager();
        }

        return databaseThreadManager;
    }

    public Handler getHandler() {
        if (databaseThread == null) {
            databaseThread = new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    databaseHandler = new Handler();
                    Looper.loop();
                }
            };
        }
        return databaseHandler;
    }
}

