package fuzzyowl2;

/**
 * Fuzzy property defined by means of an OWL 2 annotation of an OWL 2 class.
 *
 * @author Fernando Bobillo
 */
public class PropertyDefinition
{

	/** Types of fuzzy properties that can be defined using annotations.
	 */
	public enum PropertyType { MODIFIED_PROPERTY };

	private String mod;
	private String prop;
	private PropertyType type;


	public PropertyType getType ()
	{
	return type;
	}


	public void modifiedProperty(String mod, String prop)
	{
	this.mod = mod;
	this.prop = prop;
	this.type = PropertyType.MODIFIED_PROPERTY;
	}


	public String getFuzzyModifier()
	{
	return mod;
	}


	public String getProperty()
	{
	return prop;
	}


}
