package com.mithun.tilegame;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class GameScreenActivity extends Activity {

	GameCustomView game;
	SubMenu levelSubMenu;
	final int LEVEL_ITEM_ID = 98765;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_screen);
		//setTitle("Tile Game 1");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		FrameLayout frmLayout = (FrameLayout)findViewById(R.id.fullscreen_content);
	    frmLayout.setFocusable(true);
	    
	    game = new GameCustomView(this);
	    frmLayout.addView(game);
	
	}

    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		int display_mode = getResources().getConfiguration().orientation;
	    System.out.println("config change displayMode ="+display_mode);
	    TextView score = (TextView) findViewById(R.id.game_score);
		   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		   score.setGravity(Gravity.CENTER);
		   int min = Math.min(game.getWidth(), game.getHeight());
		   int max = Math.max(game.getWidth(), game.getHeight());
		   int textSize = (max-min) - 20;
		   score.setMaxWidth(textSize);
		params.setMargins(15, 15, 15, 15);
	    if(display_mode == 2){
	    	
	     params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
	   params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
	   params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
	
	   
	    }else{
	    	  params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	   	   params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
	   	   
	    }
	    score.setLayoutParams(params);
	    refreshActionBarMenu(this);
	}

	static void refreshActionBarMenu(Activity activity)
    {
    	System.out.println("invalidate called");
        activity.invalidateOptionsMenu();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar, menu);

		MenuItem selectLevelMenu = menu.findItem(R.id.action_selectlevel);
		levelSubMenu = selectLevelMenu.getSubMenu();
		int levelCompletedForSelectedImage = game.getLevelCompletedFromPref(GameLogic.imageSelected);
		//assuming level starts from 0 = 3x3 split
		System.out.println("onCreateOptionsMenu: levelCompletedForSelectedImage="+levelCompletedForSelectedImage);
		if(levelCompletedForSelectedImage < 3){
			levelCompletedForSelectedImage = 2;
		}
		for(int level = 0; level <= levelCompletedForSelectedImage-3+1; level++){
			levelSubMenu.addSubMenu(0, LEVEL_ITEM_ID+level, level, "Level "+(level+1)+" ("+(level+3)+" x "+(level+3)+")");	
		}
		
		System.out.println("onOptionsMenu"+menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("itemId = "+item.getItemId());
		switch (item.getItemId()) {
		 case android.R.id.home:
			 System.out.println("back pressed");
		        //NavUtils.navigateUpFromSameTask(this);
		        onBackPressed();
		        return true;
		case R.id.action_refresh:
			game.restart();
			break;
		case R.id.action_preview:
			//ImageView preview = (ImageView) findViewById(R.id.preview);
			//preview.setVisibility(View.VISIBLE);
			Intent previewIntent = new Intent(this, ImagePreviewDialogActivity.class);
			startActivityForResult(previewIntent, 0);
			System.out.println("preview up pressed");
			break;
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, GamePreferences.class);
			startActivityForResult(settingsIntent, 0);
			System.out.println("settings pressed");
			break;
		case R.id.action_selectlevel:
			break;
		
		default:
			//itemID starts from 0 = 3x3 split size
			int levelSelected = item.getItemId() - LEVEL_ITEM_ID;
			if(levelSelected <= 20){
				GameLogic.IMAGE_SPLIT_SIZE = levelSelected + 3;
				game.restart();
				
			}
			break;
		}
		return true;
	}


    
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		System.out.println("options closed");
		super.onOptionsMenuClosed(menu);
	}



}
