package com.kisita.uza.model;

import android.database.Cursor;
import android.util.Log;

import com.kisita.uza.provider.UzaContract;

/**
 * Created by HuguesKi on 19-04-18.
 */

public class Command extends Data {

    private  String mUser;

    private String mAddress;

    private String mNumber;

    private String mPostal;

    private String mCity;

    private String mCountry;

    private String mProvince;

    private String mLname;

    private String mFname;

    private String mCommandId;

    private String mCommandState;

    public static final String[] ITEMS_COMMANDS_COLUMNS = {
            UzaContract.ItemsEntry.TABLE_NAME + "." + UzaContract.ItemsEntry._ID,
            UzaContract.ItemsEntry.COLUMN_NAME,
            UzaContract.ItemsEntry.COLUMN_PRICE,
            UzaContract.ItemsEntry.COLUMN_TYPE,
            UzaContract.ItemsEntry.COLUMN_CURRENCY,
            UzaContract.ItemsEntry.COLUMN_BRAND,
            UzaContract.ItemsEntry.COLUMN_DESCRIPTION,
            UzaContract.ItemsEntry.COLUMN_SELLER,
            UzaContract.ItemsEntry.TABLE_NAME + "." +UzaContract.ItemsEntry.COLUMN_SIZE,
            UzaContract.ItemsEntry.COLUMN_AUTHOR,
            UzaContract.ItemsEntry.COLUMN_PICTURES,
            UzaContract.ItemsEntry.COLUMN_WEIGHT,
            UzaContract.ItemsEntry.COLUMN_URL,
            UzaContract.CommandsEntry.COLUMN_QUANTITY,
            UzaContract.CommandsEntry.COLUMN_STATE,
            UzaContract.CommandsEntry.COLUMN_ADDRESS,
            UzaContract.CommandsEntry.COLUMN_POSTAl,
            UzaContract.CommandsEntry.COLUMN_CITY,
            UzaContract.CommandsEntry.COLUMN_PROVINCE,
            UzaContract.CommandsEntry.COLUMN_COUNTRY,
            UzaContract.CommandsEntry.COLUMN_NUMBER,
            UzaContract.CommandsEntry.COLUMN_USER,
            UzaContract.CommandsEntry.COLUMN_FNAME,
            UzaContract.CommandsEntry.COLUMN_LNAME,
            UzaContract.CommandsEntry.TABLE_NAME + "." +UzaContract.CommandsEntry._ID
    };

    /**
     * Instantiates a new data.
     *
     * @param data     the Cursor containing data
     */
    public Command(Cursor data) {
        super(data);
        int index = 0;
        for (String s :
                data.getColumnNames()) {
            Log.i(TAG,"column " + s + " ,index "+ index + " ,command : " +  data.getString(index));
            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_STATE)) {
                this.mCommandState = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_ADDRESS)) {
                this.mAddress = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_POSTAl)) {
                this.mPostal = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_CITY)) {
                this.mCity = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_PROVINCE)) {
                this.mProvince = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_COUNTRY)) {
                this.mCountry = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_NUMBER)) {
                this.mNumber = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_FNAME)) {
                this.mFname = data.getString(index);

            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_LNAME)) {
                this.mFname = data.getString(index);
            }

            if (s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_USER)) {
                this.mUser = data.getString(index);
            }

            if ( s.equalsIgnoreCase("_id") && index > 0) {
                Log.i(TAG,"Command id is  : "+ index + " " + data.getString(index));
                this.mCommandId = data.getString(index);
                continue;
            }
            index++;
        }
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getPostal() {
        return mPostal;
    }

    public void setPostal(String mPostal) {
        this.mPostal = mPostal;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getProvince() {
        return mProvince;
    }

    public void setProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public String getLastName() {
        return mLname;
    }

    public void setLastName(String mLname) {
        this.mLname = mLname;
    }

    public String getFirstName() {
        return mFname;
    }

    public void setFirstName(String mFname) {
        this.mFname = mFname;
    }

    public void setCommandState(String mCommandState) {
        this.mCommandState = mCommandState;
    }

    public String getUser() {
        return mUser;
    }

    public int getCommandState(){
        int state = -1;
        try{
            state = Integer.valueOf(this.mCommandState);
        }catch(NumberFormatException e){
            Log.e(TAG, "Command state is not well formatted " + e.getMessage());
        }catch(Exception e){
            Log.e(TAG, "Unknown error when formatting the state " + e.getMessage());
        }
        return state;
    }

    public String getCommandId() {
        return mCommandId;
    }
}
