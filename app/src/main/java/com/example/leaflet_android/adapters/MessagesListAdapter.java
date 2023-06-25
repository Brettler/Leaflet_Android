package com.example.leaflet_android.adapters;

import static android.content.Context.MODE_PRIVATE;
import static com.example.leaflet_android.LeafletApp.context;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.leaflet_android.AppDB;
import com.example.leaflet_android.R;
import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.entities.ChatMessage;
import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.entities.Sender;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER_MESSAGE = 0;
    private static final int VIEW_TYPE_CONTACT_MESSAGE = 1;
    private final String currentUsername;
    private final String contactProfilePic;
    private Executor executor = Executors.newSingleThreadExecutor();

    private final LayoutInflater mInflater;
    private List<ChatMessage> messages;

    // Constructor
    public MessagesListAdapter(Context context, String contactProfilePic) {
        mInflater = LayoutInflater.from(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedLocal", Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        this.contactProfilePic = contactProfilePic;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);

        if (message != null) {
            Sender sender = message.getSender();
            if (sender != null) {
                String username = sender.getUsername();
                if (username != null && username.equals(currentUsername)) {
                    return VIEW_TYPE_USER_MESSAGE;
                }
            }
        }
        // If anything is null, default to the contact message type.
        return VIEW_TYPE_CONTACT_MESSAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER_MESSAGE) {
            View itemView = mInflater.inflate(R.layout.item_user_message_container, parent, false);
            return new UserMessageViewHolder(itemView);
        } else {
            View itemView = mInflater.inflate(R.layout.item_contact_message_container, parent, false);
            return new ContactMessageViewHolder(itemView);
        }
    }

    // onBindViewHolder will connect the information we recive into the new view we created in the
    // method above. This way we can recycle the object instead of create new object for each contact.
    // Because each contact in the friend list need to have the same information.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage currentMessage = messages.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_USER_MESSAGE) {
            UserMessageViewHolder userHolder = (UserMessageViewHolder) holder;
            userHolder.bindMessage(currentMessage);
            userHolder.bindTimeDateMessage(currentMessage);
        } else {
            ContactMessageViewHolder contactHolder = (ContactMessageViewHolder) holder;
            contactHolder.bindMessage(currentMessage);
            contactHolder.bindContactProfilePic();
            contactHolder.bindTimeDateMessage(currentMessage);
        }
    }

    public void setMessages(List<ChatMessage> c) {
        messages = c;
        // Responsible to notify the adapter he need to render the data.
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    // Getter - return the contact list.

    public List<ChatMessage> getMessages() {
        return messages;
    }


    public class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserMessage;
        private final TextView tvMessageTime;

        public UserMessageViewHolder(View itemView) {
            super(itemView);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
        }

        public void bindMessage(ChatMessage message) {
            tvUserMessage.setText(message.getContent());
            Log.d("MessageAdapter", "Message Sent by You: " + message.getContent());
        }

        public void bindTimeDateMessage(ChatMessage message) {
            //tvMessageTime.setText(message.getCreated());

            try {
                // This is your original date string from the server
                String originalDateFormat = message.getCreated();

                // Create a DateFormat for parsing the date
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                originalFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Because the original date string is in UTC

                // Parse the original date string into a Date object
                Date date = originalFormat.parse(originalDateFormat);

                // Create a new DateFormat for formatting the date into the new format
                SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                newFormat.setTimeZone(TimeZone.getDefault()); // You might want to adjust this depending on your requirements

                // Format the Date object into the new format
                String newDateString = newFormat.format(date);

                // Log or use the new date string
                Log.d("MessageAdapter", "New date string: " + newDateString);

                tvMessageTime.setText(newDateString);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public class ContactMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvContactMessage;
        private final ImageView ivContactProfilePic;
        private final TextView tvDateTimeMSG;

        public ContactMessageViewHolder(View itemView) {
            super(itemView);
            tvContactMessage = itemView.findViewById(R.id.tvContactMessage);
            ivContactProfilePic = itemView.findViewById(R.id.imgProfileContactMessage);
            tvDateTimeMSG = itemView.findViewById(R.id.contactMessageDateTime);
        }

        public void bindMessage(ChatMessage message) {
            tvContactMessage.setText(message.getContent());
            Log.d("MessageAdapter", "Message Sent by the contact: " + message.getContent());

        }



        public void bindContactProfilePic() {
            Glide.with(context)
                    .load(contactProfilePic)
                    .into(ivContactProfilePic);

        }



        public void bindTimeDateMessage(ChatMessage message) {
            try {
                // This is your original date string from the server
                String originalDateFormat = message.getCreated();

                // Create a DateFormat for parsing the date
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                originalFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Because the original date string is in UTC

                // Parse the original date string into a Date object
                Date date = originalFormat.parse(originalDateFormat);

                // Create a new DateFormat for formatting the date into the new format
                SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                newFormat.setTimeZone(TimeZone.getDefault()); // You might want to adjust this depending on your requirements

                // Format the Date object into the new format
                String newDateString = newFormat.format(date);

                // Log or use the new date string
                Log.d("MessageAdapter", "New date string: " + newDateString);

                tvDateTimeMSG.setText(newDateString);


            } catch (ParseException e) {
                e.printStackTrace();
            }


            Log.d("MessageAdapter", "Time and date of the message sent from the server: " + message.getCreated());

        }
    }
}
