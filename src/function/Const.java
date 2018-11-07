package function;

import static main.Functions.c;

/* Representation of a constant. It takes a floating-point value on
 * creation and has no special properties.
 */
public class Const implements Function {

	private final boolean changed;
	public final double val;
	
	public Const(double v) {
		
		changed = true;
		val = v;
		
	}
	
	public Const(double v, boolean b) {
		
		changed = false;
		val = v;
		
	}
	
	@Override
	public boolean hasChanged() {
		
		return changed;
		
	}

	@Override
	public double eval(double[] args, String[] vars) {
		
		return val;
		
	}

	@Override
	public Function diff(String name) {
		
		return c(0.0);
		
	}

	/*
	 * (non-Javadoc)
	 * @see function.Function#opt()
	 * This operation is never called when the branches above
	 * actually can be optimised, and only serves as an end to the
	 * recursion for functions that cannot be optimised such as
	 * 1 + x. When the branches above are optimisable, the value of
	 * this constant is used by opt() when called on the parent of
	 * this leaf.
	 */
	@Override
	public Function opt() {
		
		return new Const(val, false);
		
	}
	
	@Override
	public String toString() {
		
		return Double.toString(val);
		
	}

}