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
	//private static final int PICK_IMAGE = 1;
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
	            if(GameLogic.imageSelected == -1){
	            	System.out.println("choose from galary");
	            	/*Intent intent = new Intent();
	            	intent.setType("image/*");
	            	intent.setAction(Intent.ACTION_GET_CONTENT);
	            	startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
	            	*/
	            	Toast.makeText(getApplicationContext(), "Sorry, this feature is not avaliable now.\nIts future implementation", Toast.LENGTH_SHORT).show();
	            }else{
	            	Intent gameIntent = new Intent(getApplicationContext(), GameScreenActivity.class);
					startActivity(gameIntent);
	            }
	        }
	        
	    });
	}
	
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			System.out.println("result ok");
		}else{
			System.out.println("result cancel");
		}
	    if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
	        Uri _uri = data.getData();

	        //User had pick an image.
	        Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	        cursor.moveToFirst();

	        //Link to the image
	        final String imageFilePath = cursor.getString(0);
	        cursor.close();
	        System.out.println("image path ="+imageFilePath);
	        
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}*/

}
