package start.com.startup.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import start.com.startup.service.PackageService;

/**
 * Created by Ognian on 30.08.2017.
 */

public class AutoStartReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, PackageService.class);
        context.startService(startServiceIntent);
    }
}
