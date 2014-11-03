package com.example.activent;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OccupancyLevel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_occupancy_level);
		
		ProgressBar oLevel = (ProgressBar)findViewById(R.id.oLevel);
		oLevel.setProgress(0);
		
		int finalLevel = 72;
		
		if(android.os.Build.VERSION.SDK_INT >= 11){
			ObjectAnimator animateProgress = ObjectAnimator.ofInt(oLevel, "progress", finalLevel);
			animateProgress.setDuration(1000);
			animateProgress.setInterpolator(new AccelerateDecelerateInterpolator());
			animateProgress.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int val = (int) animation.getAnimatedValue();
					TextView oLevelText = (TextView)findViewById(R.id.oLevelText);
					oLevelText.setText(String.valueOf(val)+"%");
					
				}
			});
			animateProgress.start();
			
		}else{
			oLevel.setProgress(finalLevel);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.occupancy_level, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed(){
		this.finish();
		overridePendingTransition(R.anim.stay_still, R.anim.right_slide_out);
	}
}
