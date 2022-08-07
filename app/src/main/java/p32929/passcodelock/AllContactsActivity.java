package p32929.passcodelock;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import p32929.passcodelock.databinding.ActivityAllContactsBinding;
import p32929.passcodelock.db.Contact;
import p32929.passcodelock.viewModel.ViewModal;

public class AllContactsActivity extends AppCompatActivity {
    ActivityAllContactsBinding binding;
    List<Contact> list;
    AllContactsAdapter adapter;
    ViewModal model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list = new ArrayList<>();
        getData();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllContactsActivity.this,AddContactActivity.class));
            }
        });

    }

    public void getData() {
        model = new ViewModelProvider(this).get(ViewModal.class);
        model.getAllCourses().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                Log.d("listItems",contacts+"");
                binding.rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new AllContactsAdapter(getApplicationContext(), contacts);
                binding.rec.setAdapter(adapter);
            }
        });

    }



}