package com.kisita.uza.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;

/**
 * The Class SettingsFragment is the fragment that shows various settings options.
 */
public class SettingsFragment extends CustomFragment implements AdapterView.OnItemSelectedListener
{
	private Spinner mSpinner;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.settings, null);
		setHasOptionsMenu(true);
		setView(v);
		return v;
	}

	void setView(View v){
		Context  context = getContext();
		SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys),
				Context.MODE_PRIVATE);
		int pos = sharedPref.getInt(context.getString(R.string.uza_currency_position),0);

		mSpinner = (Spinner)v.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.currency, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setSelection(pos);
		mSpinner.setOnItemSelectedListener(this);
	}

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// parent.getItemAtPosition(pos)
		Log.i("Settings", parent.getItemAtPosition(position).toString());

		SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

		int pos = sharedPref.getInt(getResources().getString(R.string.uza_currency_position), 0);

		if (pos != position) {

			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(getString(R.string.uza_currency), parent.getItemAtPosition(position).toString());
			editor.putInt(getString(R.string.uza_currency_position), position);
			editor.commit();
			// Update app
			//update_app();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
