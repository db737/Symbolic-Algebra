package main;

import function.BiVar;
import function.Const;
import function.Function;
import function.T1;
import function.T2;
import function.UniVar;
import function.Var;

/* Used with import static to make creating new Function Objects less
 * time- and space-consuming and to make code more readable. For
 * example this allows us to write return add(f1, f2); rather than
 * return new BiVar(T1.ADD, f1, f2);. It doesn't support the
 * secondary constructors since these are only useful within opt(). 
 */
public class Functions {
	
	public static UniVar neg(Function f) {
		
		return new UniVar(T1.NEG, f);
		
	}
	
	public static UniVar sq(Function f) {
		
		return new UniVar(T1.SQ, f);
		
	}
	
	public static UniVar cu(Function f) {
		
		return new UniVar(T1.CU, f);
		
	}

	public static UniVar inv(Function f) {
	
		return new UniVar(T1.INV, f);
	
	}

	public static UniVar sqrt(Function f) {
	
		return new UniVar(T1.SQRT, f);
	
	}

	public static UniVar invsqrt(Function f) {
	
		return new UniVar(T1.INVSQRT, f);
	
	}

	public static UniVar exp(Function f) {
	
		return new UniVar(T1.EXP, f);
	
	}

	public static UniVar ln(Function f) {
	
		return new UniVar(T1.LN, f);
	
	}

	public static UniVar sin(Function f) {
	
		return new UniVar(T1.SIN, f);
	
	}

	public static UniVar cos(Function f) {
	
		return new UniVar(T1.COS, f);
	
	}

	public static UniVar tan(Function f) {
	
		return new UniVar(T1.TAN, f);
	
	}

	public static UniVar asin(Function f) {
	
		return new UniVar(T1.ASIN, f);
	
	}

	public static UniVar acos(Function f) {
	
		return new UniVar(T1.ACOS, f);
	
	}

	public static UniVar atan(Function f) {
	
		return new UniVar(T1.ATAN, f);
	
	}

	public static UniVar cosec(Function f) {
	
		return new UniVar(T1.COSEC, f);
	
	}

	public static UniVar sec(Function f) {
	
		return new UniVar(T1.SEC, f);
	
	}

	public static UniVar cot(Function f) {
	
		return new UniVar(T1.COT, f);
	
	}

	public static UniVar sinh(Function f) {
	
		return new UniVar(T1.SINH, f);
	
	}

	public static UniVar cosh(Function f) {
	
		return new UniVar(T1.COSH, f);
	
	}

	public static UniVar tanh(Function f) {
	
		return new UniVar(T1.TANH, f);
	
	}

	public static UniVar cosech(Function f) {
	
		return new UniVar(T1.COSECH, f);
	
	}

	public static UniVar sech(Function f) {
	
		return new UniVar(T1.SECH, f);
	
	}

	public static UniVar coth(Function f) {
		
		return new UniVar(T1.COTH, f);
	
	}
	
	public static BiVar add(Function f, Function g) {
		
		return new BiVar(T2.ADD, f, g);
		
	}
	
	public static BiVar sub(Function f, Function g) {
		
		return new BiVar(T2.SUB, f, g);
		
	}

	public static BiVar mult(Function f, Function g) {
	
		return new BiVar(T2.MULT, f, g);
	
	}

	public static BiVar div(Function f, Function g) {
	
		return new BiVar(T2.DIV, f, g);
	
	}
	
	public static BiVar pow(Function f, Function g) {
		
		return new BiVar(T2.POW, f, g);
		
	}
	
	public static Const c(double val) {
		
		return new Const(val);
		
	}
	
	public static Var var(String name) {
		
		return new Var(name);
		
	}
	
}
