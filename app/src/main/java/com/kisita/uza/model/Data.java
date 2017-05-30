package com.kisita.uza.model;


import static com.kisita.uza.model.Data.UZA.UID;

public class Data
{
	private String uid;

	/** The texts. This field contains the item name, the brand, the seller's name and a short item description */
	private String[] texts;
	/** The resources. */
	private int resources[];
	private String key = null;


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
	public Data(String[] texts, String key)
	{
		this.texts = texts;
		this.key = key;
	}

	public Data(String[] texts) {
		this.texts = texts;
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
    }
}
