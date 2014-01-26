package com.elddev.trigonometric;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {
	
	int fragmentHeight;
	
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
					/*Log.v("VARIABLE","Height: "+fragmentHeight);
					LayoutParams newParams = textBox.getLayoutParams();
					newParams.height = fragmentHeight;
					textBox.setLayoutParams(newParams);*/
					getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});
	}
	
	private void initializeMainFragment(int fragmentHeight) {
		
		for(int i = 0; i < 3; i++) {
			
			inflateNewTextBox(fragmentHeight);
			
		}
		
		
	}
	
	private void inflateNewTextBox(int fragmentHeight) {
		LayoutInflater inflater = LayoutInflater.from(getView().getContext());
		FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.content_box, null, false);
		
		
		LinearLayout linear = (LinearLayout) getView().findViewById(R.id.home_content_holder);
		linear.addView(layout);
		
		LayoutParams newParams = layout.getLayoutParams();
		newParams.height = fragmentHeight;
		layout.setLayoutParams(newParams);
		
		FrameLayout child = (FrameLayout) layout.getChildAt(0);
		FrameLayout.LayoutParams childParams = (FrameLayout.LayoutParams) child.getLayoutParams();
		childParams.gravity = Gravity.CENTER;
		child.setLayoutParams(childParams);
	}
	
}
