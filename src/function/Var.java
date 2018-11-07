package function;

import static main.Functions.c;

/* Representation of a variable. It doesn't need to be given a value
 * until the whole function is evaluated. It keeps a string as a name
 * so that functions can have multiple variables and be evaluated or
 * differentiated as normal, with the value or derivation filtering
 * down to the Vars on the leaves of the tree.
 */
public class Var implements Function {

	public final String label;
	
	public Var(String name) {
		
		label = name;
		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see function.Function#hasChanged()
	 * Vars cannot be optimised in any way so they are never changed.
	 */
	@Override
	public boolean hasChanged() {
		
		return false;
		
	}

	@Override
	public double eval(double[] args, String[] vars) {
		
		int pos = 0;
		boolean found = false;
		
		for(int i = 0; i < vars.length; i++) {
			
			if(label.equals(vars[i])) {
				
				pos = i;
				found = true;
				break;
				
			}
			
		}
		
		if(found) {
			
			return args[pos];
			
		}
		
		throw new RuntimeException("Variable \"" + label + "\" has no value assigned!");
		
	}

	/* 
	 * (non-Javadoc)
	 * @see function.Function#diff(java.lang.String)
	 * Since we want the partial derivative, this is just 0 if we are
	 * differentiating with respect to a different variable or 1 if
	 * we are differentiating with respect to this one.
	 */
	@Override
	public Function diff(String name) {
		
		if(label.equals(name)) return c(1.0);	
		return c(0.0);
		
	}

	/* 
	 * (non-Javadoc)
	 * @see function.Function#opt()
	 * No optimisation can be done on an unknown variable.
	 */
	@Override
	public Function opt() {
		
		return this;
		
	}
	
	@Override
	public String toString() {
		
		return label;
		
	}

}