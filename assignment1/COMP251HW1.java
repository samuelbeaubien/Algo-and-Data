package assignment1;

import java.io.*;import java.io.*;
import java.util.*;

/**
 *
 * ATTENTION: ANY CHANGES IN INITIAL CODE (INCLUDING FILE NAME, METHODS, CONSTRUCTORS etc) WILL CAUSE NOT POSITIVE MARK!
 * HOWEVER YOU CAN CREATE YOUR OWN METHODS WITH CORRECT NAME. ONLY THIS FILE WILL BE GRADED, that is NO EXTERNAL CLASSES are allowed.
 *
 * TO STUDENT: ALL IMPORTANT PARTS ARE SELECTED "TO STUDENT" AND WRITTEN IN HEADERS OF METHODS. *
 * @author AlexanderButyaev
 *
 */
public class COMP251HW1 {
	// This is the list of ALPHA s (from 0.2 till 4 with step 0.2)
	public double[] ns = {0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 2.8, 3.0, 3.2, 3.4, 3.6, 3.8, 4.0};
	/*Fields / methods for grading BEGIN*/
	private HashMap<Integer,String> pathMap;

	public HashMap<Integer, String> getPaths() {
		return pathMap;
	}

	public void setPaths(HashMap<Integer, String> pathMap) {
		this.pathMap = pathMap;
	}
	/*Fields / methods for grading END*/

