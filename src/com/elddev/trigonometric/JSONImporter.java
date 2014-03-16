package com.elddev.trigonometric;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

public class JSONImporter {
	
	public static final int HOME = 0, EQUATIONS = 1;
	public static final String FILES[] = {"content/home.json", "content/equations.json"};
	private String json = null;
	
	public JSONImporter(int type, View view) throws IOException {
		InputStream is = view.getContext().getAssets().open(FILES[type]);
		int size = is.available();
		byte[] buffer = new byte[size];
		is.read(buffer);
		is.close();
		json = new String(buffer, "UTF-8");
	}

	public JSONObject getContent() throws JSONException {
		return new JSONObject(json);
	}
	
}
