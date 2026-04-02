package fuzzyowl2;

/**
 * Fuzzy modified concept.
 *
 * @author Fernando Bobillo
 */
public class ModifiedConcept extends FuzzyConcept
{

	private String mod;
	private String c;


	public ModifiedConcept(String mod, String c)
	{
	this.mod = mod;
	this.c = c;
	}


	public String getFuzzyModifier()
	{
	return mod;
	}


	public String getFuzzyConcept()
	{
	return c;
	}


	@Override
	public String toString()
	{
	return "(ModifiedConcept " + mod + " " + c + ")";
	}

}
