package fuzzyowl2;

/**
 * Triangular function.
 *
 * @author Fernando Bobillo
 */
public class TriangularFunction extends FuzzyDatatype
{

	private double a, b, c;

	public TriangularFunction(double a, double b, double c)
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
