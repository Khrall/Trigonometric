package com.elddev.trigonometric;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IdentityFragment extends Fragment {
	
	public static final String ARG_IDENTITY_TEXT = "identity_string";
	
    public IdentityFragment() {  
    }  

    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        View identityView = inflater.inflate(R.layout.identity_fragment, container, false);  
        TextView identityTextView = (TextView) identityView.findViewById(R.id.identity_text);  
        identityTextView.setText( getArguments().getString(ARG_IDENTITY_TEXT) );  
        return identityView;  
    }  
}
