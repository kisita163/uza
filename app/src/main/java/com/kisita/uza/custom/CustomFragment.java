package com.kisita.uza.custom;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

import java.util.ArrayList;


/**
 * The Class CustomFragment is the base Fragment class. You can extend your
 * Fragment classes with this class in case you want to apply common set of
 * rules for those Fragments.
 */
public abstract class CustomFragment extends Fragment implements OnClickListener
{
	protected static final String ITEMS = "items";

	protected ArrayList<Data> itemsList;

	protected UzaCardAdapter mCardAdapter;

	//protected  RecyclerView.Adapter mCardAdapter;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
	public void onDetach() {
		getLoaderManager().destroyLoader(0);
		super.onDetach();
	}

	/*public void notifyChanges(ArrayList<Data> data){
		Log.i("CustomFragment","Notification");
		if(mCardAdapter != null)
			itemsList = data;
			BiLog.i("CustomFragment","Notification sent");
			mCardAdapter.notifyDataSetChanged();
	}*/

	protected abstract void notifyChanges(ArrayList<Data> data);
}
