package fuzzyowl2;

/**
 * Fuzzy nominal concept.
 *
 * @author Fernando Bobillo
 */
public class FuzzyNominalConcept extends FuzzyConcept
{

	private double n;
	private String i;


	public FuzzyNominalConcept(double n, String i)
	{
	this.n = n;
	this.i = i;
	}


	public double getDegree()
	{
	return n;
	}


	public String getIndividual()
	{
	return i;
	}


	@Override
	public String toString()
	{
	return "FuzzyNominalConcept(" + n + " * " + i + ")";
	}

}
