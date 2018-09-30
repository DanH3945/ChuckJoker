package hereticpurge.chuckjoker;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import hereticpurge.chuckjoker.database.DatabaseThreadManager;
import hereticpurge.chuckjoker.fragments.JokeDisplayFragment;
import hereticpurge.chuckjoker.gsonutils.GsonUtils;
import hereticpurge.chuckjoker.icndb.ApiCalls;
import hereticpurge.chuckjoker.icndb.ApiReference;
import hereticpurge.chuckjoker.logging.TimberReleaseTree;
import hereticpurge.chuckjoker.model.JokeRepository;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    FrameLayout mFragmentContainer;
    ProgressBar mProgressBar;

    private JokeRepository mJokeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseThreadManager.getManager().initDatabaseThread();

        mJokeRepository = JokeRepository.getJokeRepository(this);

        if (BuildConfig.DEBUG) {
            // Timber debug tree
            Timber.plant(new Timber.DebugTree());
        } else {
            // Timber Release Tree
            Timber.plant(new TimberReleaseTree());
        }

        mFragmentContainer = findViewById(R.id.main_fragment_container);
        mProgressBar = findViewById(R.id.loading_spinner);

        showLoadingSpinner();

        if (savedInstanceState == null) {
            checkForNewJokes();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private JokeDisplayFragment getJokeDisplayFragment() {
        return JokeDisplayFragment.createInstance();
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack, String tag) {
        showLoadingSpinner();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);

        if (addToBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        hideLoadingSpinner();
    }

    private void checkForNewJokes() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.get(ApiReference.ALL_JOKES_COUNT_URL);
        ApiCalls.GET(client, url, (responseCode, s) -> {
            if (s != null && mJokeRepository.isReady()) {
                int numJokesAvailable = GsonUtils.unpackTotalJokesCount(s);
                Handler handler = new Handler(this.getMainLooper());
                if (numJokesAvailable > mJokeRepository.getAllJokes().size()) {
                    handler.post(() -> populateDatabase(numJokesAvailable));
                } else {
                    handler.post(() -> loadFragment(getJokeDisplayFragment(), false, null));
                }
            } else if (!mJokeRepository.isReady()) {
                checkForNewJokes();
            }
        });
    }

    private void populateDatabase(int numJokes) {
        while (!DatabaseThreadManager.getManager().isReady()) {
            // Just waiting on the ThreadManager
        }
    }

    private void showLoadingSpinner() {
        mFragmentContainer.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingSpinner() {
        mFragmentContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
