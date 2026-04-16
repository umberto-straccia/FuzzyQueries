package core;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;
import FuzzyFunctions.FuzzyConcreteConcept;


public class GDType1 extends Type1Query{

    private FuzzyConcreteConcept membershipFunction;

    public GDType1(FuzzyDataProvider fdp, Quantifier q, IRI x, IRI L, IRI d) {
        super(fdp,q,x, L,d);
        super.method = "GD(Type I)";
    }

    public double run(boolean verbose) throws Exception {
        membershipFunction = fdp.getFuzzyFunction(L);
        checkErrors();
        ArrayList<Double> values = fdp.getPropertyValues(x, d);
        ArrayList<Double> membershipDegrees = new ArrayList<>();

        // Membership degrees = support(G)
        for (Double value : values) {
            if (value != null) {
                double a = membershipFunction.getMembershipDegree(value);
                if (a > 0) {
                    membershipDegrees.add(a);
                }
            }
        }

        int n = membershipDegrees.size();
        membershipDegrees.add(1.0); // b0 = 1
        membershipDegrees.add(0.0); // bn+1 = 0
        
        //System.out.println("membershipDegrees size:" + n);
        //System.out.println("membershipDegrees:" + membershipDegrees);

        Collections.sort(membershipDegrees, Collections.reverseOrder());
        
        System.out.println("After SORT membershipDegrees:" + membershipDegrees);

        // GDQ(A) = sum[0,n] ( ED(A,i) X Q(i) ) if ABSOLUTE quantifier Q
        // GDQ(A) = sum[0,n] ( ED(A,i) X Q(i/n) ) if RELATIVE quantifier Q ??
        FuzzyConcreteConcept fQ = q.getFunc();
        double sum = 0;
        for (int i=0; i <= n; i++) {
            double cardED = membershipDegrees.get(i) - membershipDegrees.get(i+1);
            //double gFval = q.getFunc().getMembershipDegree(i);
/*          
            System.out.println("i val:" + membershipDegrees.get(i));
            System.out.println("i+1 val:" + membershipDegrees.get(i+1));
            System.out.println("CardED:" + cardED);
*/
            double gFval = 0;
            if (q.getqType() == QuantifierType.ABSOLUTE) {
            	gFval = fQ.getMembershipDegree(i);
            	sum = t_conorm(sum, cardED * gFval);
            } else { // Relative quantifier
            	//double deg = n*membershipDegrees.get(i);
            	double qval = i/n;
            	System.out.println("qval:" + qval);
            	gFval = fQ.getMembershipDegree(qval);
            	sum = t_conorm(sum, cardED * gFval);
            }

//            System.out.println("gFval:" + gFval);
//            System.out.println("sum:" + sum);
            
        }
        //System.out.println("SUM:" + sum);
        return sum;
    }

    // Lukasiewicz t-conorm
    private double t_conorm(double x, double y) {
        return Double.min(x+y,1);
    }

    private void checkErrors() throws Exception {
        if (membershipFunction == null) {
            throw new NoSuchElementException("Membership function not found");
        }
    }
}

