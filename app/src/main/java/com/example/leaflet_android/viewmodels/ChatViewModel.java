package com.example.leaflet_android.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaflet_android.entities.ChatMessage;
import com.example.leaflet_android.repositories.MessagesRepository;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private static final String TAG = "ChatViewModel";
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private MessagesRepository chatRepo;
    private LiveData<List<ChatMessage>> messages;

    public ChatViewModel() {
        Log.d(TAG, "Creating new ChatViewModel");
        chatRepo = new MessagesRepository();
        // Requesting all the contacts of the user
        messages = chatRepo.getAll();
        Log.d(TAG, "Messages from repository: " + messages.getValue());

    }

    // get expose the contacts varaible to the classes who use it.
    public LiveData<List<ChatMessage>> fetchChat() {
        Log.d(TAG, "Getting Messages: " + messages.getValue());
        return messages;
    }

    public void loadMessages(String chatId) {
        chatRepo.loadMessages(chatId, isLoading);
    }

    // Need to implement them with the API.

    public void sendMessage (String chatId, String message) {
        Log.d(TAG, "Adding message: " + message);
        chatRepo.sendMessage(chatId ,message);
    }

        public void deleteContactChat(String chatId, String contactLocalID) {
            chatRepo.deleteContactChat(chatId, contactLocalID);
    }


    public LiveData<String> getErrorLiveData() {
        LiveData<String> errorLiveData = chatRepo.getErrorLiveData();
        Log.d(TAG, "Getting error LiveData: " + errorLiveData.getValue());
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


}

