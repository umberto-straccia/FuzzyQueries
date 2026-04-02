package fuzzyowl2;

import java.util.*;

/**
 * Weighted sum concept.
 *
 * @author Fernando Bobillo
 */
public class WeightedSumConcept extends FuzzyConcept
{

	private List<WeightedConcept> list;


	public WeightedSumConcept(List<WeightedConcept> list)
	{
		this.list = list;
	}


	public List<WeightedConcept> getWeightedConcepts()
	{
		return list;
	}


	@Override
	public String toString()
	{
		String s = "(" + list.get(0).toString();
		for(int i=1; i<list.size(); i++)
			s +=  " + " + list.get(i).toString();
		s += ")";
		return s;
	}

}
