package main;

import function.BiVar;
import function.Function;
import function.T1;
import function.T2;
import function.UniVar;

import static main.Functions.*;

public class InputParser {
	
	/* Reads the given String and interprets it as a Function. It is
	 * required to be in almost the same format as would be used to
	 * get an Object of it from the Functions class ie "add(x, y)"
	 * not "x + y". Consts and Vars must be inputted without the
	 * usual boilerplate eg "x" not "var(x)" and "1.3" not "c(1.3)".
	 * This function works recursively as with most operations of
	 * Functions.
	 */
	public static Function parseFun(String in) throws Exception {
		
		/* Quick check to save unnecessary work for most common kind
		 * of malformed input.
		 */
		if(Util.countOcc(in, '(') != Util.countOcc(in, ')')) throw new Exception("Mismatched brackets.");
		String fun, sub = "";
		String trimmed = in.trim();
		
		if(isVar(trimmed)) {
				
			return var(trimmed);
				
		}
		
		if(!containsNonNum(trimmed)) {
			
			return c(Double.parseDouble(trimmed));
			
		}
		
		/* At this point trimmed must either be a Function or be
		 * invalid input, so we remove its brackets to access its
		 * arguments. If the String doesn't contain enough brackets
		 * it must be malformed.
		 */
		if(!trimmed.contains("(") || !trimmed.contains(")")) throw new Exception("Malformed input.");
		
		/* The name of the Function, in upper case in accordance with
		 * the names in the Enums T1 and T2 being in upper case.
		 */
		fun = trimmed.substring(0, trimmed.indexOf('(')).toUpperCase();
		
		/* The argument(s) of the function, containing a ','
		 * separator if there are 2. 
		 */
		sub = trimmed.substring(trimmed.indexOf('(') + 1, trimmed.lastIndexOf(')') );
		int mc = mainComma(sub);
		
		if(mc == -1) {
			
			/* What we are left with must just be the single argument
			 * of a UniVar Function.
			 */
			return new UniVar(T1.valueOf(fun), parseFun(sub));
			
		}
		
		String arg1 = sub.substring(0, mc);
		String arg2 = sub.substring(mc + 1).trim();
		return new BiVar(T2.valueOf(fun), parseFun(arg1), parseFun(arg2));
		
	}
	
	/* Returns whether the String should be interpreted as a named
	 * variable.
	 */
	private static boolean isVar(String in) {
		
		if(!Character.isLetter(in.charAt(0))) return false;
		return !(in.contains("(") || in.contains(")") || in.contains(",")); 
		
	}
	
	/* Returns the position of the main ',' in an expression that has
	 * just had its outmost layer of brackets removed, or -1 if there
	 * is none. It does this by looking for a ',' after which the
	 * number of '('s and ')'s are equal (since the outer brackets
	 * are removed, every other ',' will be followed by one extra
	 * ')'.
	 */
	private static int mainComma(String in) {
		
		if(!in.contains(",")) return -1;
		
		for(int i = 0; i < in.length(); i++) {
			
			if(in.charAt(i) == ',') {
				
				String ss = in.substring(i);
				if(Util.countOcc(ss, '(') == Util.countOcc(ss, ')')) return i;
			
			}
			
		}
		
		/* If we reach this point we have no main comma so we have a
		 * UniVar Function.
		 */
		return -1;
		
	}
	
	/* Returns whether the given String contains any characters that
	 * aren't valid as part of a number (note that all numbers must
	 * be written in decimal ie "0x1f" is not valid.
	 */
	private static boolean containsNonNum(String in) {
		
		for(int i = 0; i < in.length(); i++) if(!Character.isDigit(in.charAt(i)) && in.charAt(i) != '.') return true;
		return false;
		
	}
	
}
