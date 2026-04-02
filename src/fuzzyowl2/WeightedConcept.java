package fuzzyowl2;

/**
 * Weighted concept.
 *
 * @author Fernando Bobillo
 */
public class WeightedConcept extends FuzzyConcept
{

	private double n;
	private String c;


	public WeightedConcept(double n, String c)
	{
	this.n = n;
	this.c = c;
	}


	public double getWeight()
	{
	return n;
	}


	public String getFuzzyConcept()
	{
	return c;
	}


	@Override
	public String toString()
	{
	return "(" + n + " * " + c + ")";
	}

}
