package hereticpurge.chuckjoker.fragments.fragmentutils;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import hereticpurge.chuckjoker.R;

public class LoadingSpinner {

    private FrameLayout mFragmentContainer;
    private ProgressBar mProgressBar;

    public LoadingSpinner(Fragment fragment) {
        mFragmentContainer = fragment.getActivity().findViewById(R.id.main_fragment_container);
        mProgressBar = fragment.getActivity().findViewById(R.id.loading_spinner);
    }

    public void showLoadingSpinner() {
        if (!spinnerIsShowing()) {
            mFragmentContainer.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoadingSpinner() {
        if (spinnerIsShowing()) {
            mFragmentContainer.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    boolean spinnerIsShowing() {
        return mProgressBar.getVisibility() == View.VISIBLE
                && mFragmentContainer.getVisibility() == View.INVISIBLE;
    }
}
