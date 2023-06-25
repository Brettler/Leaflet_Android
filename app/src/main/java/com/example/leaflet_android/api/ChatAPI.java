package com.example.leaflet_android.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.leaflet_android.LeafletApp;
import com.example.leaflet_android.chat.ContentMessageBody;
import com.example.leaflet_android.chat.SendMessageObject;
import com.example.leaflet_android.dao.ChatMessageDao;
import com.example.leaflet_android.dao.ContactDao;
import com.example.leaflet_android.entities.ChatMessage;
import com.example.leaflet_android.entities.Contact;
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
    private ChatMessageDao chatDao;
    private ContactDao contactDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private MutableLiveData<Boolean> isDeletionComplete = new MutableLiveData<>(false);


    public ChatAPI(MutableLiveData<List<ChatMessage>> messageListData, ChatMessageDao chatDao, ContactDao contactDao) {
        this.messageListData = messageListData;
        this.chatDao = chatDao;
        this.contactDao = contactDao;
        refreshRetrofitInstance();
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
    // Getter for isDeletionComplete
    public LiveData<Boolean> getDeletionStatus() {
        return isDeletionComplete;
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
                        chatDao.insertAll(messages);
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

    // Send message to a contact.
    public void sendMessageRequest(String token, String contactId, ContentMessageBody messageBody) {

        Call<SendMessageObject> call = webServiceAPI.sendMessageRequest("Bearer " + token, contactId, messageBody);
        call.enqueue(new Callback<SendMessageObject>() {
            @Override
            public void onResponse(Call<SendMessageObject> call, Response<SendMessageObject> response) {
                if(response.isSuccessful() && response.body() != null) {
                    // Save the contact to local DB
                    SendMessageObject myInfo = response.body();
                    Log.d("SendMessageObject", "Adding contact response: " + myInfo);

                } else {
                    Log.e("SendMessageObject", "Unsuccessful response: " + response.code());
                    if (response.code() == 404) {
                        errorLiveData.postValue("Message didn't sent!");

                    } else if(response.code() == 401) {
                        errorLiveData.postValue("You are using invalid token. Try to log in again.");
                    }
                    else if(response.code() == 403) {
                        errorLiveData.postValue("You try to send message but without your private token. please check your phone for any security settings.");
                    }
                }
            }
            @Override
            public void onFailure(Call<SendMessageObject> call, Throwable t) {
                errorLiveData.postValue("Network Error. Please provide better wifi connection.");
                // Handle failure
                Log.e("requestAddContact", "Request failed: " + t.getMessage()); // Log the failure reason

            }
        });
    }

    public void deleteContactChat(String token, String contactId, int localID) {

        Call<Void> call = webServiceAPI.deleteContactChat("Bearer " + token, contactId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    // Update deletion status
                    isDeletionComplete.postValue(true);
                    // Save the contact to local DB
                    int deleteCode = response.code();
                    Log.d("", "Successfully Delete the contact." + deleteCode);

                    // Delete contact and its messages from local DB
                    new Thread(() -> {
                        Contact contact = contactDao.get(localID);
                        if (contact != null) {
                            contactDao.delete(contact);
                        }
                    }).start();
                } else {
                    Log.e("SendMessageObject", "Unsuccessful response: " + response.code());
                    if (response.code() == 404) {
                        errorLiveData.postValue("This contact is not found in the server.");

                    } else if(response.code() == 500) {
                        errorLiveData.postValue("Error while deleting chat. please try again later.");
                    }
                    else if(response.code() == 403) {
                        errorLiveData.postValue("You are using invalid token. Try to log in again.");
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorLiveData.postValue("Network Error. Please provide better wifi connection.");
                // Handle failure
                Log.e("requestAddContact", "Request failed: " + t.getMessage()); // Log the failure reason

            }
        });
    }

}

