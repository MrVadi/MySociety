package ua.mrvadi.mysociety.activities.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.constants.Consts;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.models.Admin;
import ua.mrvadi.mysociety.models.Contact;

/**
 * Created by mrvadi on 07.10.16.
 */
public class BaseActivity extends AppCompatActivity implements
        RecyclerTouchListener.RecyclerTouchListenerHelper {
    private OnActivityTouchListener touchListener;

    private Admin mrVadi = DBHelper.getInstance().getAdminInfo();
    private SharedPreferences prefs;

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (touchListener != null) touchListener.getTouchCoordinates(event);

        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            if (w != null) {
                w.getLocationOnScreen(scrcoords);
                float x = event.getRawX() + w.getLeft() - scrcoords[0];
                float y = event.getRawY() + w.getTop() - scrcoords[1];

                if (event.getAction() == MotionEvent.ACTION_UP
                        && (x < w.getLeft() || x >= w.getRight()
                        || y < w.getTop() || y > w.getBottom())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getWindow().getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                        getWindow().getCurrentFocus().clearFocus();
                    }
                }
            }
            return ret;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.back_again , Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
