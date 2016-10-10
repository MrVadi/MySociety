package ua.mrvadi.mysociety.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.constants.Consts;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.helpers.DialogHelper;
import ua.mrvadi.mysociety.helpers.ImageHelper;
import ua.mrvadi.mysociety.helpers.MyValidator;
import ua.mrvadi.mysociety.models.Contact;
import ua.mrvadi.mysociety.widgets.RoundedImageView;

/**
 * Created by mrvadi on 04.10.16.
 */
public class CreateEditDialogFragment extends DialogFragment {

    private RoundedImageView photo;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout phoneNumber;
    private View rootView;

    private int editId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_create, container, false);
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
            return true;
        } else {
            return false;
        }
    }

    private void manageActionBar() {
        String title;
        if (getArgs())
            title = "Edit Contact";
        else
            title = "New Contact";

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(title);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear);
        }

    }

    private void initDialogViews(View rootView) {
        this.rootView = rootView;

        photo = (RoundedImageView) rootView.findViewById(R.id.crate_dialog_photo);
        firstName = (TextInputLayout) rootView.findViewById(R.id.create_first_name_wrapper);
        lastName = (TextInputLayout) rootView.findViewById(R.id.create_last_name_wrapper);
        phoneNumber = (TextInputLayout) rootView.findViewById(R.id.create_phone_wrapper);
    }

    private void manageViews() {

        if (getArgs()) {
            Contact contact = DBHelper.getInstance().getContact(editId);
            if (firstName.getEditText() != null)
                firstName.getEditText().setText(contact.getFirstName());
            if (lastName.getEditText() != null)
                lastName.getEditText().setText(contact.getLastName());
            if (phoneNumber.getEditText() != null)
                phoneNumber.getEditText().setText(contact.getPhoneNumber());

            if (contact.getPhoto() != null)
                photo.setImageBitmap(ImageHelper.bitmapFromBytes(contact.getPhoto()));
        }

        if (photo.getDrawable() == null)
            photo.setBackground(getResources().getDrawable(R.drawable.background_dummy_photo));

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photo.getDrawable() != null)
                    showPopupMenu(view, R.menu.popup_new_photo);
                else
                    showPopupMenu(view, R.menu.popup_set_photo);

            }
        });
    }

    private void createOrEditContact() {
        Contact contact = new Contact();
        if (phoneNumber.getEditText() != null &&
                !phoneNumber.getEditText().getText().toString().isEmpty()) {
            phoneNumber.setError("");
            phoneNumber.setErrorEnabled(false);

            if (firstName.getEditText() != null)
                contact.setFirstName(firstName.getEditText().getText().toString());
            if (lastName.getEditText() != null)
                contact.setLastName(lastName.getEditText().getText().toString());
            if (photo.getDrawable() != null)
                contact.setPhoto(ImageHelper.getBytesFromImage(photo));

            contact.setPhoneNumber(phoneNumber.getEditText().getText().toString());
            contact.setDisplayName(MyValidator.displayName(contact));

            if (getArgs()) {
                contact.setId(editId);
                DBHelper.getInstance().changeContact(contact);
            } else {
                contact.setColor(String.valueOf(ImageHelper.getRandomColor()));
                DBHelper.getInstance().addContact(contact);
            }
            getActivity().onBackPressed();

        } else {
            Toast.makeText(getContext(), R.string.should_have_phone, Toast.LENGTH_SHORT).show();
            phoneNumber.setErrorEnabled(true);
            phoneNumber.setError(getContext().getString(R.string.fill_this_line));
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (getArgs())
            getActivity().getMenuInflater().inflate(R.menu.menu_dialog_edit, menu);
        else
            getActivity().getMenuInflater().inflate(R.menu.menu_dialog_create, menu);

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create:
                createOrEditContact();
                return true;
            case R.id.action_done:
                createOrEditContact();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPopupMenu(View anchor, int menuId) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        popupMenu.inflate(menuId);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_take_photo:
                        takePhoto();
                        return true;
                    case R.id.menu_choose_from_gallery:
                        fromGallery();
                        return true;
                    case R.id.menu_remove_photo:
                        photo.setImageDrawable(null);
                        photo.setBackground(getResources().getDrawable(R.drawable.background_dummy_photo));
                        return true;
                    case R.id.menu_take_new_photo:
                        takePhoto();
                        return true;
                    case R.id.menu_choose_new_from_gallery:
                        fromGallery();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void fromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Consts.PERMISSION_GALLERY_REQUEST_CODE);
        } else {
            Intent intentGallery = new Intent();
            intentGallery.setType("image/*");
            intentGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intentGallery, "Select Picture"), Consts.GALLERY_REQUEST);
        }
    }

    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    Consts.PERMISSION_PHOTO_REQUEST_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, Consts.CAMERA_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Consts.CAMERA_REQUEST:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
                    photo.setImageBitmap(photoBitmap);
                    photo.setBackground(null);
                }
                break;
            case Consts.GALLERY_REQUEST:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    Picasso.with(getContext()).load(data.getData()).into(photo);
                    photo.setBackground(null);
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Consts.PERMISSION_PHOTO_REQUEST_CODE:
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            DialogHelper.goToSettingsSnackbar(rootView);
                        } else if (Manifest.permission.CAMERA.equals(permission)) {
                            DialogHelper.showRationale(getContext(), R.string.rationale_photo, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    takePhoto();
                                }
                            });
                        }
                    } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    }
                }
                break;
            case Consts.PERMISSION_GALLERY_REQUEST_CODE:
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            DialogHelper.goToSettingsSnackbar(rootView);
                        } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                            DialogHelper.showRationale(getContext(), R.string.rationale_gallery, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fromGallery();
                                }
                            });
                        }
                    } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fromGallery();
                    }
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(firstName.getEditText() != null
                && !firstName.getEditText().getText().toString().isEmpty())
            outState.putString(Consts.FIRST_NAME_STATE, firstName.getEditText().getText().toString());

        if(lastName.getEditText() != null
                && !lastName.getEditText().getText().toString().isEmpty())
            outState.putString(Consts.LAST_NAME_STATE, lastName.getEditText().getText().toString());

        if(phoneNumber.getEditText() != null
                && !phoneNumber.getEditText().getText().toString().isEmpty())
            outState.putString(Consts.PHONE_NUM_STATE, phoneNumber.getEditText().getText().toString());

        if(photo.getDrawable() != null) {
            outState.putByteArray(Consts.PHOTO_STATE, ImageHelper.getBytesFromImage(photo));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (firstName.getEditText() != null
                    && savedInstanceState.getString(Consts.FIRST_NAME_STATE) != null)
                firstName.getEditText().setText(savedInstanceState.getString(Consts.FIRST_NAME_STATE));
            if (lastName.getEditText() != null
                    && savedInstanceState.getString(Consts.LAST_NAME_STATE) != null)
                lastName.getEditText().setText(savedInstanceState.getString(Consts.LAST_NAME_STATE));
            if (phoneNumber.getEditText() != null
                    && savedInstanceState.getString(Consts.PHONE_NUM_STATE) != null)
                phoneNumber.getEditText().setText(savedInstanceState.getString(Consts.PHONE_NUM_STATE));

            if (savedInstanceState.getByteArray(Consts.PHOTO_STATE) != null) {
                photo.setImageBitmap(ImageHelper.
                        bitmapFromBytes(savedInstanceState.getByteArray(Consts.PHOTO_STATE)));
                photo.setBackground(null);
            } else {
                photo.setBackground(getResources().getDrawable(R.drawable.background_dummy_photo));
            }
        }
    }
}


