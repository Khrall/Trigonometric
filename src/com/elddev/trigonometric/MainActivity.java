package com.elddev.trigonometric;

import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.google.android.vending.licensing.AESObfuscator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;



public class MainActivity extends FragmentActivity {	
    ViewPager mViewPager;  
	
    private final int HOME = 0, LIST = 1, FAV = 2;
    private int fragmentState;
    Fragment homeFragment, listFragment, favFragment;
    FragmentManager fm;
    
    private LicenseChecker mChecker;
    private LicenseCheckerCallback mLicenseCheckerCallback;
    boolean licensed;
    boolean checkingLicense;
    boolean didCheck;

    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp+BI3oO7gMu0+NxC0dwMAufXFQ37Ta94+49U2cRYg1vGtxKHbNorH2aJ/qPZWtlNuuNBVhG+pEmhbbvyqPpdhEEl1iWwfGD/sWTZ43tLSnhtD9RclLuEuHpM1twMChNRlvzdzuUXeG7Vii+7buip1D+7QziWvwG6Q3LPLwZau4H1dwhnen8hB+G3w6z5qK/G9y3zsrcNo1/w9W8YxDoxd5K3GZs4+9EWSJu2tydiozwaAFx9z6yTJHvo1fKCcbS/KqXupNHZDJDrMU7UbUmOm4WB5sf5Nn0/FQQtEchZTCuxgwTzpkhbZHbfS9ifyt6DJzKoCUCykzFqT3Q5+0sM0QIDAQAB";
    private static final byte[] SALT = new byte[] {
        -117, 65, -11, -128, -103, -57, -113, 89, -45, 51, -95,
        -45, 65, 30, -128, -113, -11, 74, -64, 88
        };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Construct the LicenseCheckerCallback. The library calls this when done.
        mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        
        // Construct the LicenseChecker with a Policy.
        mChecker = new LicenseChecker(
	        this, new ServerManagedPolicy(this,
	            new AESObfuscator(SALT, getPackageName(), Secure.getString(getContentResolver(), Secure.ANDROID_ID))),
	        BASE64_PUBLIC_KEY  // Your public licensing key.
        );
        
        // Call a wrapper method that initiates the license check
        doCheck();
        
		setContentView(R.layout.main_activity);
		
		TextView tv = (TextView) findViewById(R.id.header_textview);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
		tv.setTypeface(tf);
		
		ImageButton homeButton = (ImageButton) findViewById(R.id.home_button);
		ImageButton listButton = (ImageButton) findViewById(R.id.list_button);
		ImageButton favButton = (ImageButton) findViewById(R.id.fav_button);
		
		homeButton.setOnClickListener(new navClickListener(HOME));
		listButton.setOnClickListener(new navClickListener(LIST));
		favButton.setOnClickListener(new navClickListener(FAV));
		
		fm = getSupportFragmentManager();
		goToHomeFragment();
	}
	
	
	private void goToHomeFragment() {
		if(homeFragment == null) { homeFragment = new HomeFragment(); }
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_holder, homeFragment);
		transaction.commit();
		fragmentState = HOME;
	}
	
	private void goToListFragment() {
		if(listFragment == null) { listFragment = new ListFragment(); }
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_holder, listFragment);
		transaction.commit();
		fragmentState = LIST;
	}
	
	private void goToFavFragment() {
		if(favFragment == null) { favFragment = new FavFragment(); }
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_holder, favFragment);
		transaction.commit();
		fragmentState = FAV;
	}
	
	private class navClickListener implements OnClickListener {
		int type;
		
		public navClickListener(int type) {
			this.type = type;
		}
		
		@Override
		public void onClick(View arg0) {
			switch(type) {
			case HOME:
				Log.v("btnclick", "Home button touched");
				if(fragmentState != HOME) { goToHomeFragment(); }
				break;
				
			case LIST:
				Log.v("btnclick", "List button touched");
				if(fragmentState != LIST) { goToListFragment(); }
				break;
			
			case FAV:
				Log.v("btnclick", "Fav button touched");
				if(fragmentState != FAV) { goToFavFragment(); }
				break;
			}
		}
		
	}
	
	private void doCheck() {

        didCheck = false;
        checkingLicense = true;
        setProgressBarIndeterminateVisibility(true);

        mChecker.checkAccess(mLicenseCheckerCallback);
    }


    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {

        @Override
        public void allow(int reason) {

            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }               
            Log.i("License","Accepted!");       

                //You can do other things here, like saving the licensed status to a
                //SharedPreference so the app only has to check the license once.

            licensed = true;
            checkingLicense = false;
            didCheck = true;
        }

        @Override
        public void dontAllow(int reason) {

             if (isFinishing()) {
                    // Don't update UI if Activity is finishing.
                    return;
                }
                Log.i("License","Denied!");
                Log.i("License","Reason for denial: "+reason);                                                                              

                        //You can do other things here, like saving the licensed status to a
                        //SharedPreference so the app only has to check the license once.

                licensed = false;
                checkingLicense = false;
                didCheck = true;               

                //showDialog(0);

        }

        @Override
        public void applicationError(int reason) {

            Log.i("License", "Error: " + reason);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            licensed = true;
            checkingLicense = false;
            didCheck = false;

            //showDialog(0);
        }


    }

    protected Dialog onCreateDialog(int id) {
        // We have only one dialog.
        return new AlertDialog.Builder(this)
                .setTitle("UNLICENSED APPLICATION DIALOG TITLE")
                .setMessage("This application is not licensed, please buy it from the play store.")
                .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "http://market.android.com/details?id=" + getPackageName()));
                        startActivity(marketIntent);
                        finish();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("Re-Check", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doCheck();
                    }
                })

                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener(){
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						Log.i("License", "Key Listener");
                        finish();
                        return true;
					}
                })
                .create();

    }
	
}
