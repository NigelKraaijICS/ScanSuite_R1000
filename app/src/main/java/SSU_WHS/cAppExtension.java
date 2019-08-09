package SSU_WHS;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class cAppExtension extends Application {
    public static Context context;
    public static FragmentActivity fragmentActivity;
    public static FragmentManager fragmentManager;
    public static Activity activity;

//    public static cAppExtension getInstance() {
//        return instance;
//    }
//
//    public static cAppExtension getContext(){
//        return instance;
//        // or return instance.getApplicationContext();
//    }
//
//
//
//    @Override
//    public void onCreate() {
//
//
//        instance = this;
//         super.onCreate();
//    }
}
