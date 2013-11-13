package com.elddev.trigonometric;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IdentityFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private int position;
	private ArrayList<TrigIdentity> identities = new ArrayList<TrigIdentity>();
	
	public IdentityFragmentPagerAdapter(FragmentManager fm, ArrayList<TrigIdentity> identities) {  
        super(fm); 
        this.identities = identities;
    }  

    @Override  
    public Fragment getItem(int position) {  
        String identityString = identities.get(position).toTextViewString();
        Fragment fragment = new IdentityFragment();  
        Bundle args = new Bundle();  
        args.putString(IdentityFragment.ARG_IDENTITY_TEXT, identityString);  
        fragment.setArguments(args);  
        return fragment;  
    }  

    @Override  
    public int getCount() {  
        return identities.size();
    } 
}
