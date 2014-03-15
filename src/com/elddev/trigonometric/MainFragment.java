package com.elddev.trigonometric;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainFragment extends Fragment {
	
	int fragmentHeight;
	int[] headerColors = {R.color.theme_cyan, R.color.theme_blue};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_fragment, container, false);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		final View myView = getView();
		
		myView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				fragmentHeight = getView().getHeight();
				if(fragmentHeight > 0) {
					initializeMainFragment(fragmentHeight);
					getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});
	}
	
	private void initializeMainFragment(int fragmentHeight) {
		try {
			JSONObject jsonContent = (new JSONImporter(JSONImporter.MAIN, this)).getContent();
			JSONArray jsonNews = jsonContent.getJSONArray("news");
			
			for(int i = 0; i < jsonNews.length(); i++) {
				createBoxFromJSON( jsonNews.getJSONObject(i) );
			}
			
		} catch (IOException e) {
			Log.v("CHAOS","Couldn't read file");
		} catch (JSONException e) {
			Log.v("CHAOS", "Problem with parsing of JSON");
		}
	}

	private void createBoxFromJSON(JSONObject jsonObject) throws JSONException, IOException {
		// Find stuff to put in box
		String text = jsonObject.getString("text");
		String title = jsonObject.getString("title");
		String imageName = jsonObject.getString("image");
		String headerColorName = jsonObject.getString("color");
		int headerColor = R.color.theme_blue;
		if(headerColorName.equals("cyan")) { headerColor = R.color.theme_cyan; } 
		
		
		
		// Inflate the box from xml-file
		LayoutInflater inflater = LayoutInflater.from(getView().getContext());
		FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.news_box, null, false);
		
		// Get the linear layout holding fragment content and add the box to this view
		LinearLayout linear = (LinearLayout) getView().findViewById(R.id.home_content_holder);
		linear.addView(layout);
		
		// Set the outter box height equal to the height of the fragment
		LayoutParams newParams = layout.getLayoutParams();
		newParams.height = fragmentHeight;
		layout.setLayoutParams(newParams);
		
		// Set the inner box centered in the outter box
		FrameLayout contentHolder = (FrameLayout) layout.getChildAt(0);
		FrameLayout.LayoutParams contentHolderParams = (FrameLayout.LayoutParams) contentHolder.getLayoutParams();
		contentHolderParams.gravity = Gravity.CENTER;
		contentHolder.setLayoutParams(contentHolderParams);
		
		// Set the header box's color and text
		FrameLayout headerView = (FrameLayout) contentHolder.getChildAt(0);
		headerView.setBackgroundColor(getResources().getColor(headerColor));
		TextView headerTextView = (TextView) headerView.getChildAt(0);
		headerTextView.setText(title);
		
		// Get the main content box
		LinearLayout contentView = (LinearLayout) contentHolder.getChildAt(1);
		
			// Set the content image
			ImageView contentImageView = (ImageView) contentView.getChildAt(0);
			Drawable imageDrawable = (new ImageImporter(imageName, this)).getDrawable();
			contentImageView.setImageDrawable(imageDrawable);
			resizeDrawable(imageDrawable, contentImageView);
			
			// Set the content text
			TextView contentTextView = (TextView) contentView.getChildAt(1);
			contentTextView.setText(text);
		
		/*
		
		FrameLayout child = (FrameLayout) layout.getChildAt(0);
		FrameLayout.LayoutParams childParams = (FrameLayout.LayoutParams) child.getLayoutParams();
		childParams.gravity = Gravity.CENTER;
		child.setLayoutParams(childParams);
		
		TextView tv = (TextView) child.getChildAt(0);
		tv.setText(text);*/
		
	}

	private void resizeDrawable(final Drawable drawable, final ImageView imageView) {
		imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				int viewWidth = imageView.getWidth();
				if(viewWidth > 0) {
					Log.v("CHAOS", "Found width: "+viewWidth);
					Bitmap b = ((BitmapDrawable)drawable).getBitmap();
					Bitmap bitmapResized = Bitmap.createScaledBitmap(b, viewWidth, viewWidth, false);
					Drawable d = new BitmapDrawable(getView().getResources(), bitmapResized);
					imageView.setImageDrawable(d);
				}
			}
		});
	}
	
}
