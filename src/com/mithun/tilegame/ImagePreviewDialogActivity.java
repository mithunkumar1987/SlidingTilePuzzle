package com.mithun.tilegame;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

public class ImagePreviewDialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_preview_dialog);
		ImageView previewImage = (ImageView) findViewById(R.id.preview);
		previewImage.setImageResource(GameLogic.imageSelected);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	System.out.println("preview up pressed");
	        //NavUtils.navigateUpFromSameTask(this);
	        onBackPressed();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
