package com.kisita.uza.model;


public class Data
{
	private String uid;

	/** The texts. This field contains the item name, the brand, the seller's name and a short item description */
	private String[] texts;
	/** The resources. */
	private int resources[];
	private byte[] mPicBytes;  // Pictures


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
		setPriceCurrency();
	}

	public Data(String[] texts, byte[] b) {
		this.texts = texts;
		this.mPicBytes = b;
		setPriceCurrency();
	}

	public Data(String[] texts) {
		this.texts = texts;
		setPriceCurrency();
	}

	public byte[] getmPicBytes() {
		return mPicBytes;
	}

	public void setmPicBytes(byte[] mPicBytes) {
		this.mPicBytes = mPicBytes;
	}

	public void setPriceCurrency() {
		String price;

		if(texts.length > 1) {
			if (texts[UzaData.CURRENCY.ordinal()].equals("US")) {
				price = texts[UzaData.PRICE.ordinal()] + " $";
			} else if (texts[UzaData.CURRENCY.ordinal()].equals("EUR")) {
				price = texts[UzaData.PRICE.ordinal()] + " €";
			} else if (texts[UzaData.CURRENCY.ordinal()].equals("FC")) {
				price = texts[UzaData.PRICE.ordinal()] + " FC";
			} else {
				price = texts[UzaData.PRICE.ordinal()] + " $";
			}

			texts[UzaData.PRICE.ordinal()] = price;
		}
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
	 * Sets the texts.
	 *
	 * @param texts
	 *            the new texts
	 */
	public void setTexts(String[] texts)
	{
		this.texts = texts;
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
		return texts[UzaData.UID.ordinal()];
	}


	public enum UzaData {UID, NAME, PRICE, CURRENCY, BRAND, DESCRIPTION, SELLER, CATEGORY, PICTURES}
}
