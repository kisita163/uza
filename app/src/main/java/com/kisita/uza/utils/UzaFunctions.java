package com.kisita.uza.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.kisita.uza.R;
import com.kisita.uza.model.Data;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/*
 * Created by HuguesKi on 07-09-17.
 */

public class UzaFunctions {
    public static String TRANSACTION_OK = "OK";
    /* Get the currency selected by the user*/
    public static String getCurrency(Context context){
        String currency;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
        currency = sharedPref.getString(context.getString(R.string.uza_currency),"EUR");
        return currency;
    }

    /* Amount format used for this application*/
    public static String setFormat(String str){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat df = new DecimalFormat("#,###,###.##",symbols);
        df.setRoundingMode(RoundingMode.CEILING);

        return df.format(Double.valueOf(str));
    }

    public static String setPriceForPayPal(String price, Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

        double usd_eur = Double.valueOf(sharedPref.getString("usd-eur","0.889098"));//0.889098;

        String mCurrency = getCurrency(context);

        double p = Double.valueOf(price.replace(",",""));

        if(mCurrency.equalsIgnoreCase("EUR")) { // From EUR to USD
            p = p/usd_eur;
        }

        return setFormat(String.valueOf(p));
    }

    /*Set amount regarding the selected currency */
    public static String setPrice(String currency,String price,Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

        /*Log.i(TAG,sharedPref.getString("eur-cdf","") + "**** done");
        Log.i(TAG,sharedPref.getString("usd-cdf","") + "**** done");
        Log.i(TAG,sharedPref.getString("usf-eur","") + "**** done");*/

        double usd_eur = Double.valueOf(sharedPref.getString("usd-eur","0.889098"));//0.889098;

        double p = getPriceDouble(price);

        String mCurrency = getCurrency(context);

        if(p >= 0) {
            if (currency.equalsIgnoreCase("USD") && mCurrency.equalsIgnoreCase("EUR")) { // From USD to EUR
                p = p * usd_eur;
            } else if (currency.equalsIgnoreCase("EUR") && mCurrency.equalsIgnoreCase("USD")) { // From EUR to USD
                p = p / usd_eur;
            }

        }else{
            p = 0;
        }

        return String.valueOf(p);
    }

    /* Multiply the amount with the item quantity*/
    public static String getCost(String cost,String quantity) {
        double price   = 0.00;
        double qty     = 0.00;
        double newCost;

        try {
            price = Double.valueOf(cost.replace(",","."));
            qty   = Double.valueOf(quantity.replace(",","."));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        newCost  =  price*qty;

        return  String.valueOf(newCost);
    }

    public static  String getShippingCost(String weight,String quantity) {

        double wght   = 0.00;
        double qty     = 0.00;
        double shippingCost;

        try {
            wght = Double.valueOf(weight.replace(",","."));
            qty   = Double.valueOf(quantity.replace(",","."));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        shippingCost = wght*qty;

        return  String.valueOf(shippingCost);
    }


    public static String printItems(ArrayList<Data> items){
        StringBuilder buf = new StringBuilder();
        for(Data d : items){
            buf.append(d.getKey());
            buf.append(System.getProperty("line.separator"));
        }

        return buf.toString();
    }

    public static ArrayList<String> getPicturesUrls(String string) {

        return  new ArrayList<>(Arrays.asList(string.split(",")));
    }

    public static double addDoubles(double a,double b){
        double x  = a  + b ;
        x = Math.ceil(x * 100) / 100;

        return x;
    }

    private void printKeyHash(Context context) {
        // Code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.kisita.uza",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash","NameNotFoundException");

        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash","NoSuchAlgorithmException");
        }
    }

    public static double getPriceDouble(String p){
        double price;
        try{
            price = Double.valueOf(p);
        }catch (NumberFormatException e){
            price = -1;
            e.printStackTrace();
        }
        return price;
    }

    public static String getCommandState(int state){

        String ret = null;

        switch (state){
            case 1 :
                ret = "Payment processing";
            break;
            case 2 :
                ret = "Packing";
                break;
            case 3 :
                ret = "Shipped";
                break;
            case 4 :
                ret = "Delivered";
                break;
            default:
                ret = "";
        }
        return ret;
    }

    public static int getCommandStateId(String state){

        int ret = -1;

        switch (state){
            case "Payment processing" :
                ret = 0;
                break;
            case "Packing":
                ret = 1;
                break;
            case "Shipped" :
                ret = 2;
                break;
            case "Delivered" :
                ret = 3;
                break;
            default:
                ret = -1;
        }
        return ret;
    }



    public static void infoAlertDialog(Context context,String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.UzaAlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, dialogClickListener).show();
    }

    public static void questionAlertDialog(Context context,String message, DialogInterface.OnClickListener dialogClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.UzaAlertDialogTheme);
        builder.setMessage(message)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no,dialogClickListener)
                .show();
    }
}
