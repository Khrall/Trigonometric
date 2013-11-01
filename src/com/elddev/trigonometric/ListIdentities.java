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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListIdentities extends Activity {

private ArrayList<TrigIdentity> identities; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list_identities);
		
		LinearLayout identityListLayout = (LinearLayout) findViewById(R.id.identityList);
		identities = new ArrayList<TrigIdentity>();
		
		JSONObject identityData = null;
		try { 
			identityData = getIdentityData();
			identities = generateListFromData( identityData );
			
			for(TrigIdentity t : identities) {
				FrameLayout layout = new FrameLayout(this);
				layout.setBackgroundResource(R.drawable.equation_box);
				
				TextView text = new TextView(this);
				text.setText( t.toTextViewString() );
				text.setPadding(0, 15, 0, 15);
				text.setGravity(Gravity.CENTER_HORIZONTAL);
				text.setTextColor(getResources().getColor(R.color.on_white_box_font_color));
				text.setTextSize(18);
				text.setLineSpacing(10, 1);
				
				layout.addView(text );
				identityListLayout.addView( layout );
			}
		} catch (JSONException e) { e.printStackTrace(); }
		
		
		
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
