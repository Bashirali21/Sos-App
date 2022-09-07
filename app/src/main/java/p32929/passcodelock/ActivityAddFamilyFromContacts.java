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
import p32929.passcodelock.db.FamilyContact;
import p32929.passcodelock.viewModel.ViewModal;

public class ActivityAddFamilyFromContacts extends AppCompatActivity {
    ViewModal viewModal;////to access view model functions
    ActivityAddFamilyFromContactsBinding binding;//for access of components in our activity
    List<FamilyContact> phoneContacts;//
    List<FamilyContact> familyContactss;//
    PhoneContactsFamilyAdapter adapter;//for calling recyler view and send list to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFamilyFromContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModal = new ViewModelProvider(this).get(ViewModal.class);
        phoneContacts = new ArrayList<>();
        familyContactss = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityAddFamilyFromContacts.this, new String[]{Manifest.permission.READ_CONTACTS}, 11);
            }
            else{
                getData();
            }

        }


//        getData();


    }

    public void getData() {
        viewModal.getFamilyContacts().observe(this, new Observer<List<FamilyContact>>() {
            @Override
            public void onChanged(List<FamilyContact> familyContacts) {
              setData(familyContacts);
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
    public void setData(List<FamilyContact> familyContacts){
        Log.d("numms1",familyContacts+"");
        familyContactss=familyContacts;
        Log.d("numms",familyContactss+"");
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
                        FamilyContact data=new FamilyContact();
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
        adapter = new PhoneContactsFamilyAdapter(getApplicationContext(), phoneContacts,familyContactss );
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
                Toast.makeText(ActivityAddFamilyFromContacts.this, "You must enable permission to access contacts", Toast.LENGTH_SHORT).show();
                return;
            }

        }

    }
}

