package com.koushikdutta.droidx.bootstrap;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BootService extends Service {
	
	private static final String TAG = "DXB/BootService";

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        // wait to make sure the system is sane.
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                
                String filesDir = getFilesDir().getAbsolutePath();
                String busybox = filesDir + "/busybox";
                String adbd = filesDir + "/adbd";
                
                StringBuilder command = new StringBuilder();
                
                // prevent recovery from booting here
                Log.d(TAG, "Removing recovery_mode trigger");
                command.append("rm /data/.recovery_mode ; ");
                
                ROMBootstrapSettings settings = new ROMBootstrapSettings();
                
                if(settings.restartAdb()) {
                    // restart adbd as root
                	Log.d(TAG, "Restarting ADB as Root");
                	command.append(busybox + " mount -orw,remount / ; ");
                	command.append("mv /sbin/adbd /sbin/adbd.old ; ");
                	command.append(busybox + " cp " +  adbd + " /sbin/adbd ; ");
                	command.append(busybox + " mount -oro,remount / ; ");
                	command.append(busybox + " kill $(ps | " + busybox + " grep adbd) ;");
                }
                
                try {
                    Helper.runSuCommand(BootService.this, command.toString());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally
                {
                    stopSelf();
                }
            }
        },
        30000);
    }
    
    Handler mHandler = new Handler();
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
