package com.example.leaflet_android.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaflet_android.R;
import com.example.leaflet_android.entities.ChatMessage;

import java.util.List;

public class MessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER_MESSAGE = 0;
    private static final int VIEW_TYPE_CONTACT_MESSAGE = 1;
    private final String currentUsername;
    private final LayoutInflater mInflater;
    private List<ChatMessage> messages;

    // Constructor
    public MessagesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedLocal", Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.getSender().getUsername().equals(currentUsername)) {
            return VIEW_TYPE_USER_MESSAGE;
        } else {
            return VIEW_TYPE_CONTACT_MESSAGE;
        }
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
        } else {
            ContactMessageViewHolder contactHolder = (ContactMessageViewHolder) holder;
            contactHolder.bindMessage(currentMessage);
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

        public UserMessageViewHolder(View itemView) {
            super(itemView);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);  // replace with your actual TextView id
        }

        public void bindMessage(ChatMessage message) {
            tvUserMessage.setText(message.getContent());
        }
    }

    public class ContactMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvContactMessage;

        public ContactMessageViewHolder(View itemView) {
            super(itemView);
            tvContactMessage = itemView.findViewById(R.id.tvContactMessage);  // replace with your actual TextView id
        }

        public void bindMessage(ChatMessage message) {
            tvContactMessage.setText(message.getContent());
        }
    }
}
