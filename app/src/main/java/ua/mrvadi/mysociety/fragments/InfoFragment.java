package ua.mrvadi.mysociety.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class InfoFragment extends Fragment {

    private ImageView photo;
    private TextView firstName;
    private TextView lastName;
    private TextView phone;
    private TextView email;
    private TextView linkedIn;
    private TextView aboutMe;

    private String info;
    private int tapCount;

    private Admin admin = DBHelper.getInstance().getAdminInfo();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView =  inflater.inflate(R.layout.fragment_info, container, false);

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
        aboutMe = (TextView) rootView.findViewById(R.id.about_me);

        tapCount = 5;
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
    private String formatInfo(int tapCount) {
        String info;
        info = String.format(getResources().getString(R.string.admin_info), String.valueOf(tapCount));
        return info;
    }

    private void setCounter() {
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tapCount--;
                aboutMe.setText(formatInfo(tapCount));
                if (tapCount < 1) {

                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Consts.TAP_COUNTER, String.valueOf(tapCount));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null &&
                savedInstanceState.getString(Consts.TAP_COUNTER) != null)
            tapCount = Integer.valueOf(savedInstanceState.getString(Consts.TAP_COUNTER));
    }
}
