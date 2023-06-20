package com.example.leaflet_android.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.leaflet_android.R;
import com.example.leaflet_android.entities.Contact;

import java.util.List;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder> {

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProfilePic;
        private final TextView tvDisplayName;
        private final TextView tvLastMessage;

        private ContactViewHolder(View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvDisplayName = itemView.findViewById(R.id.tvDisplayName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }

    private final LayoutInflater mInflater;
    private List<Contact> contacts;

    // Constructor
    public ContactsListAdapter(Context context) {mInflater = LayoutInflater.from(context);}

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }
    // onBindViewHolder will connect the information we recive into the new view we created in the
    // method above. This way we can recycle the object instead of create new object for each contact.
    // Because each contact in the friend list need to have the same information.
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        if (contacts != null) {
            final Contact current = contacts.get(position);
            holder.tvLastMessage.setText(current.getLastMessage());
            holder.tvDisplayName.setText(current.getDisplayName());
            holder.ivProfilePic.setImageResource(current.getProfilePic());
        }
    }

    // Setter - set the information of each contact.
    public void setContacts(List<Contact> c) {
        contacts = c;
        // Responsible to notify the adapter he need to render the data.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            return contacts.size();
        } else {
            return 0;
        }
    }

    // Getter - return the contact list.

    public List<Contact> getContacts() {
        return contacts;
    }
}
