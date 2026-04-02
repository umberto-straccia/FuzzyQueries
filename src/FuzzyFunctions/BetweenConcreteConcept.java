
/**
 * Fuzzy concrete concept defined with a left shoulder function.
 * @author Fernando Bobillo
 */
package FuzzyFunctions;

public class BetweenConcreteConcept extends FuzzyConcreteConcept
{
    /**
     * Parameters of the funcion
     */
    double a, b;


    public BetweenConcreteConcept(double k1, double k2, double a, double b) throws IllegalArgumentException
    {
        super(k1,k2);
        if (a > b)
            throw new IllegalArgumentException("Between functions require " + a + " <= " + b);
        if (k1 > a)
            throw new IllegalArgumentException("Between functions require " + k1 + " <= " + a);
        if (k2 < b)
            throw new IllegalArgumentException("Between functions require " + k2 + " >= " + b);

        this.a = a;
        this.b = b;
    }

    public BetweenConcreteConcept(double a, double b) throws IllegalArgumentException
    {
        super(a,b); // bad
        if (a > b)
            throw new IllegalArgumentException("Between functions require " + a + " <= " + b);
        if (k1 > a)
            throw new IllegalArgumentException("Between functions require " + k1 + " <= " + a);
        if (k2 < b)
            throw new IllegalArgumentException("Between functions require " + k2 + " >= " + b);

        this.a = a;
        this.b = b;

    }


    @Override
    public double getMembershipDegree(double x)
    {
        if (x <= (a-2)) {
            return 0;
        } else if (x <= a) {
            return 0.5 * x - 0.5 * a + 1;
        } else if (x <= b) {
            return 1;
        } else if (x <= (b+2)) {
            return -0.5*x + 0.5*b + 1;
        } else {
            return 0;
        }
    }

}
