package p32929.passcodelock;

import static p32929.passcodelock.service.SoundService.ring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Contacts;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import p32929.easypasscodelock.Utils.EasyLock;
import p32929.easypasscodelock.Utils.LockscreenHandler;
import p32929.passcodelock.databinding.ActivityTestBinding;
import p32929.passcodelock.db.Contact;
import p32929.passcodelock.db.FamilyContact;
import p32929.passcodelock.service.SoundService;
import p32929.passcodelock.viewModel.ViewModal;

public class TestActivity extends LockscreenHandler implements ChangeText {
    ViewModal viewModal;
    ActivityTestBinding binding;
    int messagetype = 0;
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file
    String latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;
    int FamilyMessagePermission = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setLocationPermission();
        SharedPreferences saveinfo = getSharedPreferences("hasUser", MODE_PRIVATE);
        if (saveinfo.getBoolean("user", false)) {
            EasyLock.checkPassword(this);
        } else {
            SharedPreferences.Editor edit = saveinfo.edit();
            edit.putBoolean("user", true);
            edit.apply();
            EasyLock.setPassword(this, TestActivity.class);
            finish();
        }
        viewModal = new ViewModelProvider(this).get(ViewModal.class);
        binding.btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TestActivity.this, OptionActivity.class));
            }
        });
        binding.btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       sendSMSMessage();
                    }
                }, 2000);
            }
        });
        binding.btnWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(TestActivity.this, SoundService.class));
                final Handler handler = new Handler(Looper.getMainLooper());
                EasyLock.checkPassword(TestActivity.this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(ring.isPlaying()){
                            binding.btnWhistle.setText("pause");
                        }
                        else{
                            binding.btnWhistle.setText("play");
                        }
                    }
                }, 1000);

            }
        });
        binding.btnReachHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFamilyMessage();
            }
        });
    }


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    protected void sendSMSMessage() {
        viewModal.getAllCourses().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if (contacts.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Contact", Toast.LENGTH_SHORT).show();
                } else {
                    SmsManager smsManager = SmsManager.getDefault();
                    for (Contact data : contacts) {
                        try {
                            smsManager.sendTextMessage(data.getNumber(), null, longitTextView + latitudeTextView, null, null);
                        } catch (Exception ex) {

                        }

                    }
                    Toast.makeText(TestActivity.this, "send succesfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                return;
            }

        }
        if (requestCode == FamilyMessagePermission) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                return;
            }

        }
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
                setMessagingPermission();
            }
        }

    }


    public void sendFamilyMessage() {
        viewModal.getFamilyContacts().observe(this, new Observer<List<FamilyContact>>() {
            @Override
            public void onChanged(List<FamilyContact> familyContacts) {
                if (familyContacts.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Contact", Toast.LENGTH_SHORT).show();
                } else {
                    SmsManager smsManager = SmsManager.getDefault();
                    for (FamilyContact data : familyContacts) {
                        try {
                            smsManager.sendTextMessage(data.getNumber(), null, "i am home", null, null);
                        } catch (Exception ex) {

                        }

                    }
                    Toast.makeText(TestActivity.this, "send succesfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //// for getting Location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView = ("Latitude: " + location.getLatitude() + "");
                            longitTextView = ("Longitude: " + location.getLongitude() + "");
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView = ("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView = ("Longitude: " + mLastLocation.getLongitude() + "");

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setLocationPermission() {
        if (!checkPermissions()) {
            requestPermissions();
        }
        else{
            getLastLocation();
        }

    }

    public void setMessagingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(TestActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        }
    }

    @Override
    public void change(Boolean status) {
        if(status){
            binding.btnWhistle.setText("Playing");
        }
        else{
            binding.btnWhistle.setText("Stop");
        }
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            Toast.makeText(TestActivity.this, "started", Toast.LENGTH_SHORT).show();
         String message=extras.getString("status","2");
         if(message=="1"){
             binding.btnWhistle.setText("play");
         }
         if(message=="0"){
             binding.btnWhistle.setText("stop");
         }

        }
    }

}
