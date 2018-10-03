package hereticpurge.chuckjoker;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.Date;

import hereticpurge.chuckjoker.database.DatabaseThreadManager;
import hereticpurge.chuckjoker.fragments.JokeDisplayFragment;
import hereticpurge.chuckjoker.gsonutils.GsonUtils;
import hereticpurge.chuckjoker.icndb.ApiCalls;
import hereticpurge.chuckjoker.icndb.ApiReference;
import hereticpurge.chuckjoker.logging.TimberReleaseTree;
import hereticpurge.chuckjoker.model.JokeItem;
import hereticpurge.chuckjoker.model.JokeRepository;
import hereticpurge.chuckjoker.model.JokeViewModel;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    FrameLayout mFragmentContainer;
    ProgressBar mProgressBar;

    private JokeRepository mJokeRepository;

    private JokeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseThreadManager.getManager().initDatabaseThread();

        mJokeRepository = JokeRepository.initRepository(this);

        mViewModel = ViewModelProviders.of(this).get(JokeViewModel.class);

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
            loadFragment(getJokeDisplayFragment(), false, null);
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

    public void checkForNewJokes(View view) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.get(ApiReference.ALL_JOKES_COUNT_URL);
        ApiCalls.GET(client, url, (responseCode, s) -> {
            if (s != null && mJokeRepository.isReady()) {
                int numJokesAvailable = GsonUtils.unpackTotalJokesCount(s);
                if (numJokesAvailable > mJokeRepository.getAllJokes().size()) {
                    Handler handler = new Handler(this.getMainLooper());
                    handler.post(() -> populateDatabase(numJokesAvailable));
                } else {
                    Timber.d("Jokes on Api: %s, Jokes in Database: %s", numJokesAvailable, mJokeRepository.getAllJokes().size());
                }
            } else if (!mJokeRepository.isReady()) {
                checkForNewJokes(view);
            }
        });
    }

    private void populateDatabase(int numJokes) {
        while (!DatabaseThreadManager.getManager().isReady()) {
            // Just waiting on the ThreadManager
        }
        // For now we're wiping the database each time we need to add new jokes.
        // Horribly inefficient.  Fix me later.
        mViewModel.deleteAllJokes();

        OkHttpClient client = new OkHttpClient();
        for (int i = 1; i < numJokes; i++) {
            HttpUrl url = HttpUrl.parse(ApiReference.SINGLE_JOKE_URL + i);
            ApiCalls.GET(client, url, (responseCode, s) -> {
                JokeItem jokeItem = GsonUtils.unpackJoke(s);
                if (jokeItem != null){
                    mViewModel.insertJoke(jokeItem);
                }
            });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.overflow_menu_wipe_database:
                mViewModel.deleteAllJokes();
                break;

            case R.id.overflow_menu_debug_add_joke:
                JokeItem jokeItem = new JokeItem();
                jokeItem.setDateAdded(new Date());
                jokeItem.setJokeBody("Funny stuff here");
                mViewModel.insertJoke(jokeItem);
        }
        return super.onOptionsItemSelected(item);
    }
}
