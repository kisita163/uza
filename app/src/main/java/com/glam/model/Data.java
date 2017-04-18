package com.glam.model;

/**
 * The Class Data is simple Java bean class that holds two members only to
 * represent dummy data for app. You can customize or can write new bean classes
 * as per you needs.
 */
public class Data
{

	/** The texts. */
	private String[] texts;

	/** The resources. */
	private int resources[];

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

}
