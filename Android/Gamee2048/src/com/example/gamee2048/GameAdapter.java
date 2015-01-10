package com.example.gamee2048;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GameAdapter extends BaseAdapter{
	private Card[][] cards ;
    private LayoutInflater listContainer; 
	private Context context;
   
   
    public GameAdapter(Context context, Card[][] cards) {
    	this.context = context;
    	listContainer = LayoutInflater.from(context);
    	this.cards = cards;
    }
    
    public int getCount() {  
        // TODO Auto-generated method stub  
        return GameView.length * GameView.length;  
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
        
        if (convertView == null) {  
            convertView = cards[position/4][position%4];
            convertView.setTag(cards[position/4][position%4]);  
        }
          
        return convertView;  
    }  
}
