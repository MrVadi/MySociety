package ua.mrvadi.mysociety.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.activities.base.BaseActivity;
import ua.mrvadi.mysociety.fragments.ContactsFragment;
import ua.mrvadi.mysociety.fragments.InfoFragment;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.managers.MrFragmentManager;
import ua.mrvadi.mysociety.models.Admin;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private MrFragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = new MrFragmentManager(this);
        setDrawer();

        if(savedInstanceState == null) {
            manageFirstRun();
        }

        setDrawerHeader();
        animateToggle();
        fragmentManager.getManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                animateToggle();
            }
        });
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            animateToggle();
            if (fragmentManager.isHome()) {
                super.onBackPressed();
            } else {
                fragmentManager.pop();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_contacts:
                fragmentManager.set(new ContactsFragment());
                closeDrawer();
                break;
            case R.id.nav_about_me:
                fragmentManager.set(new InfoFragment());
                closeDrawer();
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }
        return true;
    }

    private void closeDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setDrawer() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void manageFirstRun() {
        navigationView.getMenu().getItem(0).setChecked(true);
        fragmentManager.add(new ContactsFragment());
        fragmentManager.getManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                animateToggle();
            }
        });
    }

//    private void initFab() {
//        fab = (FloatingActionButton) findViewById(R.id.fab_create_contact);
//
//    }

    private void setDrawerHeader() {
        Admin info = DBHelper.getInstance().getAdminInfo();
        View header = navigationView.getHeaderView(0);
        ImageView avatar = (ImageView) header.findViewById(R.id.drawer_header_image);
        Picasso.with(this)
                .load(R.drawable.about_photo)
                .into(avatar);

        TextView name = (TextView) header.findViewById(R.id.drawer_header_name);
        name.setText(info.getDisplayName());

        TextView email = (TextView) header.findViewById(R.id.drawer_header_email);
        email.setText(info.getEmail());
    }

    private void logOut() {
        DBHelper.getInstance().setLogined(false);
        startActivity(new Intent(HomeActivity.this, SplashActivity.class));
        this.overridePendingTransition(R.anim.animation_start_in, R.anim.animation_end_out);
        finish();
    }

    private void animateToggle() {
        boolean ishome = fragmentManager.isHome();
        float start;
        float end;

        if(ishome) {
            start = 1.0f;
            end = 0.0f;
        } else {
            start = 0.0f;
            end = 1.0f;
        }

        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                toggle.onDrawerSlide(drawer, slideOffset);

            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (fragmentManager.isHome()) {
                    setDrawer();
                } else {
                    toolbar.setNavigationOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onBackPressed();
                                }
                            }
                    );
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();
    }
}
