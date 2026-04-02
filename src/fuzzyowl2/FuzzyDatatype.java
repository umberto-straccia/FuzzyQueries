package fuzzyowl2;

/**
 * Abstract class representing a fuzzy datatype.
 *
 * @author Fernando Bobillo
 */
public abstract class FuzzyDatatype {

	/**
	 * Minimum value
	 */
	private double k1;

	/**
	 * Maximum value
	 */
	private double k2;


	/**
	 * Sets the minimum value of the datatype.
	 * @param min Minimum value of the datatype.
	 */
	public void setMinValue(double min)
	{
		k1 = min;
	}


	/**
	 * Sets the maximum value of the datatype.
	 * @param max Maximum value of the datatype.
	 */
	public void setMaxValue(double max)
	{
		k2 = max;
	}


	/**
	 * Gets the minimum value of the datatype.
	 * @return Minimum value of the datatype.
	 */
	public double getMinValue()
	{
		return k1;
	}


	/**
	 * Gets the maximum value of the datatype.
	 * @return Maximum value of the datatype.
	 */
	public double getMaxValue()
	{
		return k2;
	}

}
