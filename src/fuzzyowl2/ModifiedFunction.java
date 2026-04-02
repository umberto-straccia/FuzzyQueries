package fuzzyowl2;

/**
 * Fuzzy modified datatype.
 *
 * @author Fernando Bobillo
 */
public class ModifiedFunction extends FuzzyDatatype
{

	private String mod;
	private String d;

	public ModifiedFunction(String mod, String d)
	{
	this.mod = mod;
	this.d = d;
	}


	public String getMod()
	{
		return mod;
	}


	public String getD()
	{
		return d;
	}


	@Override
	public String toString()
	{
		return "modified-datatype( " + mod + " " + d+ ")";
	}

}
