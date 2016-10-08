package ua.mrvadi.mysociety.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.constants.Consts;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.helpers.ImageHelper;
import ua.mrvadi.mysociety.models.Admin;

/**
 * Created by mrvadi on 04.10.16.
 */
public class InfoFragment extends Fragment implements View.OnClickListener {

    private ImageView photo;
    private TextView firstName;
    private TextView lastName;
    private TextView phone;
    private TextView email;
    private TextView linkedIn;
    private TextView titleInfo;
    private TextView aboutMe;

    private TextView made;
    private TextView with;
    private TextView by;
    private TextView mrvadi;
    private ImageView droid;
    private Button returnBtn;

    private int tapCount;
    boolean shaked;

    private Animation shake;
    float distance;
    float rotation;

    private Admin admin = DBHelper.getInstance().getAdminInfo();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_info, container, false);

        initFragmentView(fragmentView);
        setInfo();
        setCounter();

        return fragmentView;
    }

    private void initFragmentView(View rootView) {
        photo = (ImageView) rootView.findViewById(R.id.info_photo);
        firstName = (TextView) rootView.findViewById(R.id.info_first_name);
        lastName = (TextView) rootView.findViewById(R.id.info_last_name);
        phone = (TextView) rootView.findViewById(R.id.info_phone);
        email = (TextView) rootView.findViewById(R.id.info_email);
        linkedIn = (TextView) rootView.findViewById(R.id.linked_in);
        titleInfo = (TextView) rootView.findViewById(R.id.about_me_title);
        aboutMe = (TextView) rootView.findViewById(R.id.about_me);

        tapCount = 5;
        aboutMe.setText(formatInfo(tapCount));

        made = (TextView) rootView.findViewById(R.id.text_made);
        with = (TextView) rootView.findViewById(R.id.text_with);
        droid = (ImageView) rootView.findViewById(R.id.droid);
        by = (TextView) rootView.findViewById(R.id.text_by);
        mrvadi = (TextView) rootView.findViewById(R.id.text_mrvadi);
        returnBtn = (Button) rootView.findViewById(R.id.return_btn);
    }

    private void setInfo() {
        photo.setImageDrawable(ImageHelper.roundedDrawableFromBytes(getContext(), admin.getPhoto()));
        firstName.setText(admin.getFirstName());
        lastName.setText(admin.getLastName());
        phone.setText(admin.getPhoneNumber());
        email.setText(admin.getEmail());
        linkedIn.setText(admin.getLinkedIn());
        aboutMe.setText(formatInfo(tapCount));
    }

    private Spanned formatInfo(int tapCount) {
        return Html.fromHtml(String.format(getResources().getString(R.string.admin_info), String.valueOf(tapCount)));
    }

    private void setCounter() {
        photo.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Consts.TAP_COUNTER_STATE, String.valueOf(tapCount) + "");
        outState.putBoolean(Consts.SHAKED_STATE, shaked);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(Consts.TAP_COUNTER_STATE) != null)
                tapCount = Integer.valueOf(savedInstanceState.getString(Consts.TAP_COUNTER_STATE));
            if (savedInstanceState.getBoolean(Consts.SHAKED_STATE)) {
                startAnimation(!savedInstanceState.getBoolean(Consts.SHAKED_STATE), 0);
            }
            aboutMe.setText(formatInfo(tapCount));
        }
    }

    @Override
    public void onClick(View view) {
        tapCount--;
        aboutMe.setText(formatInfo(tapCount));
        if (tapCount < 1) {
            startAnimation(false, 1500);
            tapCount = 5;
        }

    }

    private void startShake(View... views) {
        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake_infinite);
        for (View view : views) {
            view.startAnimation(shake);
        }
    }

    private void stopShake(View... views) {
        for (View view : views) {
            view.clearAnimation();
        }
    }

    private void startAnimation(final boolean back, final long duration) {
        photo.setOnClickListener(null);
        final View[] views = {photo, firstName,
                lastName, phone, email, linkedIn,
                titleInfo, aboutMe};

        startShake(views);
        shaked = true;

        distance = getResources().getDimension(R.dimen.animation_distance);
        rotation = distance;

        if (back) {
            distance = 0.0f;
            rotation = distance;
            stopShake(views);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lastName.animate().translationY(distance)
                        .translationX(-distance)
                        .rotationY(distance)
                        .setDuration(duration);

                linkedIn.animate().translationY(distance)
                        .translationX(distance)
                        .rotationX(distance)
                        .setDuration(duration)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                photo.animate().rotationY(distance)
                                        .translationX(distance)
                                        .translationY(distance)
                                        .setDuration(duration);
                                titleInfo.animate().rotationY(distance)
                                        .translationX(distance)
                                        .translationY(-distance)
                                        .setDuration(duration);
                                phone.animate().rotationY(distance)
                                        .translationX(-distance)
                                        .translationY(distance)
                                        .setDuration(duration)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                email.animate().rotationX(distance)
                                                        .translationX(distance)
                                                        .translationY(-distance)
                                                        .setDuration(duration);
                                                firstName.animate().translationX(-distance)
                                                        .translationY(distance)
                                                        .rotationY(distance)
                                                        .setDuration(duration);
                                                aboutMe.animate().rotationY(distance)
                                                        .translationX(distance)
                                                        .translationY(distance)
                                                        .setDuration(duration)
                                                        .withEndAction(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                stopShake(views);
                                                                if(back) {
                                                                    photo.setOnClickListener(InfoFragment.this);
                                                                } else {
                                                                animateWithLove(500);
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });

            }
        }, Consts.START_ANIMATION_DELAY);
    }

    private void animateWithLove(final long dur) {
        final View[] withLove = {made, with, by, mrvadi, droid, returnBtn};
        setAlphaZero(1, true, withLove);

        made.animate().alpha(1.0f).setDuration(dur).withEndAction(new Runnable() {
            @Override
            public void run() {
                with.animate().alpha(1.0f).setDuration(dur).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        droid.animate().alpha(1.0f).setDuration(dur).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                by.animate().alpha(1.0f).setDuration(dur).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        mrvadi.animate().alpha(1.0f).setDuration(dur).withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                returnBtn.animate().alpha(1.0f).setDuration(dur);
                                                returnBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        startAnimation(true, 1500);
                                                        shaked = false;
                                                        setAlphaZero(1500, false, made, with,
                                                                by, mrvadi,
                                                                droid, returnBtn);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void setAlphaZero(long duration, final boolean visible, final View... views) {
        for (View view : views) {
            view.animate().alpha(0.0f).setDuration(duration).withEndAction(new Runnable() {
                @Override
                public void run() {
                    View[] withLove = { made, with, by, mrvadi, droid, returnBtn };
                    if (visible)
                        setVisibility(View.VISIBLE, withLove);
                    else
                        setVisibility(View.GONE, withLove);
                }
            });
        }
    }
    private void setVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

}
