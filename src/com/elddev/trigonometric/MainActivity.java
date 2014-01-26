package com.elddev.trigonometric;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;



public class MainActivity extends FragmentActivity {	
    ViewPager mViewPager;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);
		
		TextView tv = (TextView) findViewById(R.id.header_textview);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
		tv.setTypeface(tf);
	}
	
}
