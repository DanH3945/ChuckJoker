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


        if (savedInstanceState == null) {
            loadFragment(getJokeDisplayFragment(), false, null);
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        AdView mAdView = findViewById(R.id.included_ad_view);
        mAdView.loadAd(adRequest);

        // Fix for ad view dropping the frame rate of the app.
        mAdView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


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
