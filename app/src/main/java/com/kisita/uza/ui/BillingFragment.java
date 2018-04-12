package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import static com.kisita.uza.utils.Settings.getClientAddress;
import static com.kisita.uza.utils.Settings.getClientCity;
import static com.kisita.uza.utils.Settings.getClientCountry;
import static com.kisita.uza.utils.Settings.getClientFirstName;
import static com.kisita.uza.utils.Settings.getClientLastName;
import static com.kisita.uza.utils.Settings.getClientNumber;
import static com.kisita.uza.utils.Settings.getClientPostalCode;
import static com.kisita.uza.utils.Settings.getClientState;
import static com.kisita.uza.utils.Settings.setClientAddress;
import static com.kisita.uza.utils.Settings.setClientCity;
import static com.kisita.uza.utils.Settings.setClientCountry;
import static com.kisita.uza.utils.Settings.setClientFirstName;
import static com.kisita.uza.utils.Settings.setClientLastName;
import static com.kisita.uza.utils.Settings.setClientNumber;
import static com.kisita.uza.utils.Settings.setClientPostalCode;
import static com.kisita.uza.utils.Settings.setClientState;


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

    private String mCountryCode = "BE";

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.billing_layout, container, false);

        setView(v);
        initFields();
        return v;
    }

    private void initFields() {

        Context context = getContext();
        // First Name
        mFirstName.setText(getClientFirstName(context));
        // Name
        mName.setText(getClientLastName(context));
        // Phone number
        mPhoneNumber.setText(getClientNumber(context));
        //Address
        mAddress.setText(getClientAddress(context));
        //City
        mCity.setText(getClientCity(context));
        //Province
        mStateProvince.setText(getClientState(context));
        //Postal code
        mPostalCode.setText(getClientPostalCode(context));
        // Country field
        Country  country    = null ;//= mCountryPicker.getCountryFromSIM(getContext());
        String   countryName = getClientCountry(context);// Check country ISO here

        if(countryName == null || countryName.equalsIgnoreCase("")) { // Country not initialized yet
            mCountry.setText(R.string.selecte_a_country);
        }else {
            country =  mCountryPicker.getCountryByISO(countryName);
        }

        setCountryField(country);


        mCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountryPicker.showDialog(getActivity().getSupportFragmentManager()); // Show the dialog
            }
        });

        // Confirm button
        mConfirm.setOnClickListener(this);
    }


    private void setCountryField(Country country) {
        String countryName;
        Drawable img;

        if(country == null) {
            mCountry.setText(R.string.selecte_a_country);
            return;
        }

        countryName = country.getName();
        img = getContext().getResources().getDrawable(country.getFlag());
        img.setBounds(0, 0, 50,50);
        mCountry.setText(countryName);
        mCountry.setCompoundDrawables(img,null,null,null);
        mCountryCode = country.getCode();
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
        mCountryCode = country.getCode();
    }

    @Override
    public void onClick(View view) {
        if(validateBillingForm()){
            Context context = getContext();
            setClientFirstName(context,mFirstName.getText().toString());
            setClientLastName(context,mName.getText().toString());
            setClientNumber(context,mPhoneNumber.getText().toString());
            setClientCity(context,mCity.getText().toString());
            setClientAddress(context,mAddress.getText().toString());
            setClientState(context,mStateProvince.getText().toString());
            setClientPostalCode(context,mPostalCode.getText().toString());
            setClientCountry(context,mCountryCode);

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
