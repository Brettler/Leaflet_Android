package com.example.leaflet_android;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.leaflet_android.adapters.ContactsListAdapter;
import com.example.leaflet_android.api.ContactAPI;
import com.example.leaflet_android.databinding.ActivityContactsBinding;
import com.example.leaflet_android.databinding.ActivityRegisterBinding;
import com.example.leaflet_android.entities.Contact;
import com.example.leaflet_android.viewmodels.ContactsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;

    private ContactsViewModel viewModel;
    private AppDB db;
    private ContactDao contactDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contacts);


        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        RecyclerView lstContacts = binding.lstContacts;
        final ContactsListAdapter adapter = new ContactsListAdapter(this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));



        // We need to change it later to switch from '.allowMainThreadQueries()' to operate thread.
        db = Room.databaseBuilder(LeafletApp.context, AppDB.class, "ContactsDB")
                .allowMainThreadQueries()
                .build();
        contactDao = db.contactDao();

        // Now we want to retrieve the contactList from the local DB until the server will respond if we have any new friends that added us or sent as messages.



        // When the user click on the add friend button he can add a friend.
        FloatingActionButton btnAdd = binding.btnAdd;
        btnAdd.setOnClickListener(view -> {
            Intent i = new Intent(this, AddActivity.class);
            startActivity(i);
        });


        // Each time the viewModel changes we will refresh the contact list.
        viewModel.get().observe(this, contacts -> {
            adapter.setContacts(contacts);
        });


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token for each user that install and use the app.
            // When we retrive the token of the user app and send it to the server, the server will hold
            // this token with the unique user name. - can be done by key as a user name and value is the app token.
            String newToken = task.getResult();

//            // Log and toast
//            String msg = getString(R.string.msg_token_fmt, token);
//            Log.d(TAG, msg);
//            Toast.makeText(ContactsActivity.this, msg, Toast.LENGTH_SHORT).show();
        });
    }
}