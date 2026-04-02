package fuzzyowl2;

import java.util.*;

/**
 * OWA concept.
 *
 * @author Fernando Bobillo
 */
public class ChoquetConcept extends FuzzyConcept
{

	private List<Double> weights;
	private List<String> concepts;


	public ChoquetConcept(List<Double> weights, List<String> concepts)
	{
		this.weights = weights;
		this.concepts = concepts;
	}


	public List<Double> getWeights()
	{
		return weights;
	}


	public List<String> getConcepts()
	{
		return concepts;
	}


	@Override
	public String toString()
	{
		String s = "(Choquet [";
		for(int i=0; i<weights.size(); i++)
			s +=  " " + weights.get(i).toString();
		s += "]";
		for(int i=0; i<concepts.size(); i++)
			s +=  " " + concepts.get(i);
		s += ")";
		return s;
	}

}
