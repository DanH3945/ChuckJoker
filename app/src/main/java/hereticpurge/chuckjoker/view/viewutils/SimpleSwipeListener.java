package hereticpurge.chuckjoker.view.viewutils;

import android.view.MotionEvent;
import android.view.View;

import timber.log.Timber;

public class SimpleSwipeListener implements View.OnTouchListener {

    public interface Callback {
        void onSwipeLeftToRight();

        void onSwipeRightToLeft();
    }

    private float xStart = 0;
    private float xStop = 0;
    private float yStart = 0;
    private float yStop = 0;

    // the min distance a user must swipe across the screen for this listener to register it as
    // a swipe.
    final float SWIPE_X_MIN_DISTANCE = 200;

    // the max y distance (up / down) a user is allowed to press before the event won't be
    // recognized.
    final float SWIPE_Y_MAX_DISTANCE = 500;

    private Callback mCallback;

    public SimpleSwipeListener(Callback callback) {
        mCallback = callback;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Timber.d("Caught motion event.");
        // send the click to the view so that other methods (accessibility stuff) can see
        // that an event occured and where.
        // v.performClick();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // store the starting position
                xStart = event.getX();
                yStart = event.getY();
                Timber.d("X POSITION: " + Float.toString(xStart));
                Timber.d("Y POSITION: " + Float.toString(yStart));
                break;

            case MotionEvent.ACTION_UP:
                // store the stop position
                xStop = event.getX();
                yStop = event.getY();

                // getting the absolute values to determine swipe distance.
                float xTrans = Math.abs(xStop - xStart);
                float yTrans = Math.abs(yStop - yStart);

                // making sure the motion was intentional by ensuring motion distances.
                if (xTrans > SWIPE_X_MIN_DISTANCE && yTrans < SWIPE_Y_MAX_DISTANCE) {
                    if (xStart > xStop) {
                        mCallback.onSwipeRightToLeft();
                    } else {
                        mCallback.onSwipeLeftToRight();
                    }
                }
                Timber.d("----------  MOTION EVENT BREAK  ----------");
                break;
        }
        // tell the system that the event was handled.
        return true;
    }
}
