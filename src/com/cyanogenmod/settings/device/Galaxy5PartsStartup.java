package com.cyanogenmod.settings.device;

import android.app.ActivityManagerNative;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.os.SystemProperties;

public class Galaxy5PartsStartup extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent bootintent) {

		int processLimit;
		try {
			processLimit = Integer.valueOf(SystemProperties.get(Constants.PROP_PROCESSLIMIT));
		} catch (NumberFormatException e) {
			processLimit = -1;
		}

		try {
			ActivityManagerNative.getDefault().setProcessLimit(processLimit);
		} catch (RemoteException e) {
		}

		System.exit(0);
	}
}
