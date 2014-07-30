package com.mithun.tilegame;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;


public class ChooseImageActivity extends Activity {
	ImageAdapter imageAdapter ;
	GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("choose image activity onCreate");
		setContentView(R.layout.activity_choose_image);
	    Calendar c = Calendar.getInstance();
	    System.out.println("mont:"+c.get(Calendar.MONTH)+",date:"+c.get(Calendar.DATE)+"Jan = "+Calendar.JANUARY);
		gridView = (GridView) findViewById(R.id.gridview);
		imageAdapter = new ImageAdapter(this);
	    gridView.setAdapter(imageAdapter);
	    
	    float scalefactor = getResources().getDisplayMetrics().density * (imageAdapter.CHOOSE_IMAGE_SIZE/2);
	    @SuppressWarnings("deprecation")
		int number = getWindowManager().getDefaultDisplay().getWidth();
	    int columns = (int) ((float) number / (float) scalefactor);
	    System.out.println("scalefactor:"+scalefactor+" number:"+number);
	    gridView.setNumColumns(columns);
	    
	    gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            GameLogic.imageSelected = imageAdapter.getImageReference(position);
	            Intent gameIntent = new Intent(getApplicationContext(), GameScreenActivity.class);
				startActivity(gameIntent);
	        }
	        
	    });
	}

}
