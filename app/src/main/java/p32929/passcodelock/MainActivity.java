package p32929.passcodelock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import p32929.easypasscodelock.Utils.EasyLock;
import p32929.easypasscodelock.Utils.LockscreenHandler;

public class MainActivity extends LockscreenHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    startActivity(new Intent(MainActivity.this,TestActivity.class));
    finish();
        // EasyLock.setBackgroundColor(Color.BLUE);

    }


}
