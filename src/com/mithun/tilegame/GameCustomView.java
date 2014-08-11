package com.mithun.tilegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class GameCustomView extends View{
	Paint paint = new Paint();
	Bitmap mImage = null;
	Rect dest;
	Rect src;
	
	Rect helpTextBounds;
	ImageView helpImage;
	boolean helpNeeded; 
	
	int mImageSelected = R.drawable.img_0;
	int mImageIdxArray[];
		float touchDownX;
	float touchDownY;
	int dirn=-1;
	Context mContext;
	int maxScreenSize ;
	int maxImgSize;
	SoundPool sp;
	int spID;
	
	void init(){
		mImageSelected = GameLogic.imageSelected;
		BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(
				mImageSelected);
		
		mImage = d.getBitmap();
		maxImgSize = Math.min(mImage.getHeight(), mImage.getWidth());
		
		mImageIdxArray = new int[GameLogic.IMAGE_SPLIT_SIZE*GameLogic.IMAGE_SPLIT_SIZE];
		if(sp==null){
			sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

			spID = sp.load(mContext, R.raw.touch, 1);
		}
	}
	
	void restart(){
		init();	
		GameLogic.shuffle(mImageIdxArray);
		
        TextView scoreView = (TextView) ((Activity)getContext()).findViewById(R.id.game_score);
		scoreView.setText("Moves: "+GameLogic.moves);
        invalidate();
	}
	
	public GameCustomView(Context context) {
		super(context);
		System.out.println("GameCustomView");
		mContext =context;
		System.out.println("GameCustomView constructor");
		
		//mPlayer = MediaPlayer.create(context, R.raw.music);
		dest = new Rect();
		src = new Rect();
		helpTextBounds = new Rect();
		helpImage= (ImageView)((Activity)getContext()).findViewById(R.id.image_help);
		helpImage.setImageResource(R.drawable.help_icon_off);
		helpNeeded = false;
		helpImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					helpNeeded=!helpNeeded;
					System.out.println("image clicked, helpNeeded = "+helpNeeded);
					
					if(helpNeeded){
						helpImage.setImageResource(R.drawable.help_icon_on);
					}else{
						helpImage.setImageResource(R.drawable.help_icon_off);
					}
					invalidate();
				}
			});
		
		GameLogic.IMAGE_SPLIT_SIZE = Math.max(getLevelCompletedFromPref(GameLogic.imageSelected)+1,3);
		GameLogic.moveSound = getMoveSoundFromPref();
		
		System.out.println("Level completed = "+GameLogic.IMAGE_SPLIT_SIZE+", move sound = "+GameLogic.moveSound);
		restart();	
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		//player.start();
		System.out.println("onDraw");
		maxScreenSize = Math.min(getWidth(), getHeight());
		int xMin = (getWidth() - maxScreenSize)/2;
		int yMin = (getHeight() - maxScreenSize)/2;
		int srcX, srcY;
		int img_idx=0;
		int srcTileSize = maxImgSize/GameLogic.IMAGE_SPLIT_SIZE;
		int destTileSize = maxScreenSize/GameLogic.IMAGE_SPLIT_SIZE;
		System.out.println("onDraw called");
	
		for(int y=0; y<GameLogic.IMAGE_SPLIT_SIZE; y++){
			for (int x=0; x<GameLogic.IMAGE_SPLIT_SIZE; x++){
				dest.set(xMin+(x*destTileSize), yMin+(y*destTileSize),
						xMin+(x*destTileSize)+destTileSize-1, yMin+(y*destTileSize)+destTileSize-1);
				
				img_idx = mImageIdxArray[(y*GameLogic.IMAGE_SPLIT_SIZE)+x];
				srcX = getXfromIdx(img_idx);
				srcY = getYfromIdx(img_idx);
				srcX*=srcTileSize;
				srcY*=srcTileSize;
				src.set(srcX, srcY, srcX+srcTileSize, srcY+srcTileSize);
				paint.setFilterBitmap(true);
				if(img_idx == GameLogic.IMAGE_SPLIT_SIZE*GameLogic.IMAGE_SPLIT_SIZE -1){
					//don't draw the bottom last corner rectangle 
				}else{
					canvas.drawBitmap(mImage, src, dest, paint);
					//if help mode is enabled
					if(helpNeeded){
						
						String helpText = Integer.toString(img_idx+1);
						paint.setTextSize(destTileSize/4);
						
			
						paint.getTextBounds(helpText, 0, helpText.length(), helpTextBounds);

						paint.setColor(Color.RED); 
						paint.setStyle(Style.FILL); 
						canvas.drawText(helpText, dest.centerX() - (helpTextBounds.width())/2,
								                  dest.centerY() , paint);	
					}
				}
			}
		}
		System.out.println("size: "+getMeasuredHeight()+"ht:"+""+"w");
		
	}
	int getXfromIdx(int idx){
		return (idx % GameLogic.IMAGE_SPLIT_SIZE);
	}
	
	int getYfromIdx(int idx){
		return (idx / GameLogic.IMAGE_SPLIT_SIZE);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int dirn;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchDownX = event.getX();
				touchDownY = event.getY();
				System.out.println("touch down");
				break;
			case MotionEvent.ACTION_MOVE:
				System.out.println("touch move");
				break;
			case MotionEvent.ACTION_UP:
				touchDownX -= event.getX();
				touchDownY -= event.getY();
				if(Math.abs(touchDownX) < 20 && Math.abs(touchDownY) < 20 ){
					break;
				}
				System.out.println("touch up");
				dirn=GameLogic.getDirn(touchDownX, touchDownY);
				if(GameLogic.move(mImageIdxArray, dirn)){
					if(GameLogic.moveSound){
						sp.play(spID, 1, 1, 0, 0, 1);
					}
					TextView scoreView = (TextView) ((Activity)getContext()).findViewById(R.id.game_score);
					System.out.println("Moves: "+GameLogic.moves+" Score: "+GameLogic.score+" min move: "+GameLogic.minMoves);
					scoreView.setText("Moves: "+GameLogic.moves);
					if(GameLogic.isGameCompleted(mImageIdxArray)){
						System.out.println("Game Over!!");
						
						if(GameLogic.IMAGE_SPLIT_SIZE > getLevelCompletedFromPref(mImageSelected)){
							//save it in preferences
							setLevelCompletedFromPref(mImageSelected, GameLogic.IMAGE_SPLIT_SIZE);
						}
						//to add next level in menu->select level options
						GameScreenActivity.refreshActionBarMenu((Activity)getContext());
						anounceResults();
					}
					invalidate();
				}
				break;
		}
		//System.out.println("X:"+x+" Y:"+y);
		return true;
	}
	
	void anounceResults(){
		float highScore = getHighScoreFromPref(mImageSelected, GameLogic.IMAGE_SPLIT_SIZE); 
		String gameResultMsg;

		if(GameLogic.score > highScore){
			//setting preferences
			setHighScoreFromPref(mImageSelected, GameLogic.IMAGE_SPLIT_SIZE, GameLogic.score);
			gameResultMsg = new String("Congratulations! You scored Highest.\nYour Score : "+GameLogic.score);
		}else{
			gameResultMsg = new String("Your Score : "+GameLogic.score+"\nHighest Score : "+highScore);
			//gameResultMsg = new String("Your Score : "+536+"\nHighest Score : "+718);
		}
				
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("You Solved it!!")
		   .setMessage(gameResultMsg).setCancelable(true)
		   .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int arg1) {
		        System.out.println("Try Again"+arg1);
		        restart();
		    }
		})
		.setNegativeButton("Next Level", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int arg1) {
		        System.out.println("Next Level"+arg1);
		        GameLogic.IMAGE_SPLIT_SIZE++;
		        restart();
		    }
		}).setCancelable(false);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	

	String getPerfKeyForLevelCompleted(int imageId){
		return "img_"+Integer.toString(imageId);
	}
	
	String getPrefKeyForHighScore(int imageId, int level){
		return "img_"+Integer.toString(imageId)+"level_"+Integer.toString(level);
	}
	
	int getLevelCompletedFromPref(int imageId){
		SharedPreferences gamePrefs = ((Activity)getContext()).getSharedPreferences("TileGameScore", Context.MODE_PRIVATE);
		return gamePrefs.getInt(getPerfKeyForLevelCompleted(imageId), 0);
	}
	
	void setLevelCompletedFromPref(int imageId, int level){
		SharedPreferences gamePrefs = ((Activity)getContext()).getSharedPreferences("TileGameScore", Context.MODE_PRIVATE);
		Editor editor = gamePrefs.edit();
		editor.putInt(getPerfKeyForLevelCompleted(imageId), level);
		editor.commit();
	}
	
	float getHighScoreFromPref(int imageId, int level){
		SharedPreferences gamePrefs = ((Activity)getContext()).getSharedPreferences("TileGameScore", Context.MODE_PRIVATE);
		return gamePrefs.getFloat(getPrefKeyForHighScore(imageId, level), 0);
	}
	
	void setHighScoreFromPref(int imageId, int level, float highScore){
		SharedPreferences gamePrefs = ((Activity)getContext()).getSharedPreferences("TileGameScore", Context.MODE_PRIVATE);
		Editor editor = gamePrefs.edit();
		editor.putFloat(getPrefKeyForHighScore(imageId, level), highScore);
		editor.commit();
	}
	
	boolean getMoveSoundFromPref(){
		SharedPreferences gamePrefs = PreferenceManager.getDefaultSharedPreferences(((Activity)getContext()));
		// ((Activity)getContext()).getSharedPreferences("Options", Context.MODE_PRIVATE);
		return gamePrefs.getBoolean("sound", true);
	}
}
