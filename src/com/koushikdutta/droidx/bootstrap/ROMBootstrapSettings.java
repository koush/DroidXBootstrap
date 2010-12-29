package com.koushikdutta.droidx.bootstrap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import android.util.Log;

public class ROMBootstrapSettings {
	private static final String TAG = "DXB/ROMBootstrapSettings";
	
	private static final File SETTINGS_FILE = new File("/system/etc/DroidXBootstrap.cfg");
	private static final String RESTART_ADB_KEY = "restart_adb";
	private static final String INSTALL_HIJACK_KEY = "install_hijack";
	private static final String INSTALL_RECOVERY_KEY = "install_recovery";
	
	private boolean mRestartAdb = true;
	private boolean mInstallHijack = true;
	private boolean mInstallRecovery = true;
	
	public ROMBootstrapSettings() {
		if(SETTINGS_FILE.exists()) {
			Log.d(TAG, "Found settings file, parsing");
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(SETTINGS_FILE));
				
				String line = null;
				
				while((line = br.readLine()) != null) {
					if(line.length() > 0 && line.charAt(0) == '#') {
						// ignore # characters
						continue;
					}
					
					// split on =
					String[] parts = line.split("=");
					
					// ignore malformed shit
					if(parts.length == 2) {
						String key = parts[0].trim().toLowerCase();
						boolean value = Boolean.valueOf(parts[1].trim());
						
						if(key.equals(RESTART_ADB_KEY)) {
							Log.d(TAG, "Setting [RestartAdb] to [" + (value ? "true" : "false") + "]");
							mRestartAdb = value;
						} else if(key.equals(INSTALL_HIJACK_KEY)) {
							Log.d(TAG, "Setting [InstallHijack] to [" + (value ? "true" : "false") + "]");
							mInstallHijack = value;
						} else if(key.equals(INSTALL_RECOVERY_KEY)) {
							Log.d(TAG, "Setting [InstallRecovery] to [" + (value ? "true" : "false") + "]");
							mInstallRecovery = value;
						}
					}
				}
				
				br.close();
			} catch(Exception e) {
				Log.e(TAG, "Error parsing file", e);
			}
		}
	}
	
	public boolean restartAdb() { return mRestartAdb; }
	public boolean installHijack() { return mInstallHijack; }
	public boolean installRecovery() { return mInstallRecovery; }
}
