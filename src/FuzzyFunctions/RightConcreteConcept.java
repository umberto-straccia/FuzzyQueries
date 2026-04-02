
/**
 * Fuzzy concrete concept defined with a left shoulder function.
 * @author Fernando Bobillo
 */
package FuzzyFunctions;

public class RightConcreteConcept extends FuzzyConcreteConcept
{
	/**
	 * Parameters of the funcion
	 */
	double a, b;


	public RightConcreteConcept(double k1, double k2, double a, double b) throws IllegalArgumentException
	{
		super(k1,k2);
		if (a > b)
			throw new IllegalArgumentException("Right functions require " + a + " <= " + b);
		if (k1 > a)
			throw new IllegalArgumentException("Right functions require " + k1 + " <= " + a);
		if (k2 < b)
			throw new IllegalArgumentException("Right functions require " + k2 + " >= " + b);

		this.a = a;
		this.b = b;
	}

	public RightConcreteConcept(double a, double b) throws IllegalArgumentException
	{
		super(a,b); // bad
		if (a > b)
			throw new IllegalArgumentException("Right functions require " + a + " <= " + b);
		if (k1 > a)
			throw new IllegalArgumentException("Right functions require " + k1 + " <= " + a);
		if (k2 < b)
			throw new IllegalArgumentException("Right functions require " + k2 + " >= " + b);

		this.a = a;
		this.b = b;
	}


	@Override
	public double getMembershipDegree(double x)
	{
		if (x <= a)
			return 0;
		else if (x >= b)
			return 1;
		else
			return (x - a) / (b - a);
	}


}
