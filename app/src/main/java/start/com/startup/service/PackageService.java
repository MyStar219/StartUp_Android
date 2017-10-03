package start.com.startup.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;



/**
 * Created by Ognian on 30.08.2017.
 */

public class PackageService extends Service {
    public static String oldPackageName = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Service Start", Toast.LENGTH_SHORT).show();
        Log.e("Service Start===========> ", " Service Start ");

        final String str = "";
        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                String currentApp = "";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    UsageStatsManager usm = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
                    long time = System.currentTimeMillis();
                    List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                            time - 1000 * 1000, time);
                    if (appList != null && appList.size() > 0) {
                        SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                        for (UsageStats usageStats : appList) {
                            mySortedMap.put(usageStats.getLastTimeUsed(),
                                    usageStats);
                        }
                        if (mySortedMap != null && !mySortedMap.isEmpty()) {
                            currentApp = mySortedMap.get(
                                    mySortedMap.lastKey()).getPackageName();
                        }
                    }
                } else {
                    ActivityManager am = (ActivityManager) getBaseContext().getSystemService(ACTIVITY_SERVICE);
                    currentApp = am.getRunningTasks(1).get(0).topActivity.getPackageName();
                }

//                h.sendEmptyMessage(0);

                if(!oldPackageName.equals(currentApp)) {
                    oldPackageName = currentApp;

                    Message msg = Message.obtain(); // Creates an new Message instance
                    msg.obj = currentApp; // Put the string into Message, into "obj" field.
                    msg.setTarget(h); // Set the Handler
                    msg.sendToTarget();
                }

            }
        }, 2000, 2000);

        return START_STICKY;
    }

    Handler h = new Handler() {
        public void handleMessage(Message msg){
            String message = (String) msg.obj; //Extract the string from the Message

            if(!message.equals("")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
