
/**
 * Fuzzy concrete concept defined with a left shoulder function.
 * @author Fernando Bobillo
 */
package FuzzyFunctions;

public class LeftConcreteConcept extends FuzzyConcreteConcept
{
	/**
	 * Parameters of the function
	 */
	public double a, b;


	public LeftConcreteConcept(double k1, double k2, double a, double b) throws IllegalArgumentException
	{
		super(k1,k2);
		if (a > b)
			throw new IllegalArgumentException("Left functions require " + a + " <= " + b);
		if (k1 > a)
			throw new IllegalArgumentException("Left functions require " + k1 + " <= " + a);
		if (k2 < b)
			throw new IllegalArgumentException("Left functions require " + k2 + " >= " + b);

		this.a = a;
		this.b = b;
	}

	public LeftConcreteConcept(double a, double b) throws IllegalArgumentException
	{
		super(a,b); // bad
		if (a > b)
			throw new IllegalArgumentException("Left functions require " + a + " <= " + b);
		if (k1 > a)
			throw new IllegalArgumentException("Left functions require " + k1 + " <= " + a);
		if (k2 < b)
			throw new IllegalArgumentException("Left functions require " + k2 + " >= " + b);

		this.a = a;
		this.b = b;
	}
	

	@Override
	public double getMembershipDegree(double x)
	{
        //System.out.println("fQ a, b, x :" + a + " , " + b + " , " + x);
		
		if (x <= a)
			return 1;
		else if (x >= b)
			return 0;
		else
//			return (b - x) / (b - a);
			return (a - x) / (b - a) + 1;
	}
	


}
