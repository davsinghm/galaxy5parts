package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PreferenceSwitch extends Preference {

	private boolean mChecked;

	public PreferenceSwitch(Context context) {
		super(context);
		setWidgetLayoutResource(R.layout.preference_widget_switch);
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean bool) {
		mChecked = bool;
		notifyChanged();
	}

	@Override
	public void onBindView(final View view) {
		super.onBindView(view);

		Switch s = (Switch) view.findViewById(R.id.header_switch);
		s.setChecked(mChecked);
		s.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(view.getContext());
					
					int last_speaker_attn = sharedPreferences.getInt("last_speaker_attn", 0);
					int last_headset_attn = sharedPreferences.getInt("last_headset_attn", 0);
					int last_fm_attn = sharedPreferences.getInt("last_fm_attn", 0);
					
					SystemProperties.set(Constants.PROP_SPEAKER_ATTN, String.valueOf(last_speaker_attn));
					SystemProperties.set(Constants.PROP_HEADSET_ATTN, String.valueOf(last_headset_attn));
					SystemProperties.set(Constants.PROP_FM_ATTN, String.valueOf(last_fm_attn));
				} else {
					SystemProperties.set(Constants.PROP_SPEAKER_ATTN, "0");
					SystemProperties.set(Constants.PROP_HEADSET_ATTN, "0");
					SystemProperties.set(Constants.PROP_FM_ATTN, "0");
				}
				mChecked = arg1;

			}

		});

	}

}
