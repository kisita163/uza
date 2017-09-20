package com.kisita.uza.model;


import com.kisita.uza.provider.UzaContract;

import java.io.Serializable;
import java.util.ArrayList;

import static com.kisita.uza.model.Data.UZA.UID;

public class Data implements Serializable
{
	private String uid;

	/** The data. This field contains the item name, the brand, the seller's name and a short item description */
	private String[] data;
	/** The resources. */
	private int resources[];

	private String key = null;

	private ArrayList<String> pictures;


	/**
	 * Instantiates a new data.
	 *
	 * @param texts
	 *            the data
	 * @param resources
	 *            the resources
	 */
	public Data(String[] texts, int resources[])
	{
		this.data = texts;
		this.resources = resources;
	}
	// Data in cart
	public Data(String[] data, String key, ArrayList<String> pictures )
	{
		this.data = data;
		this.key = key;
		this.pictures = pictures;
	}

	public Data(String[] texts) {
		this.data = texts;
	}


	public Data(String[] texts, ArrayList<String> pictures) {
		this.data = texts;
		this.pictures = pictures;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public String[] getData()
	{
		return data;
	}

	/**
	 * Gets the resources.
	 *
	 * @return the resources
	 */
	public int[] getResources()
	{
		return resources;
	}

	/**
	 * Sets the resources.
	 *
	 * @param resources
	 *            the new resources
	 */
	public void setResources(int[] resources)
	{
		this.resources = resources;
	}

	public String getUid() {
		return data[UID];
	}

	public String getKey() {
		return key;
	}

	public ArrayList<String> getPictures() {
		return pictures;
	}

	public static  class UZA {
        public static final int UID         = 0;
        public static final int NAME        = 1;
        public static final int PRICE       = 2;
        public static final int CURRENCY    = 3;
        public static final int BRAND       = 4;
        public static final int DESCRIPTION = 5;
        public static final int SELLER      = 6;
        public static final int CATEGORY    = 7;
        public static final int TYPE        = 8;
        public static final int COLOR       = 9;
        public static final int SIZE        = 10;
        public static final int PICTURES    = 11;
        public static final int WEIGHT      = 12;
        public static final int URL         = 13;
        public static final int QUANTITY    = 14;
        public static final int KEY         = 15;
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
		UzaContract.ItemsEntry.TABLE_NAME + "." +UzaContract.ItemsEntry.COLUMN_COLOR,
		UzaContract.ItemsEntry.TABLE_NAME + "." +UzaContract.ItemsEntry.COLUMN_SIZE,
		UzaContract.ItemsEntry.COLUMN_PICTURES,
		UzaContract.ItemsEntry.COLUMN_WEIGHT,
		UzaContract.ItemsEntry.COLUMN_URL
    };

	public static final String[] ITEMS_COMMANDS_COLUMNS = {
			UzaContract.ItemsEntry.TABLE_NAME + "." + UzaContract.ItemsEntry._ID,
			UzaContract.ItemsEntry.COLUMN_NAME,
			UzaContract.ItemsEntry.COLUMN_PRICE,
			UzaContract.ItemsEntry.COLUMN_CURRENCY,
			UzaContract.ItemsEntry.COLUMN_BRAND,
			UzaContract.ItemsEntry.COLUMN_DESCRIPTION,
			UzaContract.ItemsEntry.COLUMN_SELLER,
			UzaContract.ItemsEntry.COLUMN_CATEGORY,
			UzaContract.ItemsEntry.COLUMN_TYPE,
			UzaContract.ItemsEntry.TABLE_NAME + "." +UzaContract.ItemsEntry.COLUMN_COLOR,
			UzaContract.ItemsEntry.TABLE_NAME + "." +UzaContract.ItemsEntry.COLUMN_SIZE,
			UzaContract.ItemsEntry.COLUMN_PICTURES,
			UzaContract.ItemsEntry.COLUMN_WEIGHT,
			UzaContract.ItemsEntry.COLUMN_URL,
			UzaContract.CommandsEntry.COLUMN_QUANTITY,
			UzaContract.CommandsEntry.TABLE_NAME + "." +UzaContract.CommandsEntry._ID
	};

	public static final String [] FAVOURITES_COLUMNS = {
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
}
