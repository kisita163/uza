package com.kisita.uza.model;


import java.io.Serializable;
import java.util.ArrayList;

import static com.kisita.uza.model.Data.UZA.UID;

public class Data implements Serializable
{
	private String uid;

	/** The texts. This field contains the item name, the brand, the seller's name and a short item description */
	private String[] texts;
	/** The resources. */
	private int resources[];

	private String key = null;

	private String[] commandDetails;

	private ArrayList<String> pictures;


	/**
	 * Instantiates a new data.
	 *
	 * @param texts
	 *            the texts
	 * @param resources
	 *            the resources
	 */
	public Data(String[] texts, int resources[])
	{
		this.texts = texts;
		this.resources = resources;
	}
	// Data in cart
	public Data(String[] texts, String key, ArrayList<String> pictures )
	{
		this.texts = texts;
		this.key = key;
		this.pictures = pictures;
	}

	public Data(String[] texts) {
		this.texts = texts;
	}

	public Data(String[] texts,String key,String[] commandDetails) {
		this.texts = texts;
		this.key = key;
		this.commandDetails = commandDetails;
	}

	public Data(String[] texts, ArrayList<String> pictures) {
		this.texts = texts;
		this.pictures = pictures;
	}

	/**
	 * Gets the texts.
	 *
	 * @return the texts
	 */
	public String[] getTexts()
	{
		return texts;
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
		return texts[UID];
	}

	public String getKey() {
		return key;
	}

	public String[] getCommandDetails() {
		return commandDetails;
	}

	public ArrayList<String> getPictures() {
		return pictures;
	}

	public class UZA {
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
    }
}
