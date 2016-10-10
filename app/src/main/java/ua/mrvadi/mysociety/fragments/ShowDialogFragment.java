package ua.mrvadi.mysociety.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.constants.Consts;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.helpers.ImageHelper;
import ua.mrvadi.mysociety.helpers.MyValidator;
import ua.mrvadi.mysociety.models.Contact;
import ua.mrvadi.mysociety.widgets.RoundedImageView;

/**
 * Created by mrvadi on 10.10.16.
 */
public class ShowDialogFragment extends DialogFragment {

    private RoundedImageView photo;
    private TextView displayName;
    private TextView phoneNumber;
    private Contact contact;

    private int editId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_show, container, false);
        setHasOptionsMenu(true);

        getArgs();
        manageActionBar();
        initDialogViews(rootView);
        manageViews();
        return rootView;
    }

    private boolean getArgs() {
        if (getArguments() != null) {
            editId = getArguments().getInt("ID");
            contact = DBHelper.getInstance().getContact(editId);
            return true;
        } else {
            return false;
        }
    }

    private void manageActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

    }

    private void initDialogViews(View rootView) {
        photo = (RoundedImageView) rootView.findViewById(R.id.show_dialog_photo);
        displayName = (TextView) rootView.findViewById(R.id.show_display_name);
        phoneNumber = (TextView) rootView.findViewById(R.id.show_display_phone);
    }

    private void manageViews() {

        if (getArgs()) {
            displayName.setText(MyValidator.displayName(contact));
            phoneNumber.setText(contact.getPhoneNumber());

            if (contact.getPhoto() != null)
                photo.setImageBitmap(ImageHelper.bitmapFromBytes(contact.getPhoto()));
            else
                photo.setBackground(ImageHelper.gmailRoundWithColor(
                        contact.getDisplayName(),
                        Integer.parseInt(contact.getColor())));
        }
    }

    private void updateTargetFragment() {
        getTargetFragment().onActivityResult(Consts.UPDATE_CODE, AppCompatActivity.RESULT_OK, new Intent());
    }

    @Override
    public void onStop() {
        super.onStop();
        updateTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
//
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        getActivity().getMenuInflater().inflate(R.menu.menu_dialog_show, menu);
        getActivity().invalidateOptionsMenu();
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_call:
////                createOrEditContact();
//                return true;
//            case R.id.action_edit:
////                createOrEditContact();
//                return true;
//            case R.id.action_delete:
////                createOrEditContact();
//                return true;
//            case android.R.id.home:
//                getActivity().onBackPressed();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (displayName.getText() != null)
            outState.putString(Consts.DISPLAY_NAME_STATE, displayName.getText().toString());

        if (phoneNumber.getText() != null)
            outState.putString(Consts.PHONE_NUM_STATE, phoneNumber.getText().toString());

        if (photo.getDrawable() != null)
            outState.putByteArray(Consts.PHOTO_STATE, ImageHelper.getBytesFromImage(photo));
        else
            outState.putString(Consts.COLOR_STATE, contact.getColor());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getString(Consts.DISPLAY_NAME_STATE) != null)
                displayName.setText(savedInstanceState.getString(Consts.DISPLAY_NAME_STATE));
            if (savedInstanceState.getString(Consts.PHONE_NUM_STATE) != null)
                phoneNumber.setText(savedInstanceState.getString(Consts.PHONE_NUM_STATE));

            if (savedInstanceState.getByteArray(Consts.PHOTO_STATE) != null) {
                photo.setImageBitmap(ImageHelper.
                        bitmapFromBytes(savedInstanceState.getByteArray(Consts.PHOTO_STATE)));
                photo.setBackground(null);
            } else {
                photo.setBackground(ImageHelper.gmailRoundWithColor(
                        contact.getDisplayName(),
                        Integer.parseInt(savedInstanceState.getString(Consts.COLOR_STATE))));
            }
        }
    }
}





