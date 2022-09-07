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
    Boolean isAllFabsVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list = new ArrayList<>();
        getData();

        binding.addAlarmFab.setVisibility(View.GONE);
        binding.addPersonFab.setVisibility(View.GONE);
        binding.addAlarmActionText.setVisibility(View.GONE);
        binding.addPersonActionText.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are
        // invisible
        isAllFabsVisible = false;

        // Set the Extended floating action button to
        // shrinked state initially
        binding.addFab.shrink();

        // We will make all the FABs and action name texts
        // visible only when Parent FAB button is clicked So
        // we have to handle the Parent FAB button first, by
        // using setOnClickListener you can see below
        binding.addFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs VISIBLE.
                            binding.addAlarmFab.show();
                            binding.addPersonFab.show();
                            binding.addAlarmActionText.setVisibility(View.VISIBLE);
                            binding.addPersonActionText.setVisibility(View.VISIBLE);

                            // Now extend the parent FAB, as
                            // user clicks on the shrinked
                            // parent FAB
                            binding.addFab.extend();

                            // make the boolean variable true as
                            // we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = true;
                        } else {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs GONE.
                            binding.addAlarmFab.hide();
                            binding.addPersonFab.hide();
                            binding.addAlarmActionText.setVisibility(View.GONE);
                            binding.addPersonActionText.setVisibility(View.GONE);

                            // Set the FAB to shrink after user
                            // closes all the sub FABs
                            binding.addFab.shrink();

                            // make the boolean variable false
                            // as we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = false;
                        }
                    }
                });

        // below is the sample action to handle add person
        // FAB. Here it shows simple Toast msg. The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        binding.addPersonActionText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(AllFamilyContactsActivity.this,ActivityAddFamilyFromContacts.class));
                    }
                });

        // below is the sample action to handle add alarm
        // FAB. Here it shows simple Toast msg The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        binding.addAlarmActionText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(AllFamilyContactsActivity.this,ActivityAddFamily.class));
                    }
                });
//        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AllFamilyContactsActivity.this,ActivityAddFamily.class));
//            }
//        });

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