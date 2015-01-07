package com.example.gamee2048;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout{

	public Card(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setNum(0);
	}

	public Card(Context context, AttributeSet attrs) {
		super(context, attrs);
		setNum(0);
	}

	public Card(Context context) {
		super(context);
		label = new TextView(getContext());
		label.setTextSize(32);
		LayoutParams lp = new LayoutParams(-1, -1);
		addView(label, lp);
		setNum(0);
	}
    
	private int num = 0;
	private TextView label;
	public void setNum(int num) {
		this.num = num;
	
		label.setText(String.valueOf(num));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + num;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		
		if (num != other.num)
			return false;
		return true;
	}
	
}
