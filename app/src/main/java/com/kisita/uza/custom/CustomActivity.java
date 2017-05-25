package com.kisita.uza.custom;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kisita.uza.R;
import com.kisita.uza.ui.DetailFragment;
import com.kisita.uza.utils.CartDrawable;
import com.kisita.uza.utils.TouchEffect;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like setting a Theme to activity.
 */
public class CustomActivity extends AppCompatActivity implements
		OnClickListener,DetailFragment.OnFragmentInteractionListener
{

	public static final TouchEffect TOUCH = new TouchEffect();
	/**
	 * Apply this Constant as touch listener for views to provide alpha touch
	 * effect. The view must have a Non-Transparent background.
	 */

	public String TAG = "###"+ getClass().getName();
	private ProgressDialog mProgressDialog;
	/* Items count */
	private long count = 0;

	/* cart icon*/
	private LayerDrawable mIcon;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.BASE)
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setupActionBar();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			getWindow()
					.addFlags(
							WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// getWindow().setStatusBarColor(getResources().getColor(R.color.main_color_dk));
		}
	}

	/**
	 * This method will setup the top title bar (Action bar) content and display
	 * values. It will also setup the custom background theme for ActionBar. You
	 * can override this method to change the behavior of ActionBar for
	 * particular Activity
	 */
	protected void setupActionBar()
	{
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar == null)
			return;
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.ic_email);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.drawer_shadow);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the touch and click listeners for a view..
	 * 
	 * @param id
	 *            the id of View
	 * @return the view
	 */
	public View setTouchNClick(int id)
	{

		View v = setClick(id);
		v.setOnTouchListener(TOUCH);
		return v;
	}

	/**
	 * Sets the click listener for a view.
	 * 
	 * @param id
	 *            the id of View
	 * @return the view
	 */
	public View setClick(int id)
	{

		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}


	public void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMessage("Loading...");
		}

		mProgressDialog.show();
	}

	public void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	private void setBadgeCount(Context context, LayerDrawable icon, String count) {

		CartDrawable badge;
		Log.i(TAG,"setBadgeCount. count = "+count);
		// Reuse drawable if possible
		Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
		if (reuse != null && reuse instanceof CartDrawable) {
			badge = (CartDrawable) reuse;
		} else {
			badge = new CartDrawable(context);
		}
		if(Double.valueOf(count) > 9)
			badge.setCount(count,6);
		else
			badge.setCount(count, 0);
		icon.mutate();
		icon.setDrawableByLayerId(R.id.ic_badge, badge);
	}

	public void commandsCount() {
		DatabaseReference commands = getDb().child("users-data").child(getUid()).child("commands");
		commands.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Log.i(TAG, "Child added. count = " + dataSnapshot.getChildrenCount());
				count = dataSnapshot.getChildrenCount();
				if (mIcon != null)
					setBadgeCount(getApplicationContext(), mIcon, String.valueOf(dataSnapshot.getChildrenCount()));
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public String getUid() {
		return FirebaseAuth.getInstance().getCurrentUser().getUid();
	}

	public DatabaseReference getDb() {
		return FirebaseDatabase.getInstance().getReference();
	}

	@Override
	public void onFragmentInteraction(String key) {
		count++;
		String command = getDb().child("users").push().getKey(); // New command id
		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put("/users-data/" + getUid() + "/commands/"+command,key);
		getDb().updateChildren(childUpdates);
		setBadgeCount(this, mIcon, String.valueOf(count));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG,"onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.search_exp, menu);
		MenuItem itemCart = menu.findItem(R.id.action_cart);
		mIcon = (LayerDrawable) itemCart.getIcon();
		setBadgeCount(this, mIcon, String.valueOf(count));
		return super.onCreateOptionsMenu(menu);
	}
}
