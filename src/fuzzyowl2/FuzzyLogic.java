
package fuzzyowl2;

/**
 * Fuzzy logic.
 * 
 * @author Fernando Bobillo
 */
public enum FuzzyLogic
{

	LUKASIEWICZ("lukasiewicz"),


	ZADEH("zadeh");


	private String shortName;


	FuzzyLogic(String name) {
	shortName = name;
	}


	@Override
	public String toString()
	{
	return shortName;
	}

}
