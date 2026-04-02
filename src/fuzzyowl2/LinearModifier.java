package fuzzyowl2;

/**
 * Linear modifier.
 *
 * @author Fernando Bobillo
 */
public class LinearModifier extends FuzzyModifier
{

	private double c;

	public LinearModifier(double c)
	{
		this.c = c;
	}


	public double getC()
	{
		return c;
	}


	@Override
	public String toString()
	{
		return "linear(" + c + ")";
	}

}
