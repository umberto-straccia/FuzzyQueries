
/**
 * Fuzzy concrete concept defined with a left shoulder function.
 * @author Fernando Bobillo
 */
package FuzzyFunctions;

public class AroundConcreteConcept extends FuzzyConcreteConcept
{
    /**
     * Parameters of the funcion
     */
    double a, b;


    public AroundConcreteConcept(double k1, double k2, double a) throws IllegalArgumentException
    {
        super(k1,k2);
        if (k1 > a)
            throw new IllegalArgumentException("Around functions require " + k1 + " <= " + a);
        if (k2 < a)
            throw new IllegalArgumentException("Around functions require " + k2 + " >= " + a);

        this.a = a;
    }

    public AroundConcreteConcept(double a) throws IllegalArgumentException
    {
        super(a,a); // bad
        if (k1 > a)
            throw new IllegalArgumentException("Around functions require " + k1 + " <= " + a);
        if (k2 < a)
            throw new IllegalArgumentException("Around functions require " + k2 + " >= " + a);

        this.a = a;
    }


    @Override
    public double getMembershipDegree(double x)
    {

        if (x <= (a-2)) {
            return 0;
        } else if (x <= a) {
            return 0.5*x - 0.5*a + 1;
        } else if (x <= (a+2)) {
            return -0.5*x + 0.5*a + 1;
        } else {
            return 0;
        }
    }

}
