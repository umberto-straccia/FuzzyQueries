package fuzzyowl2;

/**
 * Fuzzy modified property.
 *
 * @author Fernando Bobillo
 */
public class ModifiedProperty extends FuzzyProperty
{

	private String mod;
	private String prop;


	public ModifiedProperty(String mod, String prop)
	{
	this.mod = mod;
	this.prop = prop;
	}


	public String getFuzzyModifier()
	{
	return mod;
	}


	public String getFuzzyProperty()
	{
	return prop;
	}

}
