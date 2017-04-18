package com.kisita.uza;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.R;

/**
 * The Activity Home is launched after the Splash screen. It simply show two
 * options for Login and Signup.
 */
public class Home extends CustomActivity
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		if(!checkPlayServices(this)) {
			System.out.println("Allow users to download Google Play services from the Play Store");
			//TODO GoogleApiAvailability.makeGooglePlayServicesAvailable(this);
		}
		setupView();
	}

	/**
	 * Setup the click & other events listeners for the view components of this
	 * screen. You can add your logic for Binding the data to TextViews and
	 * other views as per your need.
	 */
	private void setupView()
	{
		setTouchNClick(R.id.btnReg);
		setTouchNClick(R.id.btnLogin);
	}

	/* (non-Javadoc)
	 * @see com.taxi.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnLogin) {
			Intent i = new Intent(this, Login.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
	}

	/*
Check the device to make sure it has the google play services apk. if it doesn't,
display a dialog that allow users to download the apk
 */
	private boolean checkPlayServices(Context context) {
		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
		return resultCode == ConnectionResult.SUCCESS;
	}
}
