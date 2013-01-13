package com.cyanogenmod.settings.device;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.cyanogenmod.settings.device.R;

public class DeviceSettings extends PreferenceActivity {

	public static PreferenceSwitch mPrefAttn;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			super.onBackPressed();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		Drawable settingsIcon = null;
		try {
			settingsIcon = getPackageManager().getApplicationIcon("com.android.settings");
		} catch (NameNotFoundException e) {
			settingsIcon = null;
		}
		if (settingsIcon != null) {
			getActionBar().setIcon(settingsIcon);
		}

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Resources resources = getResources();

		PreferenceScreen preferenceScreen = getPreferenceManager()
				.createPreferenceScreen(this);

		PreferenceCategory audioCat = new PreferenceCategory(this);
		audioCat.setTitle(getText(R.string.audio_settings_cat));
		preferenceScreen.addPreference(audioCat);

		// ATTENUATION
		mPrefAttn = new PreferenceSwitch(this);
		mPrefAttn.setTitle(getText(R.string.attenuation));
		mPrefAttn.setIntent(new Intent(this, AttenuationActivity.class));
		boolean isChecked = SystemProperties.get(Constants.PROP_FM_ATTN).equals("0")
				&& SystemProperties.get(Constants.PROP_HEADSET_ATTN).equals("0")
				&& SystemProperties.get(Constants.PROP_SPEAKER_ATTN).equals("0");
		mPrefAttn.setChecked(isChecked ? false : true);
		audioCat.addPreference(mPrefAttn);

		// EXTAMP FILTER
		final CheckBoxPreference extampFilter = new CheckBoxPreference(this);
		extampFilter.setTitle(getText(R.string.extamp_filter));
		extampFilter.setSummary(getText(R.string.extamp_filter_sum));
		extampFilter.setChecked(SystemProperties.get(Constants.PROP_SAMSUNG_EXTAMP_FILTER).equals("1") ? true : false);
		extampFilter.setEnabled(true);
		extampFilter.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
					public boolean onPreferenceClick(Preference arg0) {
						if (extampFilter.isChecked()) {
							SystemProperties.set(Constants.PROP_SAMSUNG_EXTAMP_FILTER, "1");
						} else {
							SystemProperties.set(Constants.PROP_SAMSUNG_EXTAMP_FILTER, "0");
						}
						return false;
					}
				});
		audioCat.addPreference(extampFilter);

		PreferenceCategory memory = new PreferenceCategory(this);
		memory.setTitle(getText(R.string.memory_man));
		preferenceScreen.addPreference(memory);

		// BACKGROUND PROCESS LIMIT
		Preference bgProcessLimit = new Preference(this);
		bgProcessLimit.setTitle(getText(R.string.back_process_limit));
		int currentBgProcLimit;
		try {
			currentBgProcLimit = Integer.parseInt(SystemProperties.get(Constants.PROP_PROCESSLIMIT)) + 1;
        } catch(NumberFormatException e) {
        	currentBgProcLimit = 0;
        }
		bgProcessLimit.setSummary(Constants.getProcessLimit(resources)[currentBgProcLimit]);
		bgProcessLimit.setEnabled(true);
		bgProcessLimit.setOnPreferenceClickListener(new BackProcessLimit(this, resources));
		memory.addPreference(bgProcessLimit);

		// DYNAMIC LMK
		final CheckBoxPreference dynamicLMK = new CheckBoxPreference(this);
		dynamicLMK.setTitle(getText(R.string.lmk));
		dynamicLMK.setSummary(getText(R.string.lmk_sum));
		dynamicLMK.setChecked(SystemProperties.get(Constants.PROP_LMK).equals("1") ? true : false);
		dynamicLMK.setEnabled(true);
		dynamicLMK.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference arg0) {
				if (dynamicLMK.isChecked()) {
					SystemProperties.set(Constants.PROP_LMK, "1");
				} else {
					SystemProperties.set(Constants.PROP_LMK, "0");
				}
				return false;
			}
		});
		memory.addPreference(dynamicLMK);

		// CACHE SIZE
		Preference cacheSize = new Preference(this);
		cacheSize.setTitle(getText(R.string.cache_size));
		int currentCacheSize;
		try {
			currentCacheSize = Integer.parseInt(SystemProperties.get(Constants.PROP_CACHESIZE));
        } catch(NumberFormatException e) {
        	currentCacheSize = 0;
        }
		cacheSize.setSummary(getText(R.string.current_setting) + ": " + Constants.getCacheSize(resources)[currentCacheSize]);
		cacheSize.setEnabled(true);
		cacheSize.setOnPreferenceClickListener(new CacheSize(this, resources));
		memory.addPreference(cacheSize);

		// SWAP
		final CheckBoxPreference swap = new CheckBoxPreference(this);
		swap.setTitle(getText(R.string.swap));
		swap.setChecked(SystemProperties.get(Constants.PROP_SWAP).equals("1") ? true : false);

		boolean zRamOFF = (SystemProperties.get(Constants.PROP_COMPCACHE).equals("") 
				|| SystemProperties.get(Constants.PROP_COMPCACHE).equals("0"))
				&& (SystemProperties.get(Constants.PROP_COMPCACHE_RO).equals("")
				|| SystemProperties.get(Constants.PROP_COMPCACHE_RO).equals("0"));

		swap.setEnabled(zRamOFF ? true : false);
		swap.setSummary(zRamOFF ? getText(R.string.swap_en) : getText(R.string.swap_dis));
		swap.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference arg0) {
				if (swap.isChecked()) {
					SystemProperties.set(Constants.PROP_SWAP, "1");
				} else {
					SystemProperties.set(Constants.PROP_SWAP, "0");
				}
				return false;
			}
		});
		memory.addPreference(swap);

		PreferenceCategory screenCat = new PreferenceCategory(this);
		screenCat.setTitle(getText(R.string.intrfc));
		preferenceScreen.addPreference(screenCat);

		// FAKE DUALTOUCH
		final CheckBoxPreference dualTouch = new CheckBoxPreference(this);
		dualTouch.setTitle(getText(R.string.fake_dt));
		dualTouch.setSummary(getText(R.string.fake_dt_sum));
		dualTouch.setChecked(SystemProperties.get(Constants.PROP_FAKE_DT).equals("1") ? true : false);
		dualTouch.setEnabled(true);
		dualTouch.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference arg0) {
				if (dualTouch.isChecked()) {
					SystemProperties.set(Constants.PROP_FAKE_DT, "1");
				} else {
					SystemProperties.set(Constants.PROP_FAKE_DT, "0");
				}
				return false;
			}
		});
		screenCat.addPreference(dualTouch);

		PreferenceCategory reportBug = new PreferenceCategory(this);
		reportBug.setTitle(getText(R.string.report_bug));
		preferenceScreen.addPreference(reportBug);

		Preference generateReport = new Preference(this);
		generateReport.setTitle(getText(R.string.bug_report_gen));
		boolean isRoot = SystemProperties.get(Constants.PROP_ROOTACCESS).equals("3");
		generateReport.setEnabled(isRoot ? true : false);
		generateReport.setSummary(isRoot ? getText(R.string.bug_report_info) : getText(R.string.bug_report_root_req));
		generateReport.setOnPreferenceClickListener(new GetLogTask(this));
		reportBug.addPreference(generateReport);

		setPreferenceScreen(preferenceScreen);

	}
}
