package com.example.leaflet_android.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.dao.ChatMessageDao;
import com.example.leaflet_android.entities.ChatMessage;
import com.example.leaflet_android.settings.AppSettings;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {

    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private MutableLiveData<List<ChatMessage>> messageListData;
    private ChatMessageDao dao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void refreshRetrofitInstance() {
        AppSettings appSettings = new AppSettings(LeafletApp.context);
        retrofit = new Retrofit.Builder()
                .baseUrl(appSettings.getServerIpAddress())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public ChatAPI(MutableLiveData<List<ChatMessage>> messageListData, ChatMessageDao dao) {
        this.messageListData = messageListData;
        this.dao = dao;
        refreshRetrofitInstance();
    }


    public void getMessages(MutableLiveData<List<ChatMessage>> messagesLiveData, String token, String contactId) {
        Call<List<ChatMessage>> call = webServiceAPI.getMessages("Bearer " + token, contactId);
        call.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful()) {
                    List<ChatMessage> messages = response.body();
                    Log.d("ContactAPI", "requestAllContacts response: " + messages);
                    messagesLiveData.postValue(messages);
                    new Thread(() -> {
                        dao.insertAll(messages);
                    }).start();
                } else {
                    Log.e("requestAllContacts", "Unsuccessful response: " + response.code()); // Log the error code
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                Log.e("requestAllContacts", "Request failed: " + t.getMessage()); // Log the failure reason
            }
        });
    }

}

