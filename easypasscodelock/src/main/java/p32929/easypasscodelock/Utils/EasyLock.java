package p32929.easypasscodelock.Utils;

import static p32929.easypasscodelock.Activities.LockscreenActivity.statuses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import p32929.easypasscodelock.Activities.LockscreenActivity;
import p32929.easypasscodelock.Interfaces.ActivityChanger;

/**
 * Created by p32929 on 7/17/2018.
 */

public class EasyLock {
    private static ActivityChanger activityChanger;
    public static int backgroundColor = Color.parseColor("#019689");
    public static View.OnClickListener onClickListener;

    private static void init(Context context) {
        FayazSP.init(context);
        if (activityChanger == null) {
            activityChanger = new LockscreenActivity();
        }
    }

    public static void setPassword(Context context, Class activityClassToGo) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "set");
        context.startActivity(intent);

    }

    public static void changePassword(Context context, Class activityClassToGo) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "change");
        context.startActivity(intent);
    }

    public static void disablePassword(Context context, Class activityClassToGo) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "disable");
        context.startActivity(intent);
    }

    public static boolean checkPassword(Context context) {
        init(context);
        if (FayazSP.getString("password", null) != null) {
            Intent intent = new Intent(context, LockscreenActivity.class);
            intent.putExtra("passStatus", "check");
            context.startActivity(intent);
        }
        return statuses;
    }

    public static void setBackgroundColor(int backgroundColor) {
        EasyLock.backgroundColor = backgroundColor;
    }

    public static void forgotPassword(View.OnClickListener onClickListener) {
        EasyLock.onClickListener = onClickListener;
    }

}
