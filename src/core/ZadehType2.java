package core;
import FuzzyFunctions.FuzzyConcreteConcept;
import util.Pair;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ZadehType2 extends Type2Query {

    private FuzzyConcreteConcept membershipFuncF, membershipFuncG;

    public ZadehType2(FuzzyDataProvider fdp, Quantifier q, IRI x, IRI L1, IRI d1, IRI L2, IRI d2) {
        super(fdp,q,x, L1, d1, L2, d2);
        super.method = "Zadeh(Type II)";
    }

    public double run(boolean v) throws NoSuchElementException {

        membershipFuncG = fdp.getFuzzyFunction(L1);
        membershipFuncF = fdp.getFuzzyFunction(L2);

        checkErrors();

        ArrayList<Pair<Double, Double>> values = fdp.getPropertyValuesByPairs(x, d1, d2);

        double sigmaCountGF = 0;
        double sigmaCountF = 0;
        // Q( P(G&F) / P(F) )
        for (Pair<Double, Double> pair : values) {
            double membershipDegreeG, membershipDegreeF;
            Double valueD1 = pair.getD1();
            Double valueD2 = pair.getD2();
            try {
                membershipDegreeG = valueD1 == null ? 0 : membershipFuncG.getMembershipDegree(valueD1);
                membershipDegreeF = valueD2 == null ? 0 : membershipFuncF.getMembershipDegree(valueD2);

                sigmaCountGF += t_norm(membershipDegreeG, membershipDegreeF);
                sigmaCountF += membershipDegreeF;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        double z = sigmaCountGF / sigmaCountF;
        return q.getFunc().getMembershipDegree(z);
    }

    private double t_norm(double x, double y) {
        return Math.min(x,y);
    }

    void checkErrors() {
        if (q.getqType() == QuantifierType.ABSOLUTE) {
            throw new NoSuchElementException("Quantifier must be relative");
        }
        if (membershipFuncG==null) {
            throw new NoSuchElementException("There is no valid function in " + L1);
        }
        if (membershipFuncF==null) {
            throw new NoSuchElementException("There is no valid function in " + L2);
        }
    }
}

