package p32929.passcodelock;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import p32929.passcodelock.databinding.ActivityAddFamilyFromContactsBinding;
import p32929.passcodelock.db.Contact;
import p32929.passcodelock.viewModel.ViewModal;

public class ActivityAddFromContacts extends AppCompatActivity {
    ViewModal viewModal;
    ActivityAddFamilyFromContactsBinding binding;
    List<Contact> phoneContacts;
    List<Contact> Contactss;
    PhoneContactsEmergencyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFamilyFromContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModal = new ViewModelProvider(this).get(ViewModal.class);
        phoneContacts = new ArrayList<>();
        Contactss = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityAddFromContacts.this, new String[]{Manifest.permission.READ_CONTACTS}, 11);
            }
            else{
                getData();
            }

        }


//        getData();


    }

    public void getData() {
        viewModal.getAllContact().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> Contacts) {
                setData(Contacts);
            }
        });
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getContactList();
            }
        }, 1000);


    }
    public void setData(List<Contact> Contacts){
        Log.d("numms1",Contacts+"");
        Contactss=Contacts;
        Log.d("numms",Contactss+"");
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    if (!mobileNoSet.contains(number)) {
                        Contact data=new Contact();
                        data.setName(name);
                        data.setNumber(number);
                        phoneContacts.add(data);
                        mobileNoSet.add(number);
//                        Toast.makeText(this, " onCreaterrView  Phone Number: name = " + name
//                                + " No = " + number, Toast.LENGTH_SHORT).show();
                    }
                }
            } finally {
                cursor.close();
            }
        }
        binding.recPhoneContacts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new PhoneContactsEmergencyAdapter(getApplicationContext(), phoneContacts,Contactss );
        binding.recPhoneContacts.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getData();

            } else {
                Toast.makeText(ActivityAddFromContacts.this, "You must enable permission to access contacts", Toast.LENGTH_SHORT).show();
                return;
            }

        }

    }
}
