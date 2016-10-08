package ua.mrvadi.mysociety.managers;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.activities.HomeActivity;

/**
 * Created by mrvadi on 04.10.16.
 */
public class MrFragmentManager {
    final private HomeActivity activity;
    private String currFragmentTag;

    public MrFragmentManager(HomeActivity activity) {
        this.activity = activity;
    }

    public void set(Fragment fragment) {
        FragmentManager fragmentManager = this.activity.getSupportFragmentManager();
        String tag = fragment.getClass().getCanonicalName();

//        if (!this.currFragmentTag.equals(tag)) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            animateTransaction(fragmentTransaction).replace(R.id.main_container, fragment, tag);
//            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
//        }
        this.currFragmentTag = tag;
    }

    public void push(Fragment fragment) {
        FragmentManager fragmentManager = this.activity.getSupportFragmentManager();
        String tag = fragment.getClass().getCanonicalName();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            animateTransaction(fragmentTransaction).replace(R.id.main_container, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

        this.currFragmentTag = tag;
    }


    public void add(Fragment fragment) {
        FragmentManager fragmentManager = this.activity.getSupportFragmentManager();
        String tag = fragment.getClass().getCanonicalName();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        animateTransaction(fragmentTransaction).add(R.id.main_container, fragment, tag);
        fragmentTransaction.commit();

        this.currFragmentTag = tag;
    }

    public void fullScreenDialog(DialogFragment dialog) {
        FragmentManager fragmentManager = this.activity.getSupportFragmentManager();
        String tag = dialog.getClass().getCanonicalName();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(R.id.main_container, dialog, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();


        this.currFragmentTag = tag;
    }

    public FragmentManager getManager() {
        return this.activity.getSupportFragmentManager();
    }

    public String getCurrentFragmentTag() {
        return this.currFragmentTag;
    }

    public void pop() {
        final FragmentManager fm = this.activity.getSupportFragmentManager();
        if (!isHome()) {
            fm.popBackStack();
        }
    }

    public boolean isHome() {
        FragmentManager fm = this.activity.getSupportFragmentManager();
        return !(fm.getBackStackEntryCount() > 0);
    }

    private FragmentTransaction animateTransaction(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        return fragmentTransaction;
    }
}