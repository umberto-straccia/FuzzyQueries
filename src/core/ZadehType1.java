package core;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;
import FuzzyFunctions.FuzzyConcreteConcept;

public class ZadehType1 extends Type1Query {

    private FuzzyConcreteConcept membershipFunction;

    public ZadehType1(FuzzyDataProvider fdp, Quantifier q, IRI x, IRI L, IRI d) {
        super(fdp,q,x, L,d);
        super.method = "Zadeh(Type I)";
    }

    public double run(boolean verbose) throws Exception {

        membershipFunction = fdp.getFuzzyFunction(L);
        
        checkErrors();

        ArrayList<Double> values = fdp.getPropertyValues(x, d);
        double sigmaCount = 0;
        for (Double value : values) {
            if (value != null) {
                sigmaCount += membershipFunction.getMembershipDegree(value);
            }
        }
        return q.getFunc().getMembershipDegree(sigmaCount);
    }

    private void checkErrors() throws Exception {
        if (q.getqType() == QuantifierType.RELATIVE) {
            throw new IllegalArgumentException("Quantifier must be absolute");
        }

        if (membershipFunction == null) {
            throw new NoSuchElementException("There is no valid function in " + L);
        }
    }
}

