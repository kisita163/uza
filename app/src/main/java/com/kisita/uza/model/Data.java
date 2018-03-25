package com.kisita.uza.model;


import android.database.Cursor;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisita.uza.provider.UzaContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kisita.uza.utils.UzaFunctions.getPicturesUrls;

public class Data implements Serializable
{
    public static int ITEM_DATA      = 0;
    public static int COMMAND_DATA   = 1;
    public static int FAVOURITE_DATA = 2;

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

	private String mDescription = "";

	private String mSize        = "";

    private String mCurrency    = "";

    //private String mUrl;

    private String mWeight;

    private String mType;

    //private String mCategory;

    private String mSeller;

    //private String mBrand;

    private String mPrice;

    private String mQuantity;

    private String mCommandId;

    /**
     * Instantiates a new data.
     *
     * @param data
     *            the Cursor containing data
     */
	public Data(Cursor data,int dataType) {
	    int index = 0;
		for (String s :
			 data.getColumnNames()) {
            //Log.i("### Data","column " + s);
			if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_NAME))
				this.mName = data.getString(index);

			if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_AUTHOR))
				this.mAuthor = data.getString(index);

			if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_DESCRIPTION))
				this.mDescription = data.getString(index);

            if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_CURRENCY))
                this.mCurrency = data.getString(index);

			if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_SIZE))
				this.mSize = data.getString(index);

            if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_PICTURES)){
                this.pictures =  getPicturesUrls(data.getString(index));
            }

            if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_PRICE)){
                this.mPrice =  data.getString(index);
            }

            /*if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_BRAND)){
                this.mBrand = data.getString(index);
            }*/

            if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_SELLER)){
                this.mSeller =  data.getString(index);
            }

            /*if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_CATEGORY)){
                this.mCategory = data.getString(index);
            }*/

            if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_TYPE)){
                this.mType = data.getString(index);
            }

            if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_WEIGHT)){
                this.mWeight = data.getString(index);
            }

            /*if(s.equalsIgnoreCase(UzaContract.ItemsEntry.COLUMN_URL)){
                this.mUrl = data.getString(index);
            }*/

            if(s.equalsIgnoreCase(UzaContract.LikesEntry.COLUMN_LIKES)){
                if(data.getString(index) != null)
                    this.mFavourite = true;
            }

            if(s.equalsIgnoreCase("_id")){
                if(index == 0){
                    this.mItemId = data.getString(0);
                }else {
                    if (data.getString(index) != null && dataType == FAVOURITE_DATA) {
                        this.mFavouriteId = data.getString(index);
                    }

                    if(data.getString(index) != null && dataType == COMMAND_DATA){
                        this.mCommandId = data.getString(index);
                    }
                }
            }

            if(s.equalsIgnoreCase(UzaContract.CommandsEntry.COLUMN_QUANTITY)){
                this.mQuantity =  data.getString(index);
            }
            index++;
		}
	}
	public String getKey() {
			return null;
	}

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

	public static final String[] ITEMS_COLUMNS = {
	        UzaContract.ItemsEntry.TABLE_NAME + "." + UzaContract.ItemsEntry._ID,
            UzaContract.ItemsEntry.COLUMN_NAME,
            UzaContract.ItemsEntry.COLUMN_PRICE,
            UzaContract.ItemsEntry.COLUMN_CURRENCY,
            UzaContract.ItemsEntry.COLUMN_BRAND,
            UzaContract.ItemsEntry.COLUMN_DESCRIPTION,
            UzaContract.ItemsEntry.COLUMN_SELLER,
            UzaContract.ItemsEntry.COLUMN_CATEGORY,
            UzaContract.ItemsEntry.COLUMN_TYPE,
            UzaContract.ItemsEntry.COLUMN_AUTHOR,
            UzaContract.ItemsEntry.TABLE_NAME + "." +UzaContract.ItemsEntry.COLUMN_SIZE,
            UzaContract.ItemsEntry.COLUMN_PICTURES,
            UzaContract.ItemsEntry.COLUMN_WEIGHT,
            UzaContract.ItemsEntry.COLUMN_URL,
			UzaContract.LikesEntry.COLUMN_LIKES,
            UzaContract.LikesEntry.TABLE_NAME + "." +UzaContract.LikesEntry._ID
    };

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
			UzaContract.CommandsEntry.TABLE_NAME + "." +UzaContract.CommandsEntry._ID
	};

	public static final String [] FAVOURITES_COLUMNS = {
			UzaContract.LikesEntry.TABLE_NAME + "." + UzaContract.LikesEntry._ID,
			UzaContract.LikesEntry.COLUMN_LIKES
	};


	public static final String[] COMMANDS_COLUMNS = {
			UzaContract.CommandsEntry.TABLE_NAME + "." + UzaContract.CommandsEntry._ID,
			UzaContract.CommandsEntry.COLUMN_KEY,
			UzaContract.CommandsEntry.TABLE_NAME + "." +UzaContract.CommandsEntry.COLUMN_SIZE,
			UzaContract.CommandsEntry.TABLE_NAME + "." +UzaContract.CommandsEntry.COLUMN_COLOR,
			UzaContract.CommandsEntry.COLUMN_COMMENT,
			UzaContract.CommandsEntry.COLUMN_QUANTITY,
			UzaContract.CommandsEntry.COLUMN_STATE
	};

	public boolean isFavourite() {
		return mFavourite;
	}

	public String getItemId() {
		return mItemId;
	}

	private String getFavouriteId() {
		return mFavouriteId;
	}

	private void setFavouriteId(String mFavouriteId) {
		this.mFavouriteId = mFavouriteId;
	}

	public String getName() {
		return mName;
	}

	public String getDescription() {
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


	private void updateFavouriteInDataBase(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

		if (isFavourite()) {
		    Log.i("### Data","Update database ("+isFavourite()+")");
			DatabaseReference likes = getDb().child("users-data").child(uid).child("likes");
			likes.child(getFavouriteId()).removeValue();

		} else {
            Log.i("### Data","Update database ("+isFavourite()+")");

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
}
