package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.kisita.uza.R;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

public class BillingFragment extends Fragment implements OnCountryPickerListener, View.OnClickListener {

    //private static String TAG = "### BillingFragment";

    private Button mCountry;

    private CountryPicker mCountryPicker;

    private Button mConfirm;

    private EditText mPostalCode;

    private EditText mStateProvince;

    private EditText mCity;

    private EditText mAddress;

    private EditText mPhoneNumber;

    private EditText mName;

    private EditText mFirstName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BillingFragment.
     */
    public static BillingFragment newInstance() {
        BillingFragment fragment = new BillingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.billing_layout, container, false);

        setView(v);
        initFields();
        return v;
    }

    private void initFields() {

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
        // First Name
        String firstName  = sharedPref.getString(getString(R.string.uza_billing_first_name),"");
        mFirstName.setText(firstName);

        // Name
        String name  = sharedPref.getString(getString(R.string.uza_billing_name),"");
        mName.setText(name);

        // Phone number
        String phoneNumber  = sharedPref.getString(getString(R.string.uza_billing_phone),"");
        mPhoneNumber.setText(phoneNumber);

        //Address
        String address  = sharedPref.getString(getString(R.string.uza_billing_address),"");
        mAddress.setText(address);

        //City
        String city  = sharedPref.getString(getString(R.string.uza_billing_city),"");
        mCity.setText(city);

        //City
        String stateProvince  = sharedPref.getString(getString(R.string.uza_billing_state_province),"");
        mStateProvince.setText(stateProvince);

        //Postal code
        String postalCode  = sharedPref.getString(getString(R.string.uza_billing_postal_code),"");
        mPostalCode.setText(postalCode);
        // Country field
        Country  country = mCountryPicker.getCountryFromSIM(getContext());


        if(country != null) {
            String  countryName = country.getName();
            Drawable img = getContext().getResources().getDrawable(country.getFlag());
            img.setBounds(0, 0, 50,50);
            mCountry.setText(countryName);
            mCountry.setCompoundDrawables(img,null,null,null);
        }else{
            mCountry.setText(R.string.selecte_a_country);
        }



        mCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountryPicker.showDialog(getActivity().getSupportFragmentManager()); // Show the dialog
            }
        });

        // Confirm button
        mConfirm.setOnClickListener(this);
    }

    private void setView(View v) {
        mFirstName     = v.findViewById(R.id.field_first_name);
        mName          = v.findViewById(R.id.field_name);
        mPhoneNumber   = v.findViewById(R.id.field_number);
        mAddress       = v.findViewById(R.id.field_address);
        mCity          = v.findViewById(R.id.field_city);
        mPostalCode    = v.findViewById(R.id.field_postal_code);
        mStateProvince = v.findViewById(R.id.field_state_province);
        mConfirm       = v.findViewById(R.id.button_confirm);
        mCountry       = v.findViewById(R.id.field_country);
        mCountryPicker =
                new CountryPicker.Builder().with(getContext())
                        .listener(this)
                        .build();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onSelectCountry(com.mukesh.countrypicker.Country country) {
        Drawable img = getContext().getResources().getDrawable( country.getFlag() );
        img.setBounds(0, 0, 50,50);

        mCountry.setCompoundDrawables(img,null,null,null);
        mCountry.setText(country.getName());
    }

    @Override
    public void onClick(View view) {
        if(validateBillingForm()){
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getString(R.string.uza_billing_first_name),mFirstName.getText().toString());
            editor.putString(getString(R.string.uza_billing_name),mName.getText().toString());
            editor.putString(getString(R.string.uza_billing_phone),mPhoneNumber.getText().toString());
            editor.putString(getString(R.string.uza_billing_address),mAddress.getText().toString());
            editor.putString(getString(R.string.uza_billing_city),mCity.getText().toString());
            editor.putString(getString(R.string.uza_billing_state_province),mStateProvince.getText().toString());
            editor.putString(getString(R.string.uza_billing_postal_code),mPostalCode.getText().toString());
            editor.putString(getString(R.string.uza_billing_country),mCountry.getText().toString());

            editor.apply();

            getActivity().finish();
        }
    }

    private boolean validateBillingForm() {
        boolean result = true;

        if (TextUtils.isEmpty(mFirstName.getText().toString())) {
            mFirstName.setError(getString(R.string.required));
            result = false;
        } else {
            mFirstName.setError(null);
        }

        if (TextUtils.isEmpty(mName.getText().toString())) {
            mName.setError(getString(R.string.required));
            result = false;
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(mPhoneNumber.getText().toString())) {
            mPhoneNumber.setError(getString(R.string.required));
            result = false;
        } else {
            mPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(mAddress.getText().toString())) {
            mAddress.setError(getString(R.string.required));
            result = false;
        } else {
            mAddress.setError(null);
        }

        if (TextUtils.isEmpty(mCity.getText().toString())) {
            mCity.setError(getString(R.string.required));
            result = false;
        } else {
            mCity.setError(null);
        }

        if (TextUtils.isEmpty(mStateProvince.getText().toString())) {
            mStateProvince.setError(getString(R.string.required));
            result = false;
        } else {
            mStateProvince.setError(null);
        }

        if (TextUtils.isEmpty(mPostalCode.getText().toString())) {
            mPostalCode.setError(getString(R.string.required));
            result = false;
        } else {
            mPostalCode.setError(null);
        }


        return result;
    }
}
