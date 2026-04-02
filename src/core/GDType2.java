package core;
import FuzzyFunctions.FuzzyConcreteConcept;
import util.Pair;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;

import java.util.*;

public class GDType2 extends Type2Query {

    private FuzzyConcreteConcept membershipFuncF, membershipFuncG;

    public GDType2(FuzzyDataProvider fuzzyDataProvider, Quantifier q, IRI x, IRI L1, IRI d1, IRI L2, IRI d2) {
        super(fuzzyDataProvider,q,x, L1, d1, L2, d2);
        super.method = "GD(Type II)";
    }

    public double run(boolean v) throws NoSuchElementException {
        
        membershipFuncG = fdp.getFuzzyFunction(L1);
        membershipFuncF = fdp.getFuzzyFunction(L2);

        //checkErrors();
        
        ArrayList<Double> membershipValuesF = new ArrayList<>();        // alpha values in support(F)
        ArrayList<Double> membershipsValuesGandF = new ArrayList<>();   // alpha values in support(G&F)
        
        double maxAlpha = fillSupportSets(membershipValuesF, membershipsValuesGandF);

        if (maxAlpha != 1) {
            normalize(membershipValuesF, maxAlpha);
            normalize(membershipsValuesGandF, maxAlpha);
        }

        // M(F) = unique alpha values in F
        Set<Double> m_F = new HashSet<>(membershipValuesF);           // M(F)
        Set<Double> m_GandF = new HashSet<>(membershipsValuesGandF);  // M(G&F)
        Set<Double> m_GbyF = new HashSet<>(m_F);
        m_GbyF.addAll(m_GandF);                                       // M(G/F) = M(F) U M(G&F)

        ArrayList<Double> alphaValues = new ArrayList<>(m_GbyF);
        int n = alphaValues.size();
        alphaValues.add(0.0);

        alphaValues.sort(Collections.reverseOrder());

        double sum = 0;
        for(int i=0; i<n; i++) {
            double cardER = alphaValues.get(i) - alphaValues.get(i+1);
            double cr_GbyF = (double) alpha_cut(membershipsValuesGandF, alphaValues.get(i))
                    / alpha_cut(membershipValuesF, alphaValues.get(i));
            sum = t_conorm(sum,cardER * q.getFunc().getMembershipDegree(cr_GbyF));
        }

        return sum;
    }

    // Lukasiewicz t-conorm
    private double t_conorm(double x, double y) {
        return Double.min(x+y,1);
    }

    // Count how many values are >= alpha
    private int alpha_cut(List<Double> alpha_values, double alpha) {
        int cont = 0;
        for (double value : alpha_values) {
            if (value >= alpha) {
                cont++;
            }
        }
        return cont;
    }

    private void normalize(ArrayList<Double> alpha_values, double maxAlpha) {
        alpha_values.replaceAll(aDouble -> aDouble / maxAlpha);
    }

    // Retrieves all membership values >0 for F and G&F. Returns max_alpha to apply normalization if max_alpha != 1
    private double fillSupportSets(ArrayList<Double> membershipValuesF, ArrayList<Double> membershipValuesGandF ) {

        ArrayList<Pair<Double, Double>> values = fdp.getPropertyValuesByPairs(x, d1, d2);
        
        double maxAlpha = 0;
        for (Pair<Double, Double> pair : values) {

            double membershipDegreeG, membershipDegreeF;
            Double valueG = pair.getD1();
            Double valueF = pair.getD2();
            try {

                membershipDegreeF = valueF == null ? 0 : membershipFuncF.getMembershipDegree(valueF);
                membershipDegreeG = valueG == null ? 0 : membershipFuncG.getMembershipDegree(valueG);

                // Don't add 0 values so both list will be the support of F and FandG
                if (membershipDegreeF > 0) {
                    maxAlpha = Math.max(maxAlpha, membershipDegreeF);
                    membershipValuesF.add(membershipDegreeF);
                    
                    double min = Math.min(membershipDegreeG, membershipDegreeF);
                    if (min > 0) {
                        membershipValuesGandF.add(min);
                    }
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return maxAlpha;
    }

    private void checkErrors() throws IllegalArgumentException, NoSuchElementException {
        if (q.getqType() == QuantifierType.ABSOLUTE) {
            throw new IllegalArgumentException("Quantifier must be relative");
        }
        
        if (membershipFuncG==null) {
            throw new NoSuchElementException("There is no fuzzyFunction for " + L1);
        }

        if (membershipFuncF==null) {
            throw new NoSuchElementException("There is no fuzzyFunction for " + L2);
        }
    }
}

