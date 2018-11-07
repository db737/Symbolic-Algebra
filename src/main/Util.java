package main;

import function.Function;

public class Util {
	
	/* Differentiate a function, then optimise as far as possible. 
	 */
	public static Function optDiff(Function f, String name) {
		
		Function out = f.diff(name);
		
		while(out.hasChanged()) {
			
			out = out.opt();
			
		}
		
		return out;
		
	}
	
	/* Simple function for counting the occurrences of a char within
	 * a String.
	 */
	public static int countOcc(String in, char toCount) {
		
		int count = 0;
		for(int i = 0; i < in.length(); i++) if(in.charAt(i) == toCount) count++;
		return count;
		
	}
	
}
