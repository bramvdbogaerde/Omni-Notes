package it.feio.android.omninotes;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.utils.Constants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class BaseActivity extends SherlockActivity {

	private final boolean TEST = false;

	protected Tracker tracker;
	protected SharedPreferences prefs;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*
		 * In esecuzioni di test viene abilitato lo StrictMode per il debug delle operazioni onerose sul thread principale e viene inibito l'invio di dati a GA
		 */
		if (TEST) {
			StrictMode.enableDefaults();
			GoogleAnalytics.getInstance(this).setDryRun(true);
		}

		// Preloads shared preferences for all derived classes
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		super.onCreate(savedInstanceState);
	}


	@Override
	public void onStart() {
		super.onStart();
		// Google Analytics
		EasyTracker.getInstance(this).activityStart(this);
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-45502770-1");
	}


	@Override
	public void onStop() {
		super.onStop();
		// Google Analytics
		EasyTracker.getInstance(this).activityStop(this);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	protected boolean navigationArchived() {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREF_NAVIGATION, "")
				.equals(getResources().getStringArray(R.array.navigation_list)[1]);
	}


	protected void showToast(CharSequence text, int duration) {
		if (prefs.getBoolean("settings_enable_info", true)) {
			Toast.makeText(getApplicationContext(), text, duration).show();
		}
	}

}