package fpoly.anhnvph32739.duanmau;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ConfigNotification extends Application {
    public final static String CHANNEL_ID = "FPTPolytechninc";

    @Override
    public void onCreate() {
        super.onCreate();
        config(); // đăng kí channel id cho thiết bị > android 8
    }
    public void config(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
