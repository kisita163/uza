package com.kisita.uza.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisita.uza.R;
import com.kisita.uza.activities.LoginActivity;
import com.kisita.uza.activities.MainActivity;
import com.kisita.uza.activities.UzaActivity;

/**
 * The Class CustomFragment is the base Fragment class. You can extend your
 * Fragment classes with this class in case you want to apply common set of
 * rules for those Fragments.
 */
public class CustomFragment extends Fragment implements OnClickListener
{
	private FirebaseAuth mAuth;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * Set the touch and click listener for a View.
	 * 
	 * @param v
	 *            the view
	 * @return the same view
	 */
	public View setTouchNClick(View v)
	{

		v.setOnClickListener(this);
		v.setOnTouchListener(CustomActivity.TOUCH);
		return v;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{

	}

	public String getUid() {
		return FirebaseAuth.getInstance().getCurrentUser().getUid();
	}

	public DatabaseReference getDb() {
		return FirebaseDatabase.getInstance().getReference();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()){
			case (R.id.action_cart):
				intent = new Intent(getActivity(), UzaActivity.class);
				intent.putExtra("fragment",1);
				startActivity(intent);
				break;
			case (R.id.favourite):
				intent = new Intent(getActivity(), UzaActivity.class);
				intent.putExtra("fragment", 0);
				startActivity(intent);
				break;
			case (R.id.action_logout):
				mAuth = FirebaseAuth.getInstance();
				mAuth.signOut();
				LoginManager.getInstance().logOut();
				intent = new Intent(getActivity(), LoginActivity.class);
				getActivity().finish();
				getActivity().startActivity(intent);
				break;
			case(R.id.action_settings):
				intent = new Intent(getActivity(), UzaActivity.class);
				intent.putExtra("fragment",2);
				getActivity().startActivity(intent);
				break;
			case(R.id.action_explore):
				intent = new Intent(getActivity(), MainActivity.class);
				getActivity().startActivity(intent);
				break;
			case(R.id.store):
				intent = new Intent(getActivity(), UzaActivity.class);
				intent.putExtra("fragment", 4);
				startActivity(intent);
				break;
			default:
				break;

		}
		return super.onOptionsItemSelected(item);
	}
}
