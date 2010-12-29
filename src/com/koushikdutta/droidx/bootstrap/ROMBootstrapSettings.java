package com.koushikdutta.droidx.bootstrap;

import java.io.File;
import java.io.FileInputStream;

import android.util.Log;

import org.json.JSONObject;

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
			
			FileInputStream f = null;
			String data = "";
			
			try {
				// read file into a string
			    byte[] buffer = new byte[(int) SETTINGS_FILE.length()];
				f = new FileInputStream(SETTINGS_FILE);
				f.read(buffer);
				data = new String(buffer);
			} catch(Exception e) {
				Log.e(TAG, "Error reading settings file", e);
			} finally {
				// ensure the stream is closed
				if(f != null) try { f.close(); } catch(Exception ignored) { }
			}
			
			try {
				// parse as JSON
				JSONObject json = new JSONObject(data);
				
				if(json.has(RESTART_ADB_KEY)) {
					mRestartAdb = json.optBoolean(RESTART_ADB_KEY, true);
					Log.d(TAG, "Setting [RestartAdb] to [" + (mRestartAdb ? "true" : "false") + "]");
				}
				if(json.has(INSTALL_HIJACK_KEY)) {
					mInstallHijack = json.optBoolean(INSTALL_HIJACK_KEY, true);
					Log.d(TAG, "Setting [InstallHijack] to [" + (mInstallHijack ? "true" : "false") + "]");
				}
				if(json.has(INSTALL_RECOVERY_KEY)) {
					mInstallRecovery = json.optBoolean(INSTALL_RECOVERY_KEY, true);
					Log.d(TAG, "Setting [InstallRecovery] to [" + (mInstallRecovery ? "true" : "false") + "]");
				}
			} catch(Exception e) {
				Log.e(TAG, "Error parsing settings file", e);
			}
		}
	}
	
	public boolean restartAdb() { return mRestartAdb; }
	public boolean installHijack() { return mInstallHijack; }
	public boolean installRecovery() { return mInstallRecovery; }
}
