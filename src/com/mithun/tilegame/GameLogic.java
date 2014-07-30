package com.mithun.tilegame;

import java.util.Random;

public class GameLogic {
	static final int DirnNone = -1;
	static final int DirnUP = 0;
	static final int DirnDOWN = 1;
	static final int DirnLEFT = 2;
	static final int DirnRIGHT = 3;
	static final int MaxDirn = 4;
	static int moves = 0;
	static int minMoves = 1;
	static float score = 0;
	static boolean isPlaying = true;
	static int IMAGE_SPLIT_SIZE = 3;
	static int imageSelected = 0;//should be set in choose image module
	static boolean  moveSound;
	static void init(int[] imgIdxArray){
		System.out.println("init()");
		for(int i=0; i< (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE); i++){
			imgIdxArray[i]=i;
		}
		System.out.println("finished Init");
	}
	
	static boolean validateShuffle(int[] imgIdxArray){
		int numFinalPos = 0; 
		for(int i=0; i<(IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE); i++){
			if(imgIdxArray[i] == i){
				numFinalPos++;
			}
		}
		//not allowing more than 3 tiles to be in its orig place while shuffle
		return (numFinalPos < (IMAGE_SPLIT_SIZE-3)*2+1)? true : false;
	}
	
	static int getOppositeDirn(int dirn){
		int oppDirn = DirnNone;
		switch (dirn) {
		case DirnUP:
			oppDirn = DirnDOWN;
			break;
		case DirnDOWN:
			oppDirn = DirnUP;
			break;
		case DirnLEFT:
			oppDirn = DirnRIGHT;
			break;
		case DirnRIGHT:
			oppDirn = DirnLEFT;
			break;
		}
		System.out.println("Opp of "+dirn+"="+oppDirn);
		return oppDirn;
	}
	
	static void shuffle(int[] imgIdxArray){
		System.out.println("shuffle called");
		boolean dbg = false;
		
		Random r = new Random();
		int dirn, prevDirn = DirnNone;

		init(imgIdxArray);
		isPlaying = false; //set to false to not increase score while shuffle moves
		minMoves = 1;
		if(!dbg){
		while(!validateShuffle(imgIdxArray)){
			dirn = r.nextInt(MaxDirn);//only 4 directions
			System.out.println("shuffle dirn:"+dirn);
			minMoves++;
			while(prevDirn == getOppositeDirn(dirn) || !move(imgIdxArray, dirn % MaxDirn)){
				dirn = r.nextInt(MaxDirn);//only 4 dirns
				System.out.println("opp move OR shuffle move false so new dirn:"+dirn);
			}
			prevDirn = dirn;
			System.out.println("in shuffle, minMoves="+minMoves);
			
		}
		}else{
			move(imgIdxArray, DirnDOWN);
		}
		
		
		isPlaying = true;
		moves = 0;
		System.out.println("moves = ");
		/* static move
		imgIdxArray[0] = 15;
		imgIdxArray[1] = 5;
		imgIdxArray[2] = 0;
		imgIdxArray[3] = 2;
		imgIdxArray[4] = 4;
		imgIdxArray[5] = 9;
		imgIdxArray[6] = 1;
		imgIdxArray[7] = 3;
		imgIdxArray[8] = 8;
		imgIdxArray[9] = 14;
		imgIdxArray[10] = 6;
		imgIdxArray[11] = 7;
		imgIdxArray[12] = 12;
		imgIdxArray[13] = 13;
		imgIdxArray[14] = 10;
		imgIdxArray[15] = 11;*/
	}
	
	static int getDirn(float x, float y){
		System.out.println("x mod = "+x+" y mod"+y);
		if(Math.abs(x)>Math.abs(y)){
			if(x>0){
				System.out.println("Left");
				return DirnLEFT;
			}else{
				System.out.println("Right");
				return DirnRIGHT;
			}
		}else{
			if(y>0){
				System.out.println("Up");
				return DirnUP;
			}else{
				System.out.println("Down");
				return DirnDOWN;
			}
		}
	}
	
	static boolean move(int[] imgIdxArray, int dirn){
		boolean isMoved = false;
		switch (dirn) {
		case DirnUP :
			isMoved = moveDirnUP(imgIdxArray);
			break;
		case DirnDOWN :
			isMoved = moveDirnDOWN(imgIdxArray);
			break;
		case DirnLEFT :
			isMoved = moveDirnLEFT(imgIdxArray);
			break;
		case DirnRIGHT :
			isMoved = moveDirnRIGHT(imgIdxArray);
			break;
		}
		if(isPlaying && isMoved){
			moves++;
		}
		System.out.println("move(), isMoved = "+isMoved);
		return isMoved;
	}
	
	static boolean moveDirnUP(int[] imgIdxArray){
		for(int y=0; y<IMAGE_SPLIT_SIZE-1; y++){
			for(int x=0; x<IMAGE_SPLIT_SIZE; x++){
				if(imgIdxArray[y*IMAGE_SPLIT_SIZE+x] == (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1){
						imgIdxArray[y*IMAGE_SPLIT_SIZE+x]=imgIdxArray[(y+1)*IMAGE_SPLIT_SIZE+x];
						imgIdxArray[(y+1)*IMAGE_SPLIT_SIZE+x] = (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1;
						return true;
				}
			}
		}
		return false;
	}
	
	static boolean moveDirnDOWN(int[] imgIdxArray){
		for(int y=1; y<IMAGE_SPLIT_SIZE; y++){
			for(int x=0; x<IMAGE_SPLIT_SIZE; x++){
				if(imgIdxArray[y*IMAGE_SPLIT_SIZE+x] == (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1){
						imgIdxArray[y*IMAGE_SPLIT_SIZE+x]=imgIdxArray[(y-1)*IMAGE_SPLIT_SIZE+x];
						imgIdxArray[(y-1)*IMAGE_SPLIT_SIZE+x] = (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1;
						return true;
				}
			}
		}
		return false;
	}
	
	static boolean moveDirnLEFT(int[] imgIdxArray){
		for(int y=0; y<IMAGE_SPLIT_SIZE; y++){
			for(int x=0; x<IMAGE_SPLIT_SIZE-1; x++){
				if(imgIdxArray[y*IMAGE_SPLIT_SIZE+x] == (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1){
						imgIdxArray[y*IMAGE_SPLIT_SIZE+x]=imgIdxArray[(y)*IMAGE_SPLIT_SIZE+x+1];
						imgIdxArray[(y)*IMAGE_SPLIT_SIZE+x+1] = (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1;
						return true;
				}
			}
		}
		return false;
	}
	
	static boolean moveDirnRIGHT(int[] imgIdxArray){
		for(int y=0; y<IMAGE_SPLIT_SIZE; y++){
			for(int x=1; x<IMAGE_SPLIT_SIZE; x++){
				if(imgIdxArray[y*IMAGE_SPLIT_SIZE+x] == (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1){
						imgIdxArray[y*IMAGE_SPLIT_SIZE+x]=imgIdxArray[(y)*IMAGE_SPLIT_SIZE+x-1];
						imgIdxArray[(y)*IMAGE_SPLIT_SIZE+x-1] = (IMAGE_SPLIT_SIZE*IMAGE_SPLIT_SIZE)-1;
						return true;
				}
			}
		}
		return false;
	}
	static boolean isGameCompleted(int[] imgIdxArray){
		for(int y=0; y<IMAGE_SPLIT_SIZE; y++){
			for(int x=0; x<IMAGE_SPLIT_SIZE; x++){
				if(imgIdxArray[y*IMAGE_SPLIT_SIZE+x] != y*IMAGE_SPLIT_SIZE+x){
					calculateFinalScore(); //mit : to be removed, debug only
					return false;
				}
			}
		}
		calculateFinalScore();
		return true;
	}
	
	static float calculateFinalScore(){
		float move_fraction = ((float)minMoves/(float)moves);
		System.out.println("final score_fraction:"+move_fraction+" minMoves:"+minMoves+" moves:"+moves);
		score = Math.round((move_fraction>1?1:move_fraction)*999);
		return score;
	}
}
