package com.example.activent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SquareGridItemView extends TextView {

	public SquareGridItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SquareGridItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SquareGridItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = getMeasuredWidth();
		setMeasuredDimension(width, width);
	}

}
