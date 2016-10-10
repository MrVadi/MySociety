package ua.mrvadi.mysociety.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.activities.HomeActivity;
import ua.mrvadi.mysociety.adapters.ContactsAdapter;
import ua.mrvadi.mysociety.constants.Consts;
import ua.mrvadi.mysociety.helpers.DialogHelper;
import ua.mrvadi.mysociety.managers.MrFragmentManager;
import ua.mrvadi.mysociety.models.Contact;

/**
 * Created by mrvadi on 04.10.16.
 */
public class ContactsFragment extends Fragment {

    private TextView emptyView;
    private RecyclerView contactsRecycler;
    private ContactsAdapter adapter;
    private RecyclerTouchListener touchListener;
    private LinearLayoutManager linearLayoutManager;
    private Contact callContact;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        setHasOptionsMenu(true);
        setTitle();

        initFragmentView(fragmentView);

        return fragmentView;
    }

    private void initFragmentView(View rootView) {

        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new ContactsAdapter();

        contactsRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view_contacts);
        contactsRecycler.setLayoutManager(linearLayoutManager);
        contactsRecycler.setAdapter(adapter);
        initListener(contactsRecycler);

        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        manageEmptyView();
    }

    private void initListener(RecyclerView recyclerView) {
        touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setIndependentViews(R.id.contacts_call)
                .setViewsToFade(R.id.contacts_call)
                .setSwipeOptionViews(R.id.contacts_edit, R.id.contacts_delete)
                .setSwipeable(R.id.contacts_foreground, R.id.contacts_background,
                        new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                            @Override
                            public void onSwipeOptionClicked(int viewID, int position) {
                                switch (viewID) {
                                    case R.id.contacts_edit:
                                        showEditDialog(adapter.getItem(position));
                                        break;
                                    case R.id.contacts_delete:
                                        deleteContact(position);
                                        break;
                                }
                            }
                        });

        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                showContact(adapter.getItem(position));
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {
                switch (independentViewID) {
                    case R.id.contacts_call:
                        callContact = adapter.getItem(position);
                        performCall(callContact);
                        break;
                }
            }
        });
    }

    private void setTitle() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }
    }

    private void showCreateDialog() {
        DialogFragment dialogFragment = new CreateEditDialogFragment();
        dialogFragment.setTargetFragment(ContactsFragment.this, 123);
        new MrFragmentManager((HomeActivity) getActivity())
                .fullScreenDialog(dialogFragment);

    }

    private void showEditDialog(Contact contact) {
        Bundle arguments = new Bundle();
        arguments.putInt("ID", contact.getId());

        DialogFragment dialogFragment = new CreateEditDialogFragment();
        dialogFragment.setArguments(arguments);
        dialogFragment.setTargetFragment(ContactsFragment.this, 123);
        new MrFragmentManager((HomeActivity) getActivity())
                .fullScreenDialog(dialogFragment);

    }

    private void showContact(Contact contact) {
        Bundle arguments = new Bundle();
        arguments.putInt("ID", contact.getId());

        DialogFragment dialogFragment = new ShowDialogFragment();
        dialogFragment.setArguments(arguments);
        dialogFragment.setTargetFragment(ContactsFragment.this, 123);
        new MrFragmentManager((HomeActivity) getActivity())
                .fullScreenDialog(dialogFragment);

    }

    private void performCall(Contact contact) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED)
            this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    Consts.PERMISSION_CALL_REQUEST_CODE);
        else
            performCallIntent(contact);
    }

    private void performCallIntent(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + contact.getPhoneNumber()));
        startActivity(intent);
    }

    private void performDialIntent(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + contact.getPhoneNumber()));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        contactsRecycler.addOnItemTouchListener(touchListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        contactsRecycler.removeOnItemTouchListener(touchListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Consts.UPDATE_CODE:
                setTitle();
                manageEmptyView();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public void manageEmptyView() {
        if (adapter.getContacts().size() > 0)
            emptyView.setVisibility(View.GONE);
        else
            emptyView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_home, menu);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_all:
                deleteAllContacts();
                return true;
            case R.id.action_add_contact:
                showCreateDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllContacts() {
        if (adapter.getItemCount() > 0)
            DialogHelper.showConfirmationDialog(getContext(), R.string.message_delete_all,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.removeAll();
                            manageEmptyView();
                        }
                    });
        else
            Toast.makeText(getContext(), R.string.no_contacts_have, Toast.LENGTH_SHORT).show();
    }

    private void deleteContact(final int position) {
        Contact contact = adapter.getItem(position);
        String message = String.format(getResources().getString(R.string.message_delete),
                contact.getDisplayName());
        DialogHelper.showConfirmationDialog(getContext(), message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.removeItem(position);
                        manageEmptyView();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Consts.PERMISSION_CALL_REQUEST_CODE:
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            performDialIntent(callContact);
                        } else if (Manifest.permission.CALL_PHONE.equals(permission)) {
                            DialogHelper.showRationale(getContext(),
                                    R.string.rationale_calls,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            performCall(callContact);
                                        }
                                    });
                        }
                    } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        performCall(callContact);
                    }
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Consts.POSITION_STATE, linearLayoutManager.findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            contactsRecycler.scrollToPosition(savedInstanceState.getInt(Consts.POSITION_STATE));
        }
    }
}

