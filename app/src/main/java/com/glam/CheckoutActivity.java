package com.glam;

import android.os.Bundle;

import com.glam.custom.CustomActivity;

/**
 * The Activity CheckoutActivity is just a container class for Checkout fragment
 * to allow checkout screen to be shown separately.
 */
public class CheckoutActivity extends CustomActivity
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout_act);

		/*Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		toolbar.setTitle("Settings");
		setSupportActionBar(toolbar);*/
		getSupportActionBar().setTitle("Checkout");

	}

}
