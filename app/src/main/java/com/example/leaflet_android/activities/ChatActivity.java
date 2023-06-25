package com.example.leaflet_android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.R;
import com.example.leaflet_android.adapters.MessagesListAdapter;
import com.example.leaflet_android.databinding.ActivityChatBinding;
import com.example.leaflet_android.viewmodels.ChatViewModel;
import com.makeramen.roundedimageview.RoundedImageView;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ChatViewModel chatViewModel;
    private BroadcastReceiver messageReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Each time the viewModel changes we will refresh the contact list.


        Intent intent = getIntent(); // retrieve the Intent that started this Activity
        // retrieve the extras from the Intent
        String chatId = intent.getStringExtra("chatId");
        int contactLocalID = intent.getIntExtra("localID", 0);
        String contactDisplayName = getIntent().getStringExtra("contactDisplayName");
        String contactProfilePic = getIntent().getStringExtra("contactProfilePic");

        // log the retrieved values for debugging purposes
        Log.d("ChatActivityData", "chatId: " + chatId);
        Log.d("ChatActivityData", "localID: " + contactLocalID);
        Log.d("ChatActivityData", "contactDisplayname: " + contactDisplayName);


        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        RecyclerView lstMessages = binding.chatRecycler;
        final MessagesListAdapter adapter = new MessagesListAdapter(this, contactProfilePic);
        lstMessages.setAdapter(adapter);
        lstMessages.setLayoutManager(new LinearLayoutManager(this));

        // Capture the layout's TextView and set the string as its text
        binding.contactHeaderName.setText(contactDisplayName);

        ProgressBar loadBarChat = findViewById(R.id.loadBarChat);
        chatViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadBarChat.setVisibility(View.VISIBLE);
            } else {
                loadBarChat.setVisibility(View.GONE);
            }
        });

        chatViewModel.loadMessages(chatId);

        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // A notification message has been received. Here you can refresh your data.
                chatViewModel.loadMessages(chatId);
            }
        };

        // The user press on the send button and we wille execute this operation.
        binding.btnSendMessage.setOnClickListener(view -> {
            String message = binding.inputMessage.getText().toString();
            chatViewModel.sendMessage(chatId, message);
            binding.inputMessage.setText("");
        });

        // Let the user the option to move back to the contact list.
        binding.chatHeaderBack.setOnClickListener(view ->{
            Intent i = new Intent(this, ContactsActivity.class);
            startActivity(i);
        });

        // Let the user the option to delete the contact that he current chating with.
        binding.chatHeaderDelete.setOnClickListener(view ->{
            chatViewModel.deleteContactChat(chatId, contactLocalID);
            Intent i = new Intent(this, ContactsActivity.class);
            startActivity(i);
        });

        chatViewModel.fetchChat().observe(this, messages -> {
            Log.d("ContactsActivity", "Updating contacts in adapter: " + messages);
            adapter.setMessages(messages);
            lstMessages.scrollToPosition(messages.size() - 1);

        });

        // Observe error live data. If something goes wrong we pop an error to the user.
        chatViewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(ChatActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
