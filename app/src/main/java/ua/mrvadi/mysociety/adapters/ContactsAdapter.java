package ua.mrvadi.mysociety.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.helpers.ImageHelper;
import ua.mrvadi.mysociety.helpers.MyValidator;
import ua.mrvadi.mysociety.models.Contact;

/**
 * Created by mrvadi on 04.10.16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Contact> contactList;


    public ContactsAdapter() {
        this.contactList = DBHelper.getInstance().getAllContacts();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item, parent, false);
        return new ContactsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ContactsViewHolder contactHolder = (ContactsViewHolder) holder;
        contactHolder.bind(DBHelper.getInstance().getAllContacts().get(position));
    }

    public Contact getItem(int position) {
        return DBHelper.getInstance().getAllContacts().get(position);
    }

    public void removeItem(int position) {
        DBHelper.getInstance().deleteContact(
                DBHelper.getInstance().getAllContacts().get(position).getId());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, DBHelper.getInstance().getAllContacts().size());
    }

    public void removeAll() {
        if(getItemCount() > 0) {
            DBHelper.getInstance().deleteAllContacts();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return DBHelper.getInstance().getAllContacts().size();
    }

    public List<Contact> getContacts() {
        return DBHelper.getInstance().getAllContacts();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        View mItemView;
        ImageView photo;
        ImageButton call;
        TextView name;
        TextView number;

        public ContactsViewHolder(final View itemView) {
            super(itemView);
            mItemView = itemView;
            photo = (ImageView) mItemView.findViewById(R.id.contacts_photo);
            call = (ImageButton) mItemView.findViewById(R.id.contacts_call);
            name = (TextView) mItemView.findViewById(R.id.contacts_name);
            number = (TextView) mItemView.findViewById(R.id.contacts_number);
        }

        public void bind(final Contact contact) {
            name.setText(MyValidator.displayName(contact));
            number.setText(contact.getPhoneNumber());
            if (contact.getPhoto() != null) {
                photo.setBackground(null);
                photo.setImageDrawable(ImageHelper.circleDrawableFromBytes(photo.getContext(), contact.getPhoto()));
            } else {
                photo.setImageDrawable(null);

                photo.setBackground(ImageHelper.gmailRoundWithColor(
                        MyValidator.displayName(contact),
                        Integer.parseInt(contact.getColor())));
            }
        }
    }
}

