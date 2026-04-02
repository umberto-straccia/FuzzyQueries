package fuzzyowl2;

/**
 * Triangular modifier.
 *
 * @author Fernando Bobillo
 */
public class TriangularModifier extends FuzzyModifier
{

	private double a, b, c;


	public TriangularModifier(double a, double b, double c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}


	public double getA()
	{
		return a;
	}


	public double getB()
	{
		return b;
	}


	public double getC()
	{
		return c;
	}


	@Override
	public String toString()
	{
		return "triangular(" + a + ", " + b + ", " + c + ")";
	}


}
