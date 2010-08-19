package com.koushikdutta.droidx.bootstrap;

import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;

public class Helper {
	static final String LOGTAG = "DroidXBootstrap";

	public final static String SCRIPT_NAME = "surunner.sh";

	public static Process runSuCommandAsync(Context context, String command) throws IOException
	{
		DataOutputStream fout = new DataOutputStream(context.openFileOutput(SCRIPT_NAME, 0));
		fout.writeBytes(command);
		fout.close();
		
		String[] args = new String[] { "su", "-c", ". " + context.getFilesDir().getAbsolutePath() + "/" + SCRIPT_NAME };
		Process proc = Runtime.getRuntime().exec(args);
		return proc;
	}

	public static int runSuCommand(Context context, String command) throws IOException, InterruptedException
	{
		return runSuCommandAsync(context, command).waitFor();
	}
	
	public static int runSuCommandNoScriptWrapper(Context context, String command) throws IOException, InterruptedException
	{
		String[] args = new String[] { "su", "-c", command };
		Process proc = Runtime.getRuntime().exec(args);
		return proc.waitFor();
	}

}
