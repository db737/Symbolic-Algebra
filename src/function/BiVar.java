package function;

import static main.Functions.*;

/* Representation of a function with 2 arguments.
 */
public class BiVar implements Function {
	
	private final boolean changed;
	public final Function f, g;
	public final T2 type;
	
	public BiVar(T2 t, Function f1, Function f2) {
		
		changed = true;
		f = f1;
		g = f2;
		type = t;
		
	}
	
	/* Constructor for telling hasChanged() that this Function is the
	 * same as the one that created it during an optimisation step.
	 */
	public BiVar(T2 t, Function f1i, Function f2i, boolean b) {
		
		changed = b;
		f = f1i;
		g = f2i;
		type = t;
		
	}
	
	@Override
	public boolean hasChanged() {
		
		return changed || f.hasChanged() || g.hasChanged();
		
	}

	@Override
	public double eval(double[] args, String[] vars) {
		
		switch(type) {
		
		case ADD:
			return f.eval(args, vars) + g.eval(args, vars);
		case SUB:
			return f.eval(args, vars) - g.eval(args, vars);
		case MULT:
			return f.eval(args, vars) * g.eval(args, vars);
		case DIV:
			return f.eval(args, vars) / g.eval(args, vars);
		case POW:
			return Math.pow(f.eval(args, vars), g.eval(args, vars));
		default:
			/* This default case should never be reached unless a new
			 * type is added and this isn't updated to support it.
			 */
			throw new RuntimeException("Unknown type!");
		
		}
		
	}

	@Override
	public Function diff(String name) {
		
		switch(type) {
		
		case ADD:
			return add(f.diff(name), g.diff(name));
		case SUB:
			return sub(f.diff(name), g.diff(name));
		case MULT:
			/* Leibniz product rule
			 */
			return add(mult(f.diff(name), g), mult(f, g.diff(name)));
		case DIV:
			/* Quotient rule
			 */
			return div(sub(mult(f.diff(name), g), mult(f, g.diff(name))), sq(g));
		case POW:
			/* d/dx(f(x)^g(x)) = d/dx(exp(g(x)ln(f(x))))
			 * = (f'(x)g(x)/f(x)+g'(x)ln(f(x)))(f(x)^g(x)).
			 */
			return mult(add(div(mult(f.diff(name), g), f), mult(g.diff(name), ln(f))), this);
		default:
			/* This default case should never be reached unless a new
			 * type is added and this isn't updated to support it.
			 */
			throw new RuntimeException("Unknown type!");
			
		}
		
	}

	@Override
	public Function opt() {

		if(f instanceof Const) {
			
			double vf = ((Const) f).val;
			
			if(g instanceof Const) {
				
				double vg = ((Const) g).val;
				if(type == T2.POW && vf == 0.0 && vg == 0.0) throw new ArithmeticException("0^0 is undefined.");
				return c(this.eval(null, null));
				
			}
			
			if(vf == 1.0) {
				
				if(type == T2.MULT) return g;
				if(type == T2.DIV) return inv(g);
				if(type == T2.POW) return c(1.0);
				
			}
			
			else if(vf == 0.0) {
				
				if(type == T2.ADD) return g;
				if(type == T2.SUB) return neg(g);
				if(type == T2.MULT || type == T2.DIV || type == T2.POW) return c(0.0);
				
			}
			
			else if(vf == -1.0 && type == T2.MULT) {
				
				return neg(g.opt());
				
			}
			
			else if(type == T2.POW) {
				
				return exp(mult(c(Math.log(vf)), g));
				
			}
			
		}
		
		else if(g instanceof Const) {
			
			double vg = ((Const) g).val;
			
			if(vg == 0.0) {
				
				if(type == T2.ADD || type == T2.SUB) return f.opt();
				if(type == T2.MULT) return c(0.0);
				if(type == T2.DIV) throw new ArithmeticException("division by 0 is undefined.");
				
			}
			
			else if(vg == 1.0 && (type == T2.MULT || type == T2.DIV || type == T2.POW)) return f.opt();
			
			/* Optimise various integer and half-integer powers of a
			 * Function into their specific UniVar counterparts; eg
			 * f(x)^0.5 becomes sqrt(f(x)).
			 */
			else if(type == T2.POW) {
				
				if(vg == -3.0) return inv(cu(f.opt()));
				if(vg == -2.0) return inv(sq(f.opt()));
				if(vg == -1.5) return invsqrt(cu(f.opt()));
				if(vg == -1.0) return inv(f.opt());
				if(vg == -0.5) return invsqrt(f.opt());
				if(vg == 0.5) return sqrt(f.opt());
				if(vg == 1.5) return sqrt(cu(f.opt()));
				if(vg == 2.0) return sq(f.opt());
				if(vg == 3.0) return cu(f.opt());
				
			}
			
			else if(vg == -1.0 && type == T2.MULT) {
				
				return neg(f.opt());
				
			}	
				
		}
		
		else if(f instanceof UniVar){
			
			UniVar unf = (UniVar) f;
			T1 tf = unf.type;
			if(type == T2.ADD && tf == T1.NEG) return sub(g.opt(), unf.f.opt());
			if(type == T2.MULT && tf == T1.INV) return div(g.opt(), unf.f.opt());
			if(type == T2.DIV && tf == T1.INV) return inv(mult(g.opt(), unf.f.opt()));
			
		}
		
		else if(g instanceof UniVar){
			
			UniVar ung = (UniVar) g;
			T1 tg = ung.type;
			if(type == T2.ADD && tg == T1.NEG) return sub(f.opt(), ung.f.opt());
			if(type == T2.SUB && tg == T1.NEG) return add(f.opt(), ung.f.opt());
			if(type == T2.MULT && tg == T1.INV) return div(f.opt(), ung.f.opt());
			if(type == T2.DIV && tg == T1.INV) return mult(f.opt(), ung.f.opt());
			
		}
		
		return new BiVar(type, f.opt(), g.opt(), false);
		
	}
	
	@Override
	public String toString() {
		
		return type.toString().toLowerCase() + "(" + f.toString() + ", " + g.toString() + ")";
		
	}

}