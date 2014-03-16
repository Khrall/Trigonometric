package com.elddev.trigonometric;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ListFragment extends Fragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		
		View listView = inflater.inflate(R.layout.list_fragment, container, false);
		LayoutInflater listInflater = LayoutInflater.from(listView.getContext());
		
		generateEquations(listView, listInflater);
		
        return listView;
    }
	
	private void generateEquations(View view, LayoutInflater inflater) {
		LinearLayout linear = (LinearLayout) view.findViewById(R.id.list_content_holder);
		
		try {
			JSONObject jsonContent = (new JSONImporter(JSONImporter.EQUATIONS, view)).getContent();
			JSONArray jsonEquations = jsonContent.getJSONArray("equations");
			
			for(int i = 0; i < jsonEquations.length(); i++) {
				JSONObject jsonEquation = jsonEquations.getJSONObject(i);
				
				String equationString = jsonEquation.getString("string");
				int height = jsonEquation.getInt("height");
				int width = jsonEquation.getInt("width");
				float startX = Float.parseFloat(jsonEquation.getString("startX"));
				float startY = Float.parseFloat(jsonEquation.getString("startY"));
				
				Log.v("trigonometric","Height: "+height);
				Log.v("trigonometric","Width: "+width);
				Log.v("trigonometric","StartX: "+startX);
				Log.v("trigonometric","StartY: "+startY);
				
				FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.equation_box, null, false);
				
				EquationView equation = (EquationView) ((FrameLayout) frame.getChildAt(0)).getChildAt(0);
				equation.setEquation(equationString);
				equation.setCursor(startX, startY);
				equation.getLayoutParams().height = height;
				equation.getLayoutParams().width = width;
				
				linear.addView(frame);
			}
			
		} catch (IOException e) {
			Log.v("trigonometric","Couldn't read file");
		} catch (JSONException e) {
			Log.v("trigonometric", "Problem with parsing of JSON");
		}
	}

	
}
