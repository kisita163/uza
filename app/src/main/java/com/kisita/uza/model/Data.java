package com.kisita.uza.model;


import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisita.uza.internal.BiLog;
import com.kisita.uza.provider.UzaContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kisita.uza.utils.UzaFunctions.getDescArray;
import static com.kisita.uza.utils.UzaFunctions.getPicturesUrls;

public class Data implements Serializable, Comparable<Data>
{
	public static String TAG = "### Data";

	private String mAvailability;

	/* List of picture(s) associated to this item */
	private ArrayList<String> pictures;

	/* True if the this item is listed as a favourite one */
	private boolean mFavourite = false;

    /* Item id in Firebase database */
	private String mItemId;

	/* Favourite id in Firebase database */
	private String mFavouriteId;

	private String mAuthor      = "";

	private String mName        = "";

	private ArrayList<String> mDescription;

	private String mSize        = "";

    private String mCurrency    = "";

    //private String mUrl;

    private String mWeight;

    private String mType;

    //private String mCategory;

    private String mSeller;

    //private String mBrand;

    private String mPrice;

    private String mQuantity     = "";

    private String mCommandState = "";

    private String mCommandId = "";

	public ArrayList<String> getPictures() {
		return pictures;
	}

	public void setFavourite(boolean favourite) {
		this.mFavourite = favourite;
	}

	public void updateFavourite(boolean favourite){
        updateFavouriteInDataBase();
        this.mFavourite = favourite;
    }

	public String getCommandId() {
		return mCommandId;
	}

	public Data(Cursor data){
		this.mItemId       = data.getString(0);
		this.mName         = data.getString(1);
		this.mPrice        = data.getString(2);
		this.mCurrency     = data.getString(3);
		this.mDescription  = getDescArray(data.getString(5));
		this.mSeller       = data.getString(6);
		this.mType         = data.getString(8);
		this.mAuthor       = data.getString(9);
		this.mSize         = data.getString(10);
		this.pictures      = getPicturesUrls(data.getString(11));
		this.mWeight       = data.getString(12);
		this.mAvailability = data.getString(14);
		this.mQuantity     = data.getString(15);
		this.mCommandState = data.getString(17);
		this.mCommandId    = data.getString(18);
		this.mFavouriteId  = data.getString(20);

		if(data.getString(19) != null)
			this.mFavourite = true;
	}

	// Changing this array imply  updating Data(Cursor data) constructor
	public static final String[] ITEMS_COLUMNS = {
	        UzaContract.ItemsEntry.TABLE_NAME + "." + UzaContract.ItemsEntry._ID, //0
            UzaContract.ItemsEntry.COLUMN_NAME,//1
            UzaContract.ItemsEntry.COLUMN_PRICE,//2
            UzaContract.ItemsEntry.COLUMN_CURRENCY,//3
            UzaContract.ItemsEntry.COLUMN_BRAND,//4
            UzaContract.ItemsEntry.COLUMN_DESCRIPTION,//5
            UzaContract.ItemsEntry.COLUMN_SELLER,//6
            UzaContract.ItemsEntry.COLUMN_CATEGORY,//7
            UzaContract.ItemsEntry.COLUMN_TYPE,//8
            UzaContract.ItemsEntry.COLUMN_AUTHOR,//9
            UzaContract.ItemsEntry.TABLE_NAME + "." +UzaContract.ItemsEntry.COLUMN_SIZE,//10
            UzaContract.ItemsEntry.COLUMN_PICTURES,//11
            UzaContract.ItemsEntry.COLUMN_WEIGHT,//12
            UzaContract.ItemsEntry.COLUMN_URL,//13
            UzaContract.ItemsEntry.COLUMN_AVAILABILITY,//14
			UzaContract.CommandsEntry.COLUMN_QUANTITY,//15
            UzaContract.CommandsEntry.COLUMN_KEY,//16
            UzaContract.CommandsEntry.COLUMN_STATE,//17
			UzaContract.CommandsEntry.TABLE_NAME + "." +UzaContract.CommandsEntry._ID,//18
			UzaContract.LikesEntry.COLUMN_LIKES,//19
            UzaContract.LikesEntry.TABLE_NAME + "." +UzaContract.LikesEntry._ID//20
    };

	public boolean isFavourite() {
		return mFavourite;
	}

	public String getItemId() {
		return mItemId;
	}

	public String getFavouriteId() {
		return mFavouriteId;
	}

	public void setFavouriteId(String mFavouriteId) {
		this.mFavouriteId = mFavouriteId;
	}

	public String getName() {
		return mName;
	}

	public ArrayList<String> getDescription() {
		return mDescription;
	}

	public String getSize() {
		return mSize;
	}

	public String getAuthor(){
		return mAuthor;
	}

    public String getCurrency() {
        return mCurrency;
    }

    /*public String geUrl() {
        return mUrl;
    }*/

    public String getWeight() {
        return mWeight;
    }

    public String getType() {
        return mType;
    }

    /*public String getCategory() {
        return mCategory;
    }*/

    public String getSeller() {
        return mSeller;
    }

    /*public String getBrand() {
        return mBrand;
    }*/

    public String getPrice() {
        return mPrice;
    }


    public String getQuantity() {
        return mQuantity;
    }

    private DatabaseReference getDb() {
		return FirebaseDatabase.getInstance().getReference();
	}

	public boolean isAvailable() {
		return mAvailability != null && mAvailability.equalsIgnoreCase("0");
	}

	private void updateFavouriteInDataBase(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

		if (isFavourite()) {
		    BiLog.i("### Data","Update database ("+isFavourite()+")");
			DatabaseReference likes = getDb().child("users-data").child(uid).child("likes");
			likes.child(getFavouriteId()).removeValue();

		} else {
            BiLog.i("### Data","Update database ("+isFavourite()+")");

			String like = getDb().child("users").push().getKey(); // Get a new firebase key

            setFavouriteId(like); // Update the item favourite Id

			Map<String, Object> childUpdates = new HashMap<>();

			childUpdates.put("/users-data/" +  uid+ "/likes/" + like, getItemId());

			getDb().updateChildren(childUpdates);
		}
	}

    public void setQuantity(String quantity) {
	    this.mQuantity = quantity;
    }

    public boolean isCommand(){
		boolean check = false;
		if(getCommandState() > 0)
			check = true;
		return check;
	}

	public boolean  isInCart(){
		return mCommandState != null && mCommandState.equalsIgnoreCase("0");
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

	@Override
	public int compareTo(@NonNull Data data) {
	    BiLog.i(TAG,"Comparator in Data "+ this.getItemId() + " - " + data.getItemId());
		return this.getItemId().compareTo(data.getItemId());
	}
}
