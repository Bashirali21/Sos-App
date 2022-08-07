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
import p32929.passcodelock.db.FamilyContact;
import p32929.passcodelock.viewModel.ViewModal;

public class AllFamilyContactsActivity extends AppCompatActivity {
    ActivityAllContactsBinding binding;
    List<FamilyContact> list;
    AlFamilyContactsAdapter adapter;
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
                startActivity(new Intent(AllFamilyContactsActivity.this,ActivityAddFamily.class));
            }
        });

    }

    public void getData() {
        model = new ViewModelProvider(this).get(ViewModal.class);
        model.getFamilyContacts().observe(this, new Observer<List<FamilyContact>>() {
            @Override
            public void onChanged(List<FamilyContact> contacts) {
                Log.d("listItems",contacts+"");
                binding.rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new AlFamilyContactsAdapter(getApplicationContext(), contacts);
                binding.rec.setAdapter(adapter);
            }
        });

    }



}