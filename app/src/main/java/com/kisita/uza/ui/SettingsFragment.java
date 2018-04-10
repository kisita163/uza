package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.kisita.uza.R;
import com.kisita.uza.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.kisita.uza.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.kisita.uza.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.kisita.uza.custom.CustomFragment;

/**
 * The Class SettingsFragment is the fragment that shows various settings options.
 */
public class SettingsFragment extends CustomFragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private static String TAG = "### SettingsFragment";

	private Switch mPainting;

    private Switch mPhotography;

    private Switch mDrawing;

    private Switch mSculpture;

    private Switch mTextile;

    private Switch mLiterature;

    private SharedPreferences sharedPref;

    private SharedPreferences.Editor editor;

    public static long MIN_PRICE_VALUE = 0;

    public static long MAX_PRICE_VALUE = 1000;

    public static int PRICE_STEP = 50;

	@SuppressLint("CommitPrefEdits")
    @Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.settings, null);
        sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
		setHasOptionsMenu(true);
		setView(v);
		return v;
	}

	void setView(View v){

		setRangeSeekBar(v);

		setCategories(v);
	}

    private void setCategories(View v) {

        mPainting    = v.findViewById(R.id.item_painting);
        mPhotography = v.findViewById(R.id.item_photography);
        mDrawing     = v.findViewById(R.id.item_drawing);
        mSculpture   = v.findViewById(R.id.item_sculpture);
        mTextile     = v.findViewById(R.id.item_textile);
        mLiterature  = v.findViewById(R.id.item_litterature);

        initCategories();

        mPainting.setOnCheckedChangeListener(this);
        mPhotography.setOnCheckedChangeListener(this);
        mDrawing.setOnCheckedChangeListener(this);
        mSculpture.setOnCheckedChangeListener(this);
        mTextile.setOnCheckedChangeListener(this);
        mLiterature.setOnCheckedChangeListener(this);
    }

    private void initCategories() {

        boolean painting = sharedPref.getBoolean(getResources().getString(R.string.painting_key),true);
        mPainting.setChecked(painting);
        Log.i(TAG,"tag"+" ** "+ painting);

        boolean photography = sharedPref.getBoolean(getResources().getString(R.string.photography_key),true);
        mPhotography.setChecked(photography);

        boolean drawing = sharedPref.getBoolean(getResources().getString(R.string.drawing_key),true);
        mDrawing.setChecked(drawing);

        boolean sculpture = sharedPref.getBoolean(getResources().getString(R.string.sculpture_key),true);
        mSculpture.setChecked(sculpture);

        boolean textile = sharedPref.getBoolean(getResources().getString(R.string.textile_key),true);
        mTextile.setChecked(textile);

        boolean literature = sharedPref.getBoolean(getResources().getString(R.string.literature_key),true);
        mLiterature.setChecked(literature);
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
			editor.apply();
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

	private void setRangeSeekBar(View v){

		// get seekbar from view
		final CrystalRangeSeekbar rangeSeekbar = v.findViewById(R.id.rangeSeekbar1);

		// get min and max text view
		final TextView tvMin = v.findViewById(R.id.textMin1);
		final TextView tvMax = v.findViewById(R.id.textMax1);

		// set listener
		rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
			@SuppressLint("SetTextI18n")
            @Override
			public void valueChanged(Number minValue, Number maxValue) {
			    String s = "";
				tvMin.setText(String.valueOf(minValue));
				if((long)maxValue == MAX_PRICE_VALUE)
				    s = "+ ";
				tvMax.setText(s + String.valueOf(maxValue));
			}
		});

		// set final value listener
		rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
			@Override
			public void finalValue(Number minValue, Number maxValue) {
				//Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                editor.putLong("priceMinValue",(long)minValue);
                editor.putLong("priceMaxValue",(long)maxValue);
                editor.apply();
			}
		});
        //Log.i(TAG,"start/end = "+sharedPref.getLong("priceMinValue",0)+"/"+sharedPref.getLong("priceMaxValue",0));
        rangeSeekbar.setMinValue(MIN_PRICE_VALUE)
                .setMaxValue(MAX_PRICE_VALUE)
                .setBarHeight(10)
                .setSteps(PRICE_STEP)
                .setMinStartValue(sharedPref.getLong("priceMinValue",0))
                .setMaxStartValue(sharedPref.getLong("priceMaxValue", MAX_PRICE_VALUE))
                .apply();
	}

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String tag;

        switch(compoundButton.getId()){
            case R.id.item_painting:
                tag = getString(R.string.painting_key);
                break;
            case R.id.item_photography:
                tag = getString(R.string.photography_key);
                break;
            case R.id.item_drawing:
                tag = getString(R.string.drawing_key);
                break;
            case R.id.item_sculpture:
                tag = getString(R.string.sculpture_key);
                break;
            case R.id.item_textile:
                tag = getString(R.string.textile_key);
                break;
            case R.id.item_litterature:
                tag = getString(R.string.literature_key);
                break;
            default:
                tag = "default";
                break;
        }

        editor.putBoolean(tag,b);
        editor.apply();
    }
}
