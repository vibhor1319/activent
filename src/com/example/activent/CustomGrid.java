package com.example.activent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomGrid extends BaseAdapter {
	private Context mContext;
	private Activity activity;
	private final String[][] web;
	private final Class<?>[][] openpaths;
	private static final int LEFT_ITEM = 0;
	private static final int RIGHT_ITEM = 1;
	
	public CustomGrid(Context c, Activity activity, String[][] web, Class<?>[][] openpaths) {
		mContext = c;
		this.web = web;
		this.openpaths = openpaths;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return web.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	public String getLeftItem(int pos){
		return web[pos][0];
	}
	public String getRightItem(int pos){
		return web[pos][1];
	}
	

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			grid = new View(mContext);
			grid = inflater.inflate(R.layout.grid_single, null);
			
			SquareGridItemView leftTextView = (SquareGridItemView) grid.findViewById(R.id.grid_left);
			SquareGridItemView rightTextView = (SquareGridItemView) grid.findViewById(R.id.grid_right);
			
			leftTextView.setText(web[position][LEFT_ITEM]);
			if(web[position][1] != null){		//Check if there is actually the right tile
				rightTextView.setText(web[position][RIGHT_ITEM]);
			}else{
				rightTextView.setVisibility(View.INVISIBLE);
			}
			
			leftTextView.setOnClickListener(new FlipGridOnClickListener(mContext, activity, getClassToOpen(position, LEFT_ITEM)));
			
			rightTextView.setOnClickListener(new FlipGridOnClickListener(mContext, activity, getClassToOpen(position, RIGHT_ITEM)));
			
		}else{
			grid = (View)convertView;
		}
		
		
		return grid;
	}

	private Class<?> getClassToOpen(int position, int i) {
		return openpaths[position][i];
	}

}
