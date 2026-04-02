
/**
 * Fuzzy concrete concept defined with a triangular function.
 * @author Fernando Bobillo
 */

package FuzzyFunctions;

public class TriangularConcreteConcept extends FuzzyConcreteConcept
{

	/**
	 * Parameters of the funcion
	 */
	protected double a, b, c;


	public TriangularConcreteConcept(double k1, double k2, double a, double b, double c) throws IllegalArgumentException
	{
		super(k1,k2);
		if ((a > b) || (b > c))
			throw new IllegalArgumentException("Triangular functions require " + a + " <= " + b + " <= " + c);
		if (k1 > a)
			throw new IllegalArgumentException("Triangular functions require " + k1 + " <= " + a);
		if (k2 < b)
			throw new IllegalArgumentException("Triangular functions require " + k2 + " >= " + b);

		this.a = a;
		this.b = b;
		this.c = c;
	}
	public TriangularConcreteConcept(double a, double b, double c) throws IllegalArgumentException
	{
		super(a,c); //bad
		if ((a > b) || (b > c))
			throw new IllegalArgumentException("Triangular functions require " + a + " <= " + b + " <= " + c);
		if (k1 > a)
			throw new IllegalArgumentException("Triangular functions require " + k1 + " <= " + a);
		if (k2 < b)
			throw new IllegalArgumentException("Triangular functions require " + k2 + " >= " + b);

		this.a = a;
		this.b = b;
		this.c = c;
	}


	@Override
	public double getMembershipDegree(double x)
	{
		if ((x <= a) || (x >= c))
			return 0;
		else if (x <= b)
			return (x - a) / (b - a);
		else
			return (b - x) / (c - b) + 1;
	}

}
