package com.example.gamee2048;

import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout{
	private Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>(); ;

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
		
		colorMap.put(0, 0x33ffffff);
		colorMap.put(2, 0xeee4da);
		colorMap.put(4, 0xede0c8);
		colorMap.put(8, 0x33333333);
		colorMap.put(16, 0x44444444);
		label = new TextView(getContext());
		label.setTextSize(32);
		LayoutParams lp = new LayoutParams(-1, -1);
		addView(label, lp);
		setNum(0);
		
	}
    
	private int num = 0;
	private TextView label;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	    if (num > 0) {
	    	label.setText(String.valueOf(num));
	    	this.setBackgroundColor(colorMap.get(num) == null ? colorMap.get(0) : colorMap.get(num));
	    	
	    }
	    else {
	    	label.setText("");
	    	this.setBackgroundColor(colorMap.get(0));
	    }
	}

	public TextView getLabel() {
		return label;
	}

	public void setLabel(TextView label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "Card [num=" + num + "]";
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
