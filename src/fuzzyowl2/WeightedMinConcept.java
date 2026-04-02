package fuzzyowl2;

import java.util.*;

/**
 * Weighted sum concept.
 *
 * @author Fernando Bobillo
 */
public class WeightedMinConcept extends FuzzyConcept
{

	private List<WeightedConcept> list;


	public WeightedMinConcept(List<WeightedConcept> list)
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
		String s = "(WeightedMinimum " + list.get(0).toString();
		for(int i=1; i<list.size(); i++)
			s +=  " , " + list.get(i).toString();
		s += ")";
		return s;
	}

}
