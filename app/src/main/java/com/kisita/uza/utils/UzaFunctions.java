package com.kisita.uza.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kisita.uza.R;
import com.kisita.uza.model.Data;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/*
 * Created by HuguesKi on 07-09-17.
 */

public class UzaFunctions {
    /* Get the currency selected by the user*/
    public static String getCurrency(Context context){
        String currency;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
        currency = sharedPref.getString(context.getString(R.string.uza_currency),"CDF");
        return currency;
    }

    /* Amount format used for this application*/
    public static String setFormat(String str){
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat df = new DecimalFormat("#,###,###.##",symbols);
        df.setRoundingMode(RoundingMode.CEILING);

        return df.format(Double.valueOf(str));
    }

    /*Set amount regarding the selected currency */
    public static String setPrice(String currency,String price,Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

        /*Log.i(TAG,sharedPref.getString("eur-cdf","") + "**** done");
        Log.i(TAG,sharedPref.getString("usd-cdf","") + "**** done");
        Log.i(TAG,sharedPref.getString("usf-eur","") + "**** done");*/

        // euros to cdf
        double eur_cdf = Double.valueOf(sharedPref.getString("eur-cdf","1623.58"));//1623.58;// 1 euros = 1623.58 fc;
        // usd to cdf
        double usd_cdf = Double.valueOf(sharedPref.getString("usd-cdf","1443.86"));//1443.86;// 1 usd   = 1443.86 fc;

        double usd_eur = Double.valueOf(sharedPref.getString("usd-eur","0.889098"));//0.889098;

        double p = Double.valueOf(price);

        String mCurrency = getCurrency(context);


        if(currency.equalsIgnoreCase("CDF") && mCurrency.equalsIgnoreCase("EUR")){
            p = p/eur_cdf;
        }else if(currency.equalsIgnoreCase("EUR") && mCurrency.equalsIgnoreCase("CDF")){
            p = Math.ceil(p*eur_cdf);
        }else if(currency.equalsIgnoreCase("USD") && mCurrency.equalsIgnoreCase("EUR")) {
            p = p*usd_eur;
        }else if(currency.equalsIgnoreCase("EUR") && mCurrency.equalsIgnoreCase("USD")) {
            p = p/usd_eur;
        }else if(currency.equalsIgnoreCase("USD") && mCurrency.equalsIgnoreCase("CDF")) {
            p = Math.ceil(p*usd_cdf);
        }else if(currency.equalsIgnoreCase("CDF") && mCurrency.equalsIgnoreCase("USD")) {
            p = p / usd_cdf;
        }

        if(mCurrency.equalsIgnoreCase("CDF")){
            // round to the next hundredth
            p = p + 100 - p%100;
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
}
