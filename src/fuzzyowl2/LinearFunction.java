package fuzzyowl2;

/**
 * Linear function.
 *
 * @author Fernando Bobillo
 */
public class LinearFunction extends FuzzyDatatype
{

	private double a, b;

	public LinearFunction(double a, double b)
	{
		this.a = a;
		this.b = b;
	}


	public double getA()
	{
		return a;
	}


	public double getB()
	{
		return b;
	}


	@Override
	public String toString()
	{
		return "linear(" + a + ", " + b + ")";
	}

}
