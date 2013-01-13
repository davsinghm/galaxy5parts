package com.cyanogenmod.settings.device;

import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.preference.Preference;
import com.cyanogenmod.settings.device.R;

final class BackProcessLimit implements Preference.OnPreferenceClickListener {

	Context mContext;
	Resources mResources;
	int mSelected;

	BackProcessLimit(Context context, Resources resources) {
		mContext = context;
		mResources = resources;
	}

	public final boolean onPreferenceClick(final Preference preference) {

		try {
			mSelected = Integer.parseInt(SystemProperties
					.get(Constants.PROP_PROCESSLIMIT));
		} catch (NumberFormatException e) {
			mSelected = 0;
		}

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(mContext.getText(R.string.back_process_limit));
		dialog.setSingleChoiceItems(Constants.getProcessLimit(mResources),
						(mSelected + 1), new OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {

								SystemProperties.set(
										Constants.PROP_PROCESSLIMIT, ""
												+ Integer.valueOf(arg1 - 1));

								int processLimit;
								try {
									processLimit = Integer.valueOf(SystemProperties
											.get(Constants.PROP_PROCESSLIMIT));
								} catch (NumberFormatException e) {
									processLimit = -1;
								}

								try {
									ActivityManagerNative.getDefault()
											.setProcessLimit(processLimit);
								} catch (RemoteException e) {
								}

								preference.setSummary(Constants.getProcessLimit(mResources)[arg1].toString());

								arg0.dismiss();

							}
						});
		dialog.setNegativeButton(mContext.getText(R.string.cancel), null);
		dialog.show();
		return false;

	}
}
