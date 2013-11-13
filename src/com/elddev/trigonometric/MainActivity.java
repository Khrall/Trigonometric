package com.elddev.trigonometric;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class MainActivity extends FragmentActivity {	
	
	private ArrayList<TrigIdentity> identities; 
	IdentityFragmentPagerAdapter mSectionsPagerAdapter;  
    ViewPager mViewPager;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		identities = new ArrayList<TrigIdentity>();		
		JSONObject identityData = null;
		try { 
			identityData = getIdentityData();
			identities = generateListFromData( identityData );
			
	        mSectionsPagerAdapter = new IdentityFragmentPagerAdapter(getSupportFragmentManager(), identities);  
	        mViewPager = (ViewPager) findViewById(R.id.pager);  
	        mViewPager.setAdapter(mSectionsPagerAdapter);  
		} catch (JSONException e) { e.printStackTrace(); }
		
		TextView tv = (TextView) findViewById(R.id.header_textview);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
		tv.setTypeface(tf);
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
