package fuzzyowl2;

/**
 * Right shoulder function.
 *
 * @author Fernando Bobillo
 */
public class RightShoulderFunction extends FuzzyDatatype
{

	private double a, b;

	public RightShoulderFunction(double a, double b)
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
		return "right-shoulder(" + a + ", " + b + ")";
	}

}
