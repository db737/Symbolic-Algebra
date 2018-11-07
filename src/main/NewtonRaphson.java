package main;

import function.Function;
import static main.Functions.*;

/* The Newton-Raphson iteration for root finding as an example of the
 * applications of symbolic differentiation.
 */
public class NewtonRaphson {

	private final Function f;
	private double xn, xp;
	private final String[] vars;
	
	public NewtonRaphson(Function fi, double x0, String name) {
		
		// x_(n+1) = x_n - f(x_n)/f'(x_n)
		Function temp = sub(var(name), div(fi, fi.diff(name)));
		while(temp.hasChanged()) temp = temp.opt();
		f = temp;
		xn = x0;
		vars = new String[] {name};
		
	}
	
	/* Advance the iteration forwards by one step. 
	 */
	public void step() {
		
		double[] args = new double[] {xn};
		xp = xn;
		xn = f.eval(args, vars);
		
	}
	
	/* Return whether the stored current and previous values are
	 * different, representing whether the last iteration caused the
	 * value to change.
	 */
	public boolean hasChanged() {
		
		return xn != xp;
		
	}
	
	/* Find a root of the equation f(x) = 0 where f(x) is the
	 * function passed to the constructor.
	 */
	public double findRoot() {
		
		/* Iterate until the variable stops changing, or halt if it
		 * becomes NaN.
		 */
		while(hasChanged() && !Double.isNaN(xn)) step();
		/* Many iteration formulae will evaluate to 1-0/0 when given
		 * the exact root as an argument so in this instance we want
		 * to return the value just before we hit NaN.
		 */
		return Double.isNaN(xn) ? xp: xn;
		
	}
	
	/* Return the current value.
	 */
	public double getVal() {
		
		return xn;
		
	}
	
	/* Return the previous value.
	 */
	public double getPrev() {
		
		return xp;
		
	}
	
	/* The same as findRoot() except now if printAll is true it will
	 * print the value of xn at each step.
	 */
	public double findRoot(boolean printAll) {
		
		while(hasChanged() && !Double.isNaN(xn)) {
		
			if(printAll) System.out.println(getVal());
			step();
		
		}
		
		System.out.println(Double.isNaN(xn) ? xp: xn);
		return Double.isNaN(xn) ? xp: xn;
		
	}
	
}
