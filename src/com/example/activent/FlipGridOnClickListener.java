package com.example.activent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class FlipGridOnClickListener implements OnClickListener {
	private Context mContext;
	private Class<?> classToOpen;
	private Activity activity;
	public FlipGridOnClickListener(Context mContext, Activity activity, Class<?> classToOpen) {
		this.mContext = mContext;
		this.classToOpen = classToOpen;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		openthisactivity(v, classToOpen);
	}

	
	protected void openthisactivity(View v, Class<?> openClass) {
		Context context = v.getContext();
		Intent openIntent = null;
		if(openClass == null){
			return;
		}
		
		openIntent = new Intent(context, openClass);
		
		mContext.startActivity(openIntent);
		activity.overridePendingTransition(R.anim.right_slide_in, R.anim.stay_still);
	}
}
