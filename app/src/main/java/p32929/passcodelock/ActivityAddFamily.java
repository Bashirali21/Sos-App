package p32929.passcodelock;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import p32929.passcodelock.databinding.ActivityAddContactBinding;
import p32929.passcodelock.db.AppDataBase;
import p32929.passcodelock.db.Contact;
import p32929.passcodelock.db.FamilyContact;
import p32929.passcodelock.viewModel.ViewModal;

public class ActivityAddFamily extends AppCompatActivity {
    ViewModal viewModal;
    ActivityAddContactBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModal = new ViewModelProvider(this).get(ViewModal.class);
        AppDataBase database = AppDataBase.getInstance(getApplicationContext());
        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(binding.EtName) || isEmpty(binding.EtNumber)) {
                    Toast.makeText(ActivityAddFamily.this, "Please fill Data", Toast.LENGTH_SHORT).show();
                }
                else{
                    FamilyContact table=new FamilyContact();
                    table.setName(binding.EtNumber.getText().toString());
                    table.setNumber(binding.EtNumber.getText().toString());
                    viewModal.insertFamily(table);

                }
            }
        });

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}