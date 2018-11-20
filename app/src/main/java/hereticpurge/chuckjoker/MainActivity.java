package hereticpurge.chuckjoker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import hereticpurge.chuckjoker.fragments.AboutDialogFragment;
import hereticpurge.chuckjoker.fragments.JokeDisplayFragment;
import hereticpurge.chuckjoker.fragments.PreferenceFragment;
import hereticpurge.chuckjoker.logging.TimberReleaseTree;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

//    private JokeRepository mJokeRepository;

//    private JokeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TIMBER MUST BE THE FIRST THING LOADED TO HANDLE DEBUGGING CORRECTLY
        // NOTHING GOES ABOVE THIS COMMENT
        if (BuildConfig.DEBUG) {
            // Timber debug tree
            Timber.plant(new Timber.DebugTree());
        } else {
            // Timber Release Tree
            Timber.plant(new TimberReleaseTree());
        }
        // END OF REQUIRED TIMBER LOAD

        setContentView(R.layout.activity_main);

//        DatabaseThreadManager.getManager().initDatabaseThread();

//        mJokeRepository = JokeRepository.initRepository(this);

        if (savedInstanceState == null) {
            loadFragment(getJokeDisplayFragment(), false, null);
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        AdView mAdView = findViewById(R.id.included_ad_view);
        mAdView.loadAd(adRequest);

        // Fix for ad view dropping the frame rate of the app. Credit to Martin on stackoverflow.com
        // for giving me the idea to switch the layer type.  My solution is somewhat simpler.  Just
        // change the layer type for the entire view since i'm only trying to load test ads on a demo.
        // https://stackoverflow.com/questions/9366365/android-admob-admob-ad-refresh-destroys-frame-rate
        mAdView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onStart() {
//        mJokeRepository = JokeRepository.initRepository(this);
//        mViewModel = ViewModelProviders.of(this).get(JokeViewModel.class);
        super.onStart();
    }

    @Override
    protected void onStop() {
//        JokeRepository.clearRepository();
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private JokeDisplayFragment getJokeDisplayFragment() {
        return JokeDisplayFragment.createInstance();
    }

    private PreferenceFragment getPreferenceFragment() {
        return new PreferenceFragment();
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);

        if (addToBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

//    public void checkForNewJokes(View view) {
//        OkHttpClient client = new OkHttpClient();
//        HttpUrl url = HttpUrl.get(ApiReference.ALL_JOKES_COUNT_URL);
//        ApiCalls.get(client, url, (responseCode, s) -> {
//            if (s != null && mJokeRepository.isReady()) {
//                int numJokesAvailable = JsonUtils.unpackTotalJokesCount(s);
//                if (numJokesAvailable > mJokeRepository.getAllJokes().size() &&
//                        MainActivity.this.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
//                    Handler handler = new Handler(this.getMainLooper());
//                    handler.post(() -> populateDatabase(numJokesAvailable));
//                } else {
//                    Timber.d("Jokes on Api: %s, Jokes in Database: %s", numJokesAvailable, mJokeRepository.getAllJokes().size());
//                }
//            } else if (!mJokeRepository.isReady()) {
//                checkForNewJokes(view);
//            }
//        });
//    }

//    private void populateDatabase(int numJokes) {
//        while (!DatabaseThreadManager.getManager().isReady()) {
//            // Todo Fix this so the app doesn't time out and get killed
//            // Just waiting on the ThreadManager
//        }
//        // For now we're wiping the database each time we need to add new jokes.
//        // Horribly inefficient.  Fix me later.
//        mViewModel.deleteAllJokes();
//
//        OkHttpClient client = new OkHttpClient();
//        for (int i = 1; i < numJokes; i++) {
//            HttpUrl url = HttpUrl.parse(ApiReference.SINGLE_JOKE_URL + i);
//            ApiCalls.get(client, url, (responseCode, s) -> {
//                JokeItem jokeItem = JsonUtils.unpackJoke(s);
//                if (jokeItem != null) {
//                    Timber.d("Inserting joke");
//                    mViewModel.insertJoke(jokeItem);
//                }
//            });
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.overflow_menu_wipe_database:
//                mViewModel.deleteAllJokes();
//                break;

//            case R.id.overflow_menu_debug_add_joke:
//                JokeItem jokeItem = new JokeItem();
//                jokeItem.setDateAdded(new Date());
//                jokeItem.setJokeBody("Funny stuff here");
//                mViewModel.insertJoke(jokeItem);

            case R.id.overflow_menu_about:
                new AboutDialogFragment().show(getSupportFragmentManager(), null);
                break;

            case R.id.overflow_menu_preferences:
                loadFragment(getPreferenceFragment(), true, PreferenceFragment.TAG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
