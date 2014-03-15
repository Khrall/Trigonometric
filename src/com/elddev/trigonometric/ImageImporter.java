package com.elddev.trigonometric;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public class ImageImporter {
	
	Drawable imageDrawable = null;
	
	public ImageImporter(String imageName, Fragment fragment) throws IOException {
		InputStream is = fragment.getResources().getAssets().open("images/"+imageName+".png");
		imageDrawable = Drawable.createFromStream(is, null);
	}
	
	public Drawable getDrawable() {
		return imageDrawable;
	}

}
