package function;

import static main.Functions.*;

/* Representation of a function of a single argument.
 */
public class UniVar implements Function {
	
	private final boolean changed;
	public final Function f;
	public final T1 type;
	
	public UniVar(T1 t, Function fi) {
		
		changed = true;
		f = fi;
		type = t;
		
	}
	
	public UniVar(T1 t, Function fi, boolean b) {
		
		changed = b;
		f = fi;
		type = t;
		
	}

	@Override
	public boolean hasChanged() {
		
		return changed || f.hasChanged();
		
	}

	@Override
	public double eval(double[] args, String[] vars) {
		
		switch(type) {
		
		case NEG:
			return -f.eval(args, vars);
		case SQ:
			return f.eval(args, vars) * f.eval(args, vars);
		case CU:
			return Math.pow(f.eval(args, vars), 3.0);
		case INV:
			return 1.0 / f.eval(args, vars);
		case SQRT:
			return Math.sqrt(f.eval(args, vars));
		case INVSQRT:
			return 1.0 / Math.sqrt(f.eval(args, vars));
		case EXP:
			return Math.exp(f.eval(args, vars));
		case LN:
			return Math.log(f.eval(args, vars));
		case SIN: 
			return Math.sin(f.eval(args, vars));
		case COS:
			return Math.cos(f.eval(args, vars));
		case TAN:
			return Math.tan(f.eval(args, vars));
		case ASIN:
			return Math.asin(f.eval(args, vars));
		case ACOS:
			return Math.acos(f.eval(args, vars));
		case ATAN:
			return Math.atan(f.eval(args, vars));
		case COSEC:
			return 1.0 / Math.sin(f.eval(args, vars));
		case SEC:
			return 1.0 / Math.cos(f.eval(args, vars));
		case COT:
			return 1.0 / Math.tan(f.eval(args, vars));
		case SINH:
			return Math.sinh(f.eval(args, vars));
		case COSH:
			return Math.cosh(f.eval(args, vars));
		case TANH:
			return Math.tanh(f.eval(args, vars));
		case COSECH:
			return 1.0 / Math.sinh(f.eval(args, vars));
		case SECH:
			return 1.0 / Math.cosh(f.eval(args, vars));
		case COTH:
			return 1.0 / Math.tanh(f.eval(args, vars));
		default:
			/* This default case should never be reached unless a new
			 * type is added and this isn't updated to support it.
			 */
			throw new RuntimeException("Unknown type!");
			
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see function.Function#diff(java.lang.String)
	 * We explicitly avoid powers in favour of specialised
	 * functions such as invsqrt() since the derivative of
	 * f(x)^g(x) is complicated even though in many of the cases
	 * where we would use it it would be much simpler eg
	 * d/dx(f(x)^n) = nf'(x)f(x)^(n-1). The variable out is used
	 * to store the derivative of the argument of this Function
	 * since in almost all cases we need it for use by the chain rule
	 * so we compute it separately to make the switch statement more
	 * readable.
	 */
	@Override
	public Function diff(String name) {
		
		Function out = null;
		
		switch(type) {
		
		case NEG:
			return neg(f.diff(name));
		case SQ:
			out = mult(c(2.0), f);
			break;
		case CU:
			out = mult(c(3.0), sq(f));
			break;
		case INV:
			out = mult(c(-1.0), inv(sq(f)));
			break;
		case SQRT:
			out = mult(c(0.5), invsqrt(f));
			break;
		case INVSQRT:
			out = mult(c(-0.5), invsqrt(cu(f)));
			break;
		case EXP:
			out = this;
			break;
		case LN:
			out = inv(f);
			break;
		case SIN: 
			out = cos(f);
			break;
		case COS:
			out = neg(sin(f));
			break;
		case TAN:
			out = sq(sec(f));
			break;
		case ASIN:
			out = invsqrt(sub(c(1.0), sq(f)));
			break;
		case ACOS:
			out = neg(invsqrt(sub(c(1.0), sq(f))));
			break;
		case ATAN:
			out = inv(add(c(1.0), sq(f)));
			break;
		case COSEC:
			out = neg(mult(cosec(f), cot(f)));
			break;
		case SEC:
			out = mult(sec(f), tan(f));
			break;
		case COT:
			out = neg(sq(cosec(f)));
			break;
		case SINH:
			out = cosh(f);
			break;
		case COSH:
			out = sinh(f);
			break;
		case TANH:
			out = sq(sech(f));
			break;
		case COSECH:
			out = neg(mult(cosech(f), coth(f)));
			break;
		case SECH:
			out = neg(mult(sech(f), tanh(f)));
			break;
		case COTH:
			out = neg(sq(cosech(f)));
			break;
			
		}
		
		return mult(out, f.diff(name));
		
	}

	/*
	 * (non-Javadoc)
	 * @see function.Function#opt()
	 * Supports optimising functions of functions that can be
	 * simplified by basic rules of algebra or function definitions,
	 * eg f(f^-1(g(x))) gets optimised to g(x) for all supported
	 * functions where the inverse is also supported. We also flatten
	 * out functions of constants by evaluating the function
	 * immediately to get a new constant. It also supports in
	 * particular moving squaring inside multiplications or divisions
	 * since often this will allow further optimisation.
	 */
	@Override
	public Function opt() {
		
		if(f instanceof Const) {
			
			if(((Const) f).val < 0.0 && (type == T1.SQRT || type == T1.INVSQRT)) throw new ArithmeticException("Imaginary numbers are not supported.");
			if(((Const) f).val == 0.0 && (type == T1.INV || type == T1.INVSQRT)) throw new ArithmeticException("Division by 0 is undefined.");
			return c(this.eval(null, null));
			
		}
		
		else if(f instanceof UniVar) {
			
			UniVar uf = (UniVar) f;
			Function g = uf.f;
			T1 tf = uf.type;
			if(type == T1.NEG && tf == T1.NEG) return g.opt();
			if((type == T1.SQ && tf == T1.SQRT ) || (type == T1.SQRT && tf == T1.SQ)) return g.opt();
			if(type == T1.INV && tf == T1.INV) return g.opt();
			if((type == T1.INV && tf == T1.SQRT) || (type == T1.SQRT && tf == T1.INV)) invsqrt(g.opt());
			if(type == T1.INV && tf == T1.SIN) return cosec(g.opt());
			if(type == T1.INV && tf == T1.COS) return sec(g.opt());
			if(type == T1.INV && tf == T1.TAN) return cot(g.opt());
			if(type == T1.INV && tf == T1.SINH) return cosech(g.opt());
			if(type == T1.INV && tf == T1.COSH) return sech(g.opt());
			if(type == T1.INV && tf == T1.TANH) return coth(g.opt());
			if(type == T1.INV && tf == T1.COSEC) return sin(g.opt());
			if(type == T1.INV && tf == T1.SEC) return cos(g.opt());
			if(type == T1.INV && tf == T1.COT) return tan(g.opt());
			if(type == T1.INV && tf == T1.COSECH) return sinh(g.opt());
			if(type == T1.INV && tf == T1.SECH) return cosh(g.opt());
			if(type == T1.INV && tf == T1.COTH) return tanh(g.opt());
			if((type == T1.EXP && tf == T1.LN) || (type == T1.LN && tf == T1.EXP)) return g.opt();
			if((type == T1.SIN && tf == T1.ASIN) || (type == T1.ASIN && tf == T1.SIN)) return g.opt();
			if((type == T1.COS && tf == T1.ACOS) || (type == T1.ACOS && tf == T1.COS)) return g.opt();
			if((type == T1.TAN && tf == T1.ATAN) || (type == T1.ATAN && tf == T1.TAN)) return g.opt();
			
		}
		
		if(type == T1.SQ && f instanceof BiVar) {
			
			BiVar bf = (BiVar) f;
			
			if(bf.type == T2.MULT) {
				
				return mult(sq(bf.f), sq(bf.g));
				
			}
			
			if(bf.type == T2.DIV) {
				
				return div(sq(bf.f), sq(bf.g));
				
			}
			
		}
		
		return new UniVar(type, f.opt(), false);
		
	}
	
	@Override
	public String toString() {
		
		return type.toString().toLowerCase() + "(" + f.toString() + ")";
		
	}
	
}