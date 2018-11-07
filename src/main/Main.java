package main;

import function.Function;
import static main.Functions.*;

import java.util.Scanner;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] s) throws Exception {
		
		System.out.println("Root finder");
		Scanner sc = new Scanner(System.in);
		System.out.println("Function to find root of (use x as the only variable):");
		String in = sc.nextLine();
		Function f = InputParser.parseFun(in);
		System.out.println("Interpreted as: " + f.toString());
		System.out.println("Starting value:");
		double x0 = sc.nextDouble();
		sc.close();
		NewtonRaphson nr = new NewtonRaphson(f, x0, "x");
		System.out.println("Root of f(x) = 0 is: " + nr.findRoot());

	}

}
