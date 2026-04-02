package fuzzyowl2;

/**
 * Trapezoidal function.
 *
 * @author Fernando Bobillo
 */
public class TrapezoidalFunction extends FuzzyDatatype
{

	private double a, b, c, d;

	public TrapezoidalFunction(double a, double b, double c, double d)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}


	public double getA()
	{
		return a;
	}


	public double getB()
	{
		return b;
	}


	public double getC()
	{
		return c;
	}


	public double getD()
	{
		return d;
	}


	@Override
	public String toString()
	{
		return "trapezoidal(" + a + ", " + b + ", " + c + ", " + d + ")";
	}

}
