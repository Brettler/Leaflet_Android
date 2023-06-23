package com.example.leaflet_android;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaflet_android.adapters.ContactsListAdapter;
import com.example.leaflet_android.adapters.MessagesListAdapter;
import com.example.leaflet_android.databinding.ActivityChatBinding;
import com.example.leaflet_android.databinding.ActivityContactsBinding;
import com.example.leaflet_android.viewmodels.ChatViewModel;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        RecyclerView lstMessages = binding.chatRecycler;
        final MessagesListAdapter adapter = new MessagesListAdapter(this);
        lstMessages.setAdapter(adapter);
        lstMessages.setLayoutManager(new LinearLayoutManager(this));

        // Each time the viewModel changes we will refresh the contact list.

        String chatId = getIntent().getStringExtra("chatId");
        chatViewModel.loadMessages(chatId);

        chatViewModel.fetchChat().observe(this, messages -> {
            Log.d("ContactsActivity", "Updating contacts in adapter: " + messages);
            adapter.setMessages(messages);
        });
    }
}
