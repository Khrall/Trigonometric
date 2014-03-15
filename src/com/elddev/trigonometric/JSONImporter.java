package com.elddev.trigonometric;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

public class JSONImporter {
	
	public static final int MAIN = 0;
	private String json = null;
	
	public JSONImporter(int type, Fragment fragment) throws IOException {
		InputStream is = fragment.getView().getContext().getAssets().open("content/home.json");
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
