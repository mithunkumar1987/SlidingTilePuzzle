package com.mithun.tilegame;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.view.View;

public class GamePreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		System.out.println("gamepreferences: onCreate");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.layout.game_preferences);
		Preference myPref = (Preference) findPreference("about");
		myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
		             public boolean onPreferenceClick(Preference preference) {
		            	 System.out.println("about clicked");
		            	 showAbout();
		                 return true;
		             }
		         });
	}
	
	 protected void showAbout() {
	        // Inflate the about message contents
	        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
	 

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);

	        builder.setTitle(R.string.about_title);
	        builder.setIcon(R.drawable.mithun_icon_106_64);
	        builder.setView(messageView);
	        builder.create();
	        builder.show();
	    }
	
	@SuppressWarnings("deprecation")
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPerf, String key) {
		System.out.println("pref changed :"+key);
		if(key.equals("sound")){
			GameLogic.moveSound = sharedPerf.getBoolean(key, true);
			System.out.println("sound changed to :"+GameLogic.moveSound);
		}else if(key.equals("about")){
			System.out.println("about");
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	System.out.println("preference up pressed");
	        //NavUtils.navigateUpFromSameTask(this);
	        onBackPressed();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	

}