	/**
	 * method generateRandomNumbers generates array of random numbers (double primitive) of size = "size" and w, which limits generated random number by 2^w-1
	 * @param w - integer number, which limits generated random number by 2^w-1
	 * @param size - size of the resulting array
	 * @return double[]
	 */
	public double[] generateRandomNumbers(int w, int size) {
		double[] resultArray = new double[size];
		if (getPaths() != null) {	//THIS PART WILL BE USED FOR GRADING
			String path = getPaths().get(size);
			File file = new File (path);
			Scanner scanner;
			try {
				scanner = new Scanner(file);
				int i = 0;
				while (scanner.hasNextLine() && i < resultArray.length) {
					resultArray[i] = Double.parseDouble(scanner.nextLine());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {

			for (int i = 0; i < size; i++) {
				resultArray[i] = (int)(Math.random()*(Math.pow(2, w)-1)); //cast to int to make it Integer
			}
		}
		return resultArray;
	}

	public double generateRandomNumberInRange(double min, double max) {
		double res = min;
		while (res == min) {
			res = min + Math.random() * (max - min);
		}
		return res;
	}

	/**
	 * method generateCSVOutputFile generates CSV File which contains row of x (first element is identificator "X"),
	 * and one row for every experiment (ys - id with set of values)
	 * Looks like this:
	 * ================
	 *  X,1,2,3
	 *  E1,15,66,34
	 *  E2,16,15,14
	 *  E3,99,88,77
	 * ================
	 *
	 * @param filePathName - absolute path to the file with name (it will be rewritten or created)
	 * @param x - values along X axis, eg 1,2,3,4,5,6,7,8
	 * @param ys - values for Y axis with the name of the experiment for different plots.
	 */
	public void generateCSVOutputFile(String filePathName, double[] x, HashMap<String, double[]> ys) {
		File file = new File(filePathName);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.append("X");
			for (double d: x) {
				fw.append("," + d);
			}
			fw.append("\n");
			for (Map.Entry<String, double[]> entry: ys.entrySet()) {
				fw.append(entry.getKey());
				double[] dTemp = entry.getValue();
				for (int i = 0, len = dTemp.length;i < len; i++) {
					fw.append(","+dTemp[i]);
				}
				fw.append("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * divisionMethod is the main method for division method in hashing problem. It creates specific array ys, iterates over the set of n (defined by ns field) and for every n adds in ys array particular number of collisions
	 *
	 * It requires arguments:
	 * r - division factor, w - integer number (required for random number generator)
	 *
	 * It returns an array of number of collisions (for all n in range from 10% to 200% of m) - later in plotting phase - it is Y values for divisionMethod
	 * @param r
	 * @param w
	 * @return ys for division method {double[]}
	 * 
	 * Division method : f(k) = k % D
	 * Division method : f(k) = k % 2^r
	 */
	public double[] divisionMethod (int r, int w) {
		double[] ys = new double[ns.length];
		for (int it = 0, len = ns.length; it < len; it++) {
			int m = (int) (Math.pow(2,r));	// m = 2^r (number of slots in the array of the hash table 
			int n = (int)(ns[it]*m);	// number of keys to insert
			ys[it] = divisionMethodImpl (r, n, w);
		}
		return ys;
	}

	/**
	 * divisionMethodImpl is the particular implementation of the division method.
	 *
	 * It requires arguments:
	 * r - division factor, n - number of key to insert, w - integer number (required for random number generator)
	 * 
	 * @param r
	 * @param n
	 * @param w
	 * @return number of collision for particular configuration {double}
	 */
	public double divisionMethodImpl (int r, int n, int w) {

		/*TO STUDENT: WRITE YOUR CODE HERE*/
		
		int sumCollisions = 0;
		double maxKey = Math.pow(2,w) -1;	// maximum value of a given key
		double D = Math.pow(2, r);	// Size of the hash table
		boolean[] hashTable = new boolean[(int) D];	// Hash table 
		
		for (int counter_n = 0; counter_n < n; counter_n++)
		{
			double currentKey = generateRandomNumberInRange(0, maxKey);	// Create a new key
			int indexHashTable = (int) (currentKey % D);	// calculate index in hashTable
			// Check if the position in the HashTable has already been visited (value = true), else change for true 
			if (hashTable[indexHashTable] == true)
			{
				sumCollisions++;
			}
			else
			{
				hashTable[indexHashTable] = true;
			}	
		}
		return sumCollisions;
	}



	/**
	 * multiplicationMethod is the main method for multiplication method in hashing problem. It creates specific array ys, specifies A under with some validations, iterates over the set of n (defined by ns field) and for every n adds in ys array particular number of collisions
	 *
	 * It requires arguments:
	 * r and w - are such integers, that w > r
	 *
	 * It returns an array of number of collisions (for all n in range from 0.2 to 4 of m) - later in plotting phase - it is Y values for multiplicationMethod
	 * @param r
	 * @param w
	 * @return ys for multiplication method {double[]}
	 */
	public double[] multiplicationMethod (int r, int w) {
		double[] ys = new double[ns.length];
		double y;
		double A = generateRandomNumberInRange(Math.pow(2, (w-1)), Math.pow(2, w)); // This line should be updated to assign a valid value to A
		for (int it = 0, len = ns.length; it < len; it++) {
			int m = (int)(Math.pow(2,r));
			int n = (int)(ns[it]*m);
			y = multiplicationMethodImpl (r, n, w, A);
			if (y < 0) return null;
			ys[it] = y;
		}
		return ys;
	}


	/**
	 * multiplicationMethodImpl is the particular implementation of the multiplication method.
	 *
	 * It requires arguments:
	 * n - number of key to insert, r and w - are such integers, that w > r, A is a factor
	 * @param r
	 * @param n
	 * @param w
	 * @param A
	 * @return number of collisions for particular configuration {double}
	 */
	public double multiplicationMethodImpl (int r, int n, int w, double A) {

		/*TO STUDENT: WRITE YOUR CODE HERE*/
		
		int sumCollisions = 0;
		boolean[] hashTable = new boolean[(int) Math.pow(2, r)];
		double maxKey = Math.pow(2,w) -1;	// maximum value of a given key
		for (int counter_n = 0; counter_n < n; counter_n++)
		{
			double currentKey = generateRandomNumberInRange(0, maxKey);	// Create new key
			
			// Calculate indexHashTable			
			double temp = (A * currentKey) % Math.pow(2,w);	//  (A ⋅ k mod 2w ) 
			int indexHashTable = (int) (temp / Math.pow(2, (w - r))); // Same as temp >> (w - r)
			if (hashTable[indexHashTable] == true)
			{
				sumCollisions++;
			}
			else 
			{
				hashTable[indexHashTable] = true;
			}
		}
		return sumCollisions;
	}


	/**
	 * TO STUDENT: MAIN method - WRITE/CHANGE code here (it should be compiled anyway!)
	 * TO STUDENT: NUMBERS ARE RANDOM!
	 * @param args
	 */
	public static void main(String[] args) {
		int r = 0, w = 0;
		if (args!= null && args.length>1) {
			r = Integer.parseInt(args[0]);
			w = Integer.parseInt(args[1]);
		} else {
			System.err.println("Input should be r w  (integers). Exit(-1).");
			System.exit(-1);
		}

		if (w<=r) {
			System.err.println("Input should contain r w (integers) such that w>r. Exit(-1).");
			System.exit(-1);
		}

		COMP251HW1 hf = new COMP251HW1();
		double[] yTemp;

		HashMap<String, double[]> ys = new HashMap<String, double[]>();
		System.out.println("===Division=Method==========");
		yTemp = hf.divisionMethod(r, w);
		if (yTemp == null) {
			System.out.println("Something wrong with division method. Check your implementation, formula and all its parameters.");
			System.exit(-1);
		}
		ys.put("divisionMethod", yTemp);
		for (double y: ys.get("divisionMethod")) {
			System.out.println(y);
		}

		System.out.println("============================");
		System.out.println("===Multiplication=Method====");
		yTemp = hf.multiplicationMethod(r, w);
		if (yTemp == null) {
			System.out.println("Something wrong with division method. Check your implementation, formula and all its parameters.");
			System.exit(-1);
		}
		ys.put("multiplicationMethod", yTemp);

		for (double y: ys.get("multiplicationMethod")) {
			System.out.println(y);
		}

		double[] x = new double[hf.ns.length];
		int m = (int)(Math.pow(2,r));
		for (int it = 0, len = hf.ns.length; it < len; it++) {
			x[it] = (int)(hf.ns[it]*m);
		}

		hf.generateCSVOutputFile("hashFunctionProblem.csv", x, ys);
	}
}