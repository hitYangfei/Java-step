package com.example.gamee2048;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class GameAdapter extends BaseAdapter{
	private TextView[][] cardArray = new TextView[4][4];
	private int i = 0;
	private int x = 0, y =0;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public TextView[][] getCardArray() {
		return cardArray;
	}
	private Context context;                        //运行上下文  
    private List<HashMap<String, Object>> listItems;    //商品信息集合  
    private LayoutInflater listContainer; 
    private int cardWidth;
   
    public final class ListItemView{                //自定义控件集合    
        public TextView card;    
    }
    public GameAdapter(Context contet, TextView[][] cardArray)
    {
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
        this.cardArray = cardArray;
    }
    public GameAdapter(Context context, List<HashMap<String, Object>> listItems) {  
        this.context = context;           
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
        this.listItems = listItems;  
        Random ran = new Random();
        x = ran.nextInt(16);
        y = ran.nextInt(15);
        if (x == y)
        	y = 15;
        
        System.out.println("random x.y:" + x + "," + y);
   
      
    } 
    public void setCardWidth(int cardWidth)
    {
    	this.cardWidth = cardWidth;
    }
    
    public int getCount() {  
        // TODO Auto-generated method stub  
        return cardArray.length*cardArray.length;  
    }  
  
    public Object getItem(int arg0) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public long getItemId(int arg0) {  
        // TODO Auto-generated method stub  
        return 0;  
    }  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
        Log.e("method", "getView");  
        //自定义视图  
        ListItemView  listItemView = null;  
        if (convertView == null) {  
            listItemView = new ListItemView();   
            //获取list_item布局文件的视图  
            convertView = listContainer.inflate(R.layout.card, null);  
            //获取控件对象
            if (cardArray[position/4][position%4] == null) {
	            listItemView.card = (TextView)convertView.findViewById(R.id.ItemText);
	            if (position ==x || position == y) {
	            	String t = new Random().nextInt(10) == 0 ? "4" : "2";
	            	listItemView.card.setText(t);
	            	System.out.println("random card " + t);
	            } else
	            	listItemView.card.setText("");
	            listItemView.card.setGravity(Gravity.CENTER);
	            listItemView.card.setBackgroundColor(0x33ffffff);
	            LayoutParams lp = new LayoutParams(cardWidth, cardWidth);
	            lp.setMargins(10, 10, 0, 0);
	            listItemView.card.setLayoutParams(lp);
	        
	     
	            cardArray[position/4][position%4] = listItemView.card;
	         
	            System.out.println("position = " + position);
            } else {
            	listItemView.card = cardArray[position/4][position%4];
            }
            convertView.setTag(listItemView);  
            
        }else {  
            listItemView = (ListItemView)convertView.getTag();  
        }  
          
        return convertView;  
    }  
}
