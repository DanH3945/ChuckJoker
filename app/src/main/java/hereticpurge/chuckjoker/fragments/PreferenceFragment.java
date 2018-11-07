package hereticpurge.chuckjoker.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import hereticpurge.chuckjoker.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    public static final String TAG = "PreferenceFragment";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);
    }
}
