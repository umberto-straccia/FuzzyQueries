package core;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;
import FuzzyFunctions.FuzzyConcreteConcept;

public class YagerType1 extends Type1Query {

    private FuzzyConcreteConcept membershipFunction;

    public YagerType1(FuzzyDataProvider fdp, Quantifier q, IRI x, IRI L, IRI d) {
        super(fdp,q,x, L,d);
        super.method = "Yager(Type I)";
    }

    public double run(boolean verbose) throws Exception {
        membershipFunction = fdp.getFuzzyFunction(L);
        checkErrors();
        ArrayList<Double> values = fdp.getPropertyValues(x, d);
        ArrayList<Double> xi = new ArrayList<Double> ();
        for (Double value : values) {
            if (value != null) {
            	xi.add(membershipFunction.getMembershipDegree(value));
            }  
        }
        Collections.sort(xi, Collections.reverseOrder());
        int n = xi.size();
        ArrayList<Double> wi = new ArrayList<Double> ();
        double result = 0;
        for (int i=0; i<n; i++) {
        	wi.add(q.getFunc().getMembershipDegree(1.0 * (i+1)/n) - q.getFunc().getMembershipDegree(1.0 * i/n));
        	System.out.println("wi " + wi.get(i) + ", xi " + xi.get(i) );
        	result += wi.get(i) * xi.get(i);
        }
        return result;
    }

    private void checkErrors() throws Exception {
        if (q.getqType() == QuantifierType.ABSOLUTE) {
            throw new IllegalArgumentException("Quantifier must be relative");
        }
        if (membershipFunction == null) {
            throw new NoSuchElementException("There is no valid function in " + L);
        }
    }
}

