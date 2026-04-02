package fuzzyowl2;

import java.util.*;

/**
 * Quantifier-guided OWA concept.
 *
 * @author Fernando Bobillo
 */
public class QowaConcept extends FuzzyConcept
{

	private String quantifier;
	private List<String> concepts;


	public QowaConcept(String quantifier, List<String> concepts)
	{
		this.quantifier = quantifier;
		this.concepts = concepts;
	}


	public String getQuantifier()
	{
		return quantifier;
	}


	public List<String> getConcepts()
	{
		return concepts;
	}


	@Override
	public String toString()
	{
		String s = "(Q-OWA [" + quantifier + "]";
		for(int i=0; i<concepts.size(); i++)
			s +=  " " + concepts.get(i);
		s += ")";
		return s;
	}

}
