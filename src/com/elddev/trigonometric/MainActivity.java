package com.elddev.trigonometric;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;



public class MainActivity extends FragmentActivity {	
    ViewPager mViewPager;  
	
    private final int HOME = 0, LIST = 1, FAV = 2;
    private int fragmentState;
    Fragment homeFragment, listFragment;
    FragmentManager fm;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);
		
		TextView tv = (TextView) findViewById(R.id.header_textview);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
		tv.setTypeface(tf);
		
		ImageButton homeButton = (ImageButton) findViewById(R.id.home_button);
		ImageButton listButton = (ImageButton) findViewById(R.id.list_button);
		ImageButton favButton = (ImageButton) findViewById(R.id.fav_button);
		
		homeButton.setOnClickListener(new navClickListener(HOME));
		listButton.setOnClickListener(new navClickListener(LIST));
		favButton.setOnClickListener(new navClickListener(FAV));
		
		fm = getSupportFragmentManager();
		goToListFragment();
	}
	
	private void goToHomeFragment() {
		if(homeFragment == null) { homeFragment = new HomeFragment(); }
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_holder, homeFragment);
		transaction.commit();
		fragmentState = HOME;
	}
	
	private void goToListFragment() {
		if(listFragment == null) { listFragment = new ListFragment(); }
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_holder, listFragment);
		transaction.commit();
		fragmentState = LIST;
	}
	
	private class navClickListener implements OnClickListener {
		int type;
		
		public navClickListener(int type) {
			this.type = type;
		}
		
		@Override
		public void onClick(View arg0) {
			switch(type) {
			case HOME:
				Log.v("btnclick", "Home button touched");
				if(fragmentState != HOME) { goToHomeFragment(); }
				break;
				
			case LIST:
				Log.v("btnclick", "List button touched");
				if(fragmentState != LIST) { goToListFragment(); }
				break;
			
			case FAV:
				Log.v("btnclick", "Fav button touched");
				break;
			}
		}
		
	}
	
}
