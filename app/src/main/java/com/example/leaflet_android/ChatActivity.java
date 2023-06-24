package com.example.leaflet_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaflet_android.adapters.MessagesListAdapter;
import com.example.leaflet_android.databinding.ActivityChatBinding;
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
        String contactLocalID = getIntent().getStringExtra("localID");

//        String contactDisplayname = getIntent().getStringExtra("contactDisplayname");
//        String contactProfilePic = getIntent().getStringExtra("contactProfilePic");

        ProgressBar loadBarChat = findViewById(R.id.loadBarChat);
        chatViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadBarChat.setVisibility(View.VISIBLE);
            } else {
                loadBarChat.setVisibility(View.GONE);
            }
        });

        chatViewModel.loadMessages(chatId);

//        // Capture the layout's TextView and set the string as its text
//        binding.contactHeaderName.setText(contactDisplayname);
//        // Capture the layout's TextView and set the string as its text
//        Glide.with(this)
//                .load(contactProfilePic)
//                .into((RoundedImageView) findViewById(R.id.imgProfileContactMessage));


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
        });

        // Observe error live data. If something goes wrong we pop an error to the user.
        chatViewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(ChatActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
