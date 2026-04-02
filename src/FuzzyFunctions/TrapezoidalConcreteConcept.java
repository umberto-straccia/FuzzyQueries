
/**
 * Fuzzy concrete concept defined with a triangular function.
 * @author Fernando Bobillo
 */

package FuzzyFunctions;

public class TrapezoidalConcreteConcept extends FuzzyConcreteConcept
{

	/**
	 * Parameters of the funcion
	 */
	protected double a, b, c, d;

	public TrapezoidalConcreteConcept(double k1, double k2, double a, double b, double c, double d) throws IllegalArgumentException
	{
		super(k1,k2);
		if ((a > b) || (b > c) || (c > d))
			throw new IllegalArgumentException("Trapezoidal functions require " + a + " <= " + b + " <= " + c + " <= " + d);
		if (k1 > a)
			throw new IllegalArgumentException("Trapezoidal functions require " + k1 + " <= " + a);
		if (k2 < d)
			throw new IllegalArgumentException("Trapezoidal functions require " + k2 + " >= " + d);

		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	public TrapezoidalConcreteConcept(double a, double b, double c, double d) throws IllegalArgumentException
	{
		super(a,d); //bad
		if ((a > b) || (b > c) || (c > d))
			throw new IllegalArgumentException("Trapezoidal functions require " + a + " <= " + b + " <= " + c + " <= " + d);
		if (k1 > a)
			throw new IllegalArgumentException("Trapezoidal functions require " + k1 + " <= " + a);
		if (k2 < d)
			throw new IllegalArgumentException("Trapezoidal functions require " + k2 + " >= " + d);

		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	@Override
	public double getMembershipDegree(double x)
	{
		if (x <= a)
			return 0;
		else if (x <= b)
			return (x - a) / (b - a);
		else if (x <= c)
			return 1;
		else if (x <= d)
			return (c - x) / (d - c) + 1;
		else
			return 0;
	}
}
