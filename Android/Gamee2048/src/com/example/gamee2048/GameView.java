package com.example.gamee2048;


import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.Gravity;

import android.view.MotionEvent;
import android.view.View;

import android.widget.GridView;




public class GameView extends GridView{
	private boolean isValidOperation = false;

	public static final int length = 4;


	private int[] randomCardSet = null;
	private int count = 16;

	private Card[][] cards = null;

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
	private void initCards() {
		cards = new Card[length][length];
		randomCardSet = new int[length*length];

		for (int x = 0; x < length; x++) {
			for (int y = 0; y < length; y++) {
				cards[x][y] = new Card(getContext());
			}
		}
		for (int position = 0; position < length*length; position++)
			randomCardSet[position] = position;
	}
    private void initGameView() {
    	this.initCards();
    	this.setNumColumns(length);
    	this.setBackgroundColor(0xffbbada0);


    	setOnTouchListener(new View.OnTouchListener() {
			private float startX, startY, offsetX, offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				isValidOperation = false;
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
							moveLeft();
						} else if (offsetX > 5) {
							System.out.println("right");
							moveRight();
						}
					} else {
						if (offsetY < -5) {
							System.out.println("up");
							moveUp();
						} else if (offsetY > 5) {
							System.out.println("down");
							moveDown();
						}
					}
					break;
				default:
					break;
				}
			  //  if (isValidOperation) {
			    	reGameView();
			   /* } else {
			    	if (isGameOver()) {
			    		gameOver();
			    	}
			    }*/
				return true;
			}
		});


    }
    private void gameOver() {
    	System.out.println("Game over");
		new AlertDialog.Builder(getContext())
		.setTitle("Game Over")
		.setMessage("继续吗？")
		.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for (int x = 0; x < length; x++) {
					for (int y = 0; y < length; y++) {
						cards[x][y].setNum(0);;
					}
				}

				reinitGameCards();
				randomOne();
				randomOne();
			}
		})
		.setNegativeButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.exit(1);
			}
		})
		.show();
    }
    private int getUpNum(int x, int y) {
    	if (x == 0)
    		return -1;
    	return cards[x - 1][y].getNum();
    }
    private int getDownNum(int x, int y) {
    	if (x == length - 1)
    		return -1;
    	return cards[x + 1][y].getNum();
    }
    private int getLeftNum(int x, int y) {
    	if (y == 0)
    		return -1;
    	return cards[x][y - 1].getNum();
    }
    private int getRightNum(int x, int y) {
    	if (y == length - 1)
    		return -1;
    	return cards[x][y + 1].getNum();
    }
    private boolean isGameOver() {
    	for (int x = 0; x < length; x++) {
    		for (int y = 0; y < length; y++) {
    			int current = cards[x][y].getNum();
    			if (current == getUpNum(x, y)
    				|| current == getDownNum(x, y)
    				|| current == getLeftNum(x, y)
    				|| current == getRightNum(x, y)) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    private void reGameView() {
    	reinitGameCards();
		if (count == 0) {
			if (isGameOver()) {
	    		gameOver();
	    	}
		}
		else if (isValidOperation){
			if (count == 1) {
				randomOne();
			} else {
				randomOne();
				randomOne();
			}
		}
		System.out.println("count = " + count);
    }
    private void randomOne() {
    	int position= new Random().nextInt(count);
    	int x = randomCardSet[position];
    	cards[x/4][x%4].setNum(new Random().nextInt(10) == 0 ? 4 : 2);
    	randomCardSet[position] = randomCardSet[count-1];
    	System.out.println("position = " + x + "num = " + cards[x/4][x%4].getNum());
    	count--;
    }

    private void reinitGameCards() {
    	int i = 0;
    	for (int x=0;x<4;x++) {
    		for (int y=0;y<4;y++) {
    			if(cards[x][y].getNum() == 0)
    				randomCardSet[i++] = 4*x+y;
    		}
    	}
    	count = i;
    	System.out.println("count = " + count);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	int cardWidth = (Math.min(w, h) - 10)/4;
    	addCards(cardWidth, cardWidth);
    	System.out.println("in startGame");
    }

    private void addCards(int cardWidth , int cardHeight) {
    	for (int x = 0; x < length; x++) {
			for (int y = 0; y < length; y++) {
				Card tmp = cards[x][y];
				tmp.getLabel().setGravity(Gravity.CENTER);
		        tmp.setBackgroundColor(0x33ffffff);
		        LayoutParams lp = new LayoutParams(cardWidth, cardWidth);
		        tmp.setLayoutParams(lp);
			}
		}
    	randomOne();
    	randomOne();
    	GameAdapter gameAdapter = new GameAdapter(getContext(), cards);
    	this.setAdapter(gameAdapter);
    }

    private boolean mergeLeft() {
    	boolean isContinueMergeLeft = false ;
    	boolean rtn = false;
    	do {
    		isContinueMergeLeft = false ;
	    	for (int x = 0; x < length;x++) {
	    		for (int y = 0; y < length -1; y++) {
	    			int current = cards[x][y].getNum();
	    			int leftOfCurrent = cards[x][y + 1].getNum();
	    		    if (current != 0 && leftOfCurrent != 0 && current == leftOfCurrent) {
	    		    	cards[x][y].setNum(current * 2);
	    		    	cards[x][y + 1].setNum(0);
	    		    	isContinueMergeLeft = true;
	    		    	isValidOperation = true;
	    		    	rtn = true;
	    			}
	    		}
	    	}
    	} while (isContinueMergeLeft);
    	return rtn;
    }


    private void moveLeft() {
    	boolean isCanLeft = false;
    	do {
    		isCanLeft = false;
	    	for (int x = 0; x < length; x++) {
	    		for (int y = 0; y < length - 1; y++) {
	    			int current = cards[x][y].getNum();

		    		if (current ==0) {
		    			int position = 0;
		    			for (int z = y; z < length; z++) {
		    				if (cards[x][z].getNum() != 0) {
		    					isCanLeft = true;
		    					isValidOperation = true;
		    					position = z;
		    					break;
		    				}
		    			}
		    			if (isCanLeft) {
			    			for (int z = y; z < length - 1 && position < length; z++, position++) {
			    				cards[x][z].setNum(cards[x][position].getNum());
			    			}
			    			cards[x][length - 1].setNum(0);
			    			isCanLeft = false;
		    			}
		    		}
	    		}
	    	}
	    	isCanLeft = mergeLeft();
    	} while (isCanLeft);

	}
    private boolean mergeRight() {
    	boolean isContinueMergeRight = false;
    	boolean rtn = false;
    	do {
    		isContinueMergeRight = false;
    		for (int x = 0; x < length; x++) {
	    		for (int y = length -1; y > 0; y--) {
	    			int current = cards[x][y].getNum();
	    			int rightOfCurrent = cards[x][y - 1].getNum();
	    			if (current != 0 && rightOfCurrent != 0 && current == rightOfCurrent) {
	    				cards[x][y].setNum(current * 2);
	    				cards[x][y - 1].setNum(0);
	    				isContinueMergeRight = true;
	    				isValidOperation = true;
	    				rtn = true;
	    			}
	    		}
	    	}
    	} while (isContinueMergeRight);
    	return rtn;
    }
    private void moveRight() {
    	boolean isCanRight = false;
    	do {
    		isCanRight = false;


	    	for (int x = 0; x < length; x++) {
	    		for (int y = length - 1; y > 0; y--) {
	    			int current = cards[x][y].getNum();
	    			if (current == 0) {
	    				int position = 0;
		    			for (int z = y; z >= 0; z--) {
		    				if (cards[x][z].getNum() != 0) {
		    					isCanRight = true;
		    					isValidOperation = true;
		    					position = z;
		    					break;
		    				}
		    			}
		    			if (isCanRight) {
			    			for (int z = y; z > 0 && position >= 0; z--, position--) {
			    				cards[x][z].setNum(cards[x][position].getNum());
			    			}
			    			cards[x][0].setNum(0);
			    			isCanRight = false;
		    			}
		    		}
	    		}
	    	}

    		isCanRight = mergeRight();
    	} while (isCanRight);
   	}
    private boolean mergeUp() {
    	boolean isContinueMergeUp = false;
    	boolean rtn = false;
    	do {
    		isContinueMergeUp = false;
    		for (int x = 0; x < length - 1; x++) {
	    		for (int y = 0; y < length ; y++) {
	    			int current = cards[x][y].getNum();
	    			int upOfCurrent = cards[x + 1][y].getNum();
	    			if (current != 0 && upOfCurrent != 0 && current == upOfCurrent) {
	    				cards[x][y].setNum(current * 2);
	    				cards[x + 1][y].setNum(0);
	    				isContinueMergeUp = true;
	    				isValidOperation = true;
	    				rtn = true;
	    			}
	    		}
	    	}
    	} while (isContinueMergeUp);
    	return rtn;
    }
    private void moveUp() {
    	boolean isCanUp = false;
    	do {

    		
    		isCanUp = false;

	    	for (int x = 0; x < length - 1;x++) {
	    		for (int y = 0; y < length; y++) {
	    			int current = cards[x][y].getNum();
	    			if (current == 0) {
	    				int position = 0;
		    			for (int z = x; z < length ; z++) {
		    				if (cards[z][y].getNum() != 0) {
		    					isCanUp = true;
		    					position = z;
		    					isValidOperation = true;
		    					break;
		    				}
		    			}
		    			if (isCanUp) {
			    			for (int z = x; z < length -1 && position < length; z++, position++) {
			    				cards[z][y].setNum(cards[position][y].getNum());
			    			}
			    			cards[length - 1][y].setNum(0);
			    			isCanUp = false;
		    			}
		    		}
	    		}
	    	}
	    	isCanUp = mergeUp();
    	} while (isCanUp);
   	}

    private boolean mergeDown() {
    	boolean isContinueMereDwon = false;
    	boolean rtn = false;
    	do {
    		isContinueMereDwon = false;
	    	for (int x = length-1; x > 0; x--) {
	    		for (int y = 0; y < length; y++) {
	    			int current = cards[x][y].getNum();
	    			int downOfCurrent = cards[x - 1][y].getNum();
	    			if (current !=0 && downOfCurrent !=0 && current == downOfCurrent) {
	    				cards[x][y].setNum(current * 2);
	    				cards[x - 1][y].setNum(0);
	    				isContinueMereDwon = true;
	    				isValidOperation = true;
	    				rtn = true;
	    			}
	    		}
	    	}
    	} while (isContinueMereDwon);
    	return rtn;
    }
    private void moveDown() {
    	boolean isCanDown = false;
    	do {
    		
    		isCanDown = false;

	    	for (int x = length -1; x > 0; x--) {
	    		for (int y = 0; y < length; y++) {
	    			int current = cards[x][y].getNum();
	    			if (current == 0) {
	    				int position = 0;
		    			for (int z = x; z >= 0; z--) {
		    				if (cards[z][y].getNum() != 0) {
		    					isCanDown = true;
		    					isValidOperation = true;
		    					position = z;
		    					break;
		    				}
		    			}
		    			if (isCanDown) {
			    			for (int z = x; z > 0 && position >= 0; z--, position--) {
			    				cards[z][y].setNum(cards[position][y].getNum());
			    			}
			    			cards[0][y].setNum(0);
			    			isCanDown = false;
		    			}
		    		}
	    		}
	    	}
	    	isCanDown = mergeDown();
    	} while (isCanDown);
   	}

}

