package com.example.leaflet_android.repositories;

import static android.content.Context.MODE_PRIVATE;
import static com.example.leaflet_android.LeafletApp.context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.leaflet_android.AppDB;
import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.api.ChatAPI;
import com.example.leaflet_android.dao.ChatMessageDao;
import com.example.leaflet_android.entities.ChatMessage;

import java.util.List;

public class MessagesRepository {
    private ChatMessageDao dao;
    private MessagesRepository.MessageListData messageListData;
    private ChatAPI chatAPI;

    public MessagesRepository(){
        AppDB db = AppDB.getDatabase(context);
        dao = db.chatMessageDao();

        messageListData = new MessagesRepository.MessageListData();
        chatAPI = new ChatAPI(messageListData, dao);

        // If we
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshRetrofitInstance();
            }
        }, new IntentFilter("IP_ADDRESS_CHANGED"));
    }

    public void refreshRetrofitInstance() {
        chatAPI.refreshRetrofitInstance();
    }

    public void loadMessages(String chatId) {
        new Thread(() -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("sharedLocal", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "");
            chatAPI.getMessages(messageListData, token, chatId);
        }).start();
    }

    class MessageListData extends MutableLiveData<List<ChatMessage>> {
        public MessageListData() {
            super();
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                List<ChatMessage> messagesFromDB = dao.index();
                Log.d("ContactListData", "Loaded contacts from database: " + messagesFromDB);
                postValue(messagesFromDB);
            }).start();
        }

    }

    // contactListData is type mutable live data therefore we can return it as livedata.
    public LiveData<List<ChatMessage>> getAll() {
        return messageListData;
    }

//    public void addContact (final String username, String token) {
//        chatAPI.requestAddContact(username, token);
//    }

    public LiveData<String> getErrorLiveData() {
        return chatAPI.getErrorLiveData();
    }
}
