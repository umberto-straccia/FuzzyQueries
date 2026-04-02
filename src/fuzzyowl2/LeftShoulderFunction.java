package fuzzyowl2;

/**
 * Left shoulder function.
 *
 * @author Fernando Bobillo
 */
public class LeftShoulderFunction extends FuzzyDatatype
{

	private double a, b;

	public LeftShoulderFunction(double a, double b)
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
		return "left-shoulder(" + a + ", " + b + ")";
	}

}
