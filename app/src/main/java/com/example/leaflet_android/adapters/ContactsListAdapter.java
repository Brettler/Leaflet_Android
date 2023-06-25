package com.example.leaflet_android.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.leaflet_android.activities.ChatActivity;
import com.example.leaflet_android.R;
import com.example.leaflet_android.entities.Contact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

            // When a contact is clicked by the user we want to move to the chat with this contact.
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Contact clickedContact = contacts.get(position);
                    // This is the contact that was clicked. You can start ChatActivity here.
                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    // Adding the chatID of the chat with the contact the user chose so we can use
                    // the api to retrive the messages with this contact.
                    intent.putExtra("chatId", clickedContact.getId());
                    intent.putExtra("localID", clickedContact.getLocalID());
                    // Also add the contact DisplayName so we can present it in the chat for the user.
                    String contactDisplayName = clickedContact.getUser().getDisplayName();
                    String contactProfilePic = clickedContact.getUser().getProfilePic();
                    intent.putExtra("contactDisplayName", contactDisplayName);
                    intent.putExtra("contactProfilePic", contactProfilePic);
                    v.getContext().startActivity(intent);
                }
            });
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
            if (current.getLastMessage() != null) {  // Added null check here
                String lastMessage = current.getLastMessage().getContent();
                if (lastMessage.length() > 15) {  // Check if the message is longer than 15 characters
                    lastMessage = lastMessage.substring(0, 15) + "...";  // If it is, take the first 15 characters and add '...' at the end
                }
                String lastMessageTime = current.getLastMessage().getCreated();
                // This is your original date string from the server
                String originalDateFormat = lastMessageTime;

                // Create a DateFormat for parsing the date
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                originalFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Because the original date string is in UTC

                // Parse the original date string into a Date object
                Date date = null;
                try {
                    date = originalFormat.parse(originalDateFormat);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                // Create a new DateFormat for formatting the date into the new format
                SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                newFormat.setTimeZone(TimeZone.getDefault()); // You might want to adjust this depending on your requirements

                // Format the Date object into the new format
                String newDateString = newFormat.format(date);

                String combineTogether = lastMessage + "    [Time: " +newDateString + "]";
                holder.tvLastMessage.setText(combineTogether);
            } else {
                holder.tvLastMessage.setText(""); // Or any other placeholder text
            }            holder.tvDisplayName.setText(current.getUser().getDisplayName());

            // Use Glide to load the image
            Glide.with(holder.ivProfilePic.getContext())
                    .load(current.getUser().getProfilePic())
                    .into(holder.ivProfilePic);
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
