package p32929.passcodelock;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import p32929.passcodelock.databinding.ActivityOptionBinding;

public class OptionActivity extends AppCompatActivity {
ActivityOptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionActivity.this,AllFamilyContactsActivity.class));
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(OptionActivity.this,AllContactsActivity.class));
            }
        });
    }
}