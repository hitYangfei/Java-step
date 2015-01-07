package com.example.gamee2048;

import java.util.ArrayList;
import java.util.HashMap;




import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class GameView extends GridView{
	private int[] randomCardNum = null; 
	private int count = 16;
	TextView[][] gameCards = new TextView[4][4] ;
	public void initRandomCardNum() {
		randomCardNum = new int[16];
		for (int i=0;i<16;i++)
			randomCardNum[i] = i;
	}
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
		// TODO Auto-generated constructor stub
	}

	public GameView(Context context) {
		super(context);
		initGameView();
		// TODO Auto-generated constructor stub
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initGameView();
		// TODO Auto-generated constructor stub
	}
	
    private void initGameView() {
    	this.setNumColumns(4);
    	this.setBackgroundColor(0xffbbada0);
    	initRandomCardNum();
    	
    	setOnTouchListener(new View.OnTouchListener() {
			private float startX, startY, offsetX, offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						if (offsetX < -5) {
							System.out.println("left");
							swipeLeft();
							reGameView();
						} else if (offsetX > 5) {
							System.out.println("right");
							swipeRight();
							reGameView();
						}
					} else {
						if (offsetY < -5) {
							System.out.println("up");
							swipeUp();
							reGameView();
						} else if (offsetY > 5) {
							System.out.println("down");
							swipeDown();
							reGameView();
						}
					}
					break;
				default:
					break;
				}
			
				return true;
			}
		});
    	
    	
    }
    private void reGameView() {
    	reinitGameCards();
		if (count == 0)
			System.out.println("Game over");
		else if (count ==1 ) {
			randomOne();
		} else {
			randomOne();
			randomOne();
		}
		System.out.println("count = " + count);
    }
    private void randomOne() {
    	int position= new Random().nextInt(count);
    	int x = randomCardNum[position];
    	gameCards[x/4][x%4].setText(new Random().nextInt(10) == 0 ? "4" : "2");
    	randomCardNum[position] = randomCardNum[count-1];
    	System.out.println("position = " + x);
    	count--;
    }
    private void reinitGameCards() {
    	int i = 0;
    	for (int x=0;x<4;x++) {
    		for (int y=0;y<4;y++) {
    			if(gameCards[x][y].getText().equals(""))
    				randomCardNum[i++] = 4*x+y;
    		}
    	}
    	count = i;
    	System.out.println("count = " + count);
    	
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	
    	int cardWidth = Math.min(w, h)/4;
    	
    	addCard(cardWidth, cardWidth);
    	System.out.println("in startGame");
    	
    	
    }
    
    private void addCard(int cardWidth , int cardHeight) {
    	
       
    	List<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>(); 
    	for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				HashMap<String, Object> map = new HashMap<String, Object>(); 
				
				map.put("ItemText", "2");
				lstImageItem.add(map); 
			
			}
		}
    
    	GameAdapter saImageItems = new GameAdapter(getContext(), lstImageItem);
    	saImageItems.setCardWidth(cardWidth);
    	this.setAdapter(saImageItems);
    	this.gameCards = saImageItems.getCardArray();
    	int x = saImageItems.getX();
    	int y = saImageItems.getY();
    	randomCardNum[x] = 16;
    	randomCardNum[y] = 15;
 
    	count = count - 2;
 
    }
    
    
    private void swipeLeft() {
    	boolean isCanLeft = false;
    	do {
    		isCanLeft = false;
	    	
	    	
	    	for (int x=0;x<4;x++) {
	    		for (int y=0;y<3;y++) {
	    			CharSequence strx = gameCards[x][y].getText();
	    			CharSequence stry = gameCards[x][y+1].getText();
	    			if (!strx.equals("")&&
	    			    !stry.equals("")&&
	    			    strx.equals(stry)) {
	    				int newNum = Integer.parseInt((String) strx)*2;
	    				gameCards[x][y].setText("" + newNum);
	    				gameCards[x][y+1].setText("");
	    			}
	    		}
	    	}
	    	
	    	for (int x=0;x<4;x++) {
	    		for (int y=0;y<3;y++) {
		    		CharSequence strx = gameCards[x][y].getText();
		    		if(strx.equals("")) {
		    			for (int z=y;z<4;z++) {
		    				if (!gameCards[x][z].getText().equals(""))
		    					isCanLeft = true;
		    			}
		    			if (isCanLeft) {
			    			for (int z = y; z < 3; z++) {
			    				gameCards[x][z].setText(gameCards[x][z+1].getText());
			    			}
			    			gameCards[x][3].setText("");
		    			}
		    		}
	    		}
	    	}
    	} while (isCanLeft);
    	
	}
    private void swipeRight() {
    	boolean isCanRight = false;
    	do {
    		isCanRight = false;	    		    	
	    	for (int x=0;x<4;x++) {
	    		for (int y=3;y>0;y--) {
	    			CharSequence strx = gameCards[x][y].getText();
	    			CharSequence stry = gameCards[x][y-1].getText();
	    			if (!strx.equals("")&&
	    			    !stry.equals("")&&
	    			    strx.equals(stry)) {
	    				int newNum = Integer.parseInt((String) strx)*2;
	    				gameCards[x][y].setText("" + newNum);
	    				gameCards[x][y-1].setText("");
	    			}
	    		}
	    	}
	    	
	    	for (int x=0;x<4;x++) {
	    		for (int y=3;y>0;y--) {
		    		CharSequence strx = gameCards[x][y].getText();
		    		if(strx.equals("")) {
		    			for (int z=y;z>=0;z--) {
		    				if (!gameCards[x][z].getText().equals(""))
		    					isCanRight = true;
		    			}
		    			if (isCanRight) {
			    			for (int z = y; z > 0; z--) {
			    				gameCards[x][z].setText(gameCards[x][z-1].getText());
			    			}
			    			gameCards[x][0].setText("");
		    			}
		    		}
	    		}
	    	}
    	} while (isCanRight);
   	}
    private void swipeUp() {
    	boolean isCanUp = false;
    	do {
    		isCanUp = false;
	    	
	    	
	    	for (int x=0;x<3;x++) {
	    		for (int y=0;y<4;y++) {
	    			CharSequence strx = gameCards[x][y].getText();
	    			CharSequence stry = gameCards[x+1][y].getText();
	    			if (!strx.equals("")&&
	    			    !stry.equals("")&&
	    			    strx.equals(stry)) {
	    				int newNum = Integer.parseInt((String) strx)*2;
	    				gameCards[x][y].setText("" + newNum);
	    				gameCards[x+1][y].setText("");
	    			}
	    		}
	    	}
	    	
	    	for (int x=0;x<3;x++) {
	    		for (int y=0;y<4;y++) {
		    		CharSequence strx = gameCards[x][y].getText();
		    		if(strx.equals("")) {
		    			for (int z=x;z<4;z++) {
		    				if (!gameCards[z][y].getText().equals(""))
		    					isCanUp = true;
		    			}
		    			if (isCanUp) {
			    			for (int z = x; z < 3; z++) {
			    				gameCards[z][y].setText(gameCards[z+1][y].getText());
			    			}
			    			gameCards[3][y].setText("");
		    			}
		    		}
	    		}
	    	}
    	} while (isCanUp);
   	}
    private void swipeDown() {
    	boolean isCanDown = false;
    	do {
    		isCanDown = false;
	    	
	    	
	    	for (int x=3;x>0;x--) {
	    		for (int y=0;y<4;y++) {
	    			CharSequence strx = gameCards[x][y].getText();
	    			CharSequence stry = gameCards[x-1][y].getText();
	    			if (!strx.equals("")&&
	    			    !stry.equals("")&&
	    			    strx.equals(stry)) {
	    				int newNum = Integer.parseInt((String) strx)*2;
	    				gameCards[x][y].setText("" + newNum);
	    				gameCards[x-1][y].setText("");
	    			}
	    		}
	    	}
	    	
	    	for (int x=3;x>0;x--) {
	    		for (int y=0;y<4;y++) {
		    		CharSequence strx = gameCards[x][y].getText();
		    		if(strx.equals("")) {
		    			for (int z=x;z>=0;z--) {
		    				if (!gameCards[z][y].getText().equals(""))
		    					isCanDown = true;
		    			}
		    			if (isCanDown) {
			    			for (int z = x; z > 0; z--) {
			    				gameCards[z][y].setText(gameCards[z-1][y].getText());
			    			}
			    			gameCards[0][y].setText("");
		    			}
		    		}
	    		}
	    	}
    	} while (isCanDown);
   	}
 
}

