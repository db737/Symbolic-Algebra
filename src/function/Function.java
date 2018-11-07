package function;

/* Representation of a function made up of compositions of functions.
 * It is treated as a tree and operations on it propagate recursively
 * through.
 */
public interface Function {
	
	/* Returns whether this node or any below it have changed since
	 * the last operation on the tree. Note that 2 consecutive calls
	 * to hasChanged() will return the same result if nothing else
	 * happened in-between. Since the Object itself never changes in
	 * any way, it is set to having been changed when it is created,
	 * since if this part of the tree actually does change, a new
	 * Object will be created. But if opt() is called and it just
	 * passes the call down to the children of this node without
	 * changing it, opt() will use a second constructor to give the
	 * new Object a value of false.
	 */
	public boolean hasChanged();
	
	/* Evaluate the tree of composed functions with the given values
	 * where the variable with name vars[i] has value args[i]. It
	 * works recursively until it reaches a Var or a Const, and if it
	 * finds the former it assigns the correct value in vars to it.
	 */
	public double eval(double[] args, String[] vars);
	
	/* Differentiate the tree of composed functions recursively using
	 * the standard rules eg d/dx(f(x)g(x)) = f'(x)g(x) + f(x)g'(x).
	 * It finds the partial derivative with respect to the variable
	 * called name.
	 */
	public Function diff(String name);
	
	/* A cleanup operation that evaluates UniVar functions of purely
	 * constants to reduce the depth of the tree. For example exp(x)
	 * where x is a Const with value 2.0 is replaced by a Const with
	 * value 7.389... It also optimises BiVars where one argument is
	 * a Const with value 0.0, an important step after calling
	 * diff(), along with MULT with one argument equal to 0.0 or 1.0.
	 * Compositions of functions that are redundant such as
	 * exp(ln(f(x))) are also fixed, in this case giving f(x).
	 */
	public Function opt();
	
}