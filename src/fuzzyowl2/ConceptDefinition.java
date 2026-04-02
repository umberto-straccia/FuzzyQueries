package fuzzyowl2;

import java.util.*;

/**
 * Fuzzy concept defined by means of an OWL 2 annotation of an OWL 2 class.
 *
 * @author Fernando Bobillo
 */
public class ConceptDefinition
{

	/** Types of fuzzy concepts that can be defined using annotations.
	 */
	public enum ConceptType {
		CHOQUET,
		FUZZY_NOMINAL, 
		MODIFIED_CONCEPT, 
		OWA, 
		QUANTIFIER_OWA,
		QUASI_SUGENO,
		SUGENO,
		WEIGHTED_CONCEPT, 
		WEIGHTED_MAX, 
		WEIGHTED_MIN, 
		WEIGHTED_SUM 
	};

	private ConceptType type;
	private String mod;
	private String c;
	private String i;
	private String q;
	private double n;
	private List<String> concepts;
	private List<Double> weights;
	private List<ConceptDefinition> weightedConcepts;


	public ConceptType getType ()
	{
		return type;
	}


	public void modifiedConcept(String mod, String c)
	{
		this.mod = mod;
		this.c = c;
		type = ConceptType.MODIFIED_CONCEPT;
	}


	public void weightedConcept(double n, String c)
	{
		this.n = n;
		this.c = c;
		type = ConceptType.WEIGHTED_CONCEPT;
	}


	public void fuzzyNominalConcept(double n, String i)
	{
		this.n = n;
		this.i = i;
		type = ConceptType.FUZZY_NOMINAL;
	}


	public void weightedMaxConcept(List<ConceptDefinition> list)
	{
		this.weightedConcepts = list;
		type = ConceptType.WEIGHTED_MAX;
	}


	public void weightedMinConcept(List<ConceptDefinition> list)
	{
		this.weightedConcepts = list;
		type = ConceptType.WEIGHTED_MIN;
	}


	public void weightedSumConcept(List<ConceptDefinition> list)
	{
		this.weightedConcepts = list;
		type = ConceptType.WEIGHTED_SUM;
	}


	public void owaConcept(List<Double> weights, List<String> concepts)
	{
		this.weights = weights;
		this.concepts = concepts;
		type = ConceptType.OWA;
	}


	public void choquetConcept(List<Double> weights, List<String> concepts)
	{
		this.weights = weights;
		this.concepts = concepts;
		type = ConceptType.CHOQUET;
	}


	public void sugenoConcept(List<Double> weights, List<String> concepts)
	{
		this.weights = weights;
		this.concepts = concepts;
		type = ConceptType.SUGENO;
	}


	public void quasiSugenoConcept(List<Double> weights, List<String> concepts)
	{
		this.weights = weights;
		this.concepts = concepts;
		type = ConceptType.QUASI_SUGENO;
	}


	public void quantifierOwaConcept(String q, List<String> concepts)
	{
		this.q = q;
		this.concepts = concepts;
		type = ConceptType.QUANTIFIER_OWA;
	}


	public List<Double> getWeights()
	{
		return weights;
	}


	public List<String> getConcepts()
	{
		return concepts;
	}


	public List<ConceptDefinition> getWeightedConcepts()
	{
		return weightedConcepts;
	}


	public String getFuzzyModifier()
	{
		return mod;
	}


	public String getFuzzyConcept()
	{
		return c;
	}


	public String getIndividual()
	{
		return i;
	}


	public String getQuantifier()
	{
		return q;
	}


	public double getNumber()
	{
		return n;
	}

}
