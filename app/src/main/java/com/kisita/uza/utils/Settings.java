package com.kisita.uza.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kisita.uza.R;

/*
 * Created by HuguesKi on 09-04-18.
 */

public class Settings {


    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(context.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context){
        return getSharedPreferences(context).edit();
    }

    public static String getClientFirstName(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_first_name),"");

        return checkStringValue(value);
    }

    public static String getClientLastName(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_name),"");

        return checkStringValue(value);
    }

    public static String getClientNumber(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_phone),"");

        return checkStringValue(value);
    }

    public static String getClientAddress(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_address),"");

        return checkStringValue(value);
    }

    public static String getClientCity(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_city),"");

        return checkStringValue(value);
    }

    public static String getClientState(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_state_province),"");

        return checkStringValue(value);
    }

    public static String getClientPostalCode(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_postal_code),"");

        return checkStringValue(value);
    }

    public static String getClientCountry(Context context){
        String value  = getSharedPreferences(context).getString(context.
                getString(R.string.uza_billing_country),"");

        return checkStringValue(value);
    }

    public static void setClientFirstName(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_first_name),value).apply();
    }

    public static void setClientLastName(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_name),value).apply();
    }

    public static void setClientNumber(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_phone),value).apply();
    }

    public static void setClientAddress(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_address),value).apply();
    }

    public static void setClientPostalCode(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_postal_code),value).apply();
    }

    public static void setClientCity(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_city),value).apply();
    }

    public static void setClientState(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_state_province),value).apply();
    }

    public static void setClientCountry(Context context,String value){
        getSharedPreferencesEditor(context).putString(context.getString(R.string.uza_billing_country),value).apply();
    }

    public static boolean isAllBillingInformationSet(Context context){
        return !(getClientFirstName(context)   == null ||
                 getClientLastName(context)   == null ||
                 getClientNumber(context)     == null ||
                 getClientAddress(context)    == null ||
                 getClientCity(context)       == null ||
                 getClientState(context)      == null ||
                 getClientPostalCode(context) == null ||
                 getClientCountry(context)    == null);
    }

    private static String checkStringValue(String s){
        return (s.equalsIgnoreCase("")) ? null : s;
    }
}
