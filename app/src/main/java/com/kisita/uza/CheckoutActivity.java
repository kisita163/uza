package com.kisita.uza;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kisita.uza.custom.CustomActivity;

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
	}

}
