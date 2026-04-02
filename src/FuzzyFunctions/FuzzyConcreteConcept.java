

/**
 * Fuzzy concrete concept defined with an explicit membership function.
 * @author Fernando Bobillo
 */
package FuzzyFunctions;

public abstract class FuzzyConcreteConcept
{

	private static final long serialVersionUID = -2493279357925543693L;

	double k1, k2;

	public FuzzyConcreteConcept(double k1, double k2) {
		this.k1 = k1;
		this.k2 = k2;
	}


	/**
	 * Sets the value of the parameter k1.
	 * @param k1 New value of the parameter.
	 */
	public void setK1(double k1)
	{
		this.k1 = k1;
	}


	/**
	 * Sets the value of the parameter k2.
	 * @param k2 New value of the parameter.
	 */
	public void setK2(double k2)
	{
		this.k2 = k2;
	}


	/**
	 * Gets the image in [0,1] of a real number to the explicit membership function.
	 * @param x A real number in the range of values of the explicit membership function.
	 * @return Image in [0,1] of x to the explicit membership function.
	 */
	public abstract double
	getMembershipDegree(double x);


}
