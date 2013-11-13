package com.elddev.trigonometric;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ListIdentities extends FragmentActivity {	
	
	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
	
    
	private ArrayList<TrigIdentity> identities; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list_identities);

		identities = new ArrayList<TrigIdentity>();		
		JSONObject identityData = null;
		try { 
			identityData = getIdentityData();
			identities = generateListFromData( identityData );
		} catch (JSONException e) { e.printStackTrace(); }
		
		TextView tv = (TextView) findViewById(R.id.header_textview);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
		tv.setTypeface(tf);
		
		// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
	}
	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
	
	private ArrayList<TrigIdentity> generateListFromData(JSONObject data) throws JSONException {
		ArrayList<TrigIdentity> identities = new ArrayList<TrigIdentity>();
		
		JSONArray identitiesData = data.getJSONArray("identities");
		
		for(int i = 0; i < identitiesData.length(); i++) {
			JSONObject identityData = identitiesData.getJSONObject(i);
			TrigIdentity t = new TrigIdentity(identityData.getString("name"));
			
			JSONArray equationsData = identityData.getJSONArray("equations");
			for(int j = 0; j < equationsData.length(); j++) {
				t.addEq( equationsData.getString(j) );
			}
			
			identities.add(t);
		}
		
		return identities;
	}

	private JSONObject getIdentityData() throws JSONException {
		String result = "No result";
		InputStream content = getResources().openRawResource(R.raw.identities);
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(content,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			content.close();
  
			result=sb.toString();
      	} catch(Exception e){
      		Log.e("log_tag", "Error converting result "+e.toString());
      	}
		
		return new JSONObject(result);
	}
	
}
