package com.roomies.contactselector;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	public static ThumbnailCache cacheManager;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			if (getSupportFragmentManager().findFragmentById(
					android.R.id.content) == null) {
				ContactPickerFragment fragment = ContactPickerFragment
						.newInstance(1);
				fragment.setRetainInstance(true);
				getSupportFragmentManager().beginTransaction()
						.add(android.R.id.content, fragment).commit();
				
				final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
						.getMemoryClass();

				// Use 1/8th of the available memory for this memory cache.
				final int cacheSize = 1024 * 1024 * memClass / 8;

				cacheManager = new ThumbnailCache(cacheSize);

			}
		}
			
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
