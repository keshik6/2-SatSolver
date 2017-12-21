/*This code will be the entry point to program execution. The cnf file is read and an implication graph
 * is created using the 2-literal clauses. A transpose of this implication graph is also created during run time
 * to determine the Strongly Connected Components(SCC) in the implication graph.
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.naming.InvalidNameException;


public class Execute {		
	//Hash maps to store implication graph and the transpose implication graph
	public static final Map<Integer, ArrayList<Integer>> graphMap = new HashMap<>();
	public static final Map<Integer, ArrayList<Integer>> graphMapTrans = new HashMap<>();
	
	//Hash maps to indicate the whether vertices are visited or not during DFS
	public static final Map<Integer, Boolean> visitedOriginal = new HashMap<>();
	public static final Map<Integer, Boolean> visitedReverse = new HashMap<>();
	
	
	//Main function
	public static void main(String[] args) {
		try {
			getInput();
			Sat2Solve.process();
			if (Sat2Solve.isValid()) {
				System.out.println("FORMULA SATISFIABLE");
				Sat2Solve.solve();
			}
			else {
				System.out.println("FORMULA UNSATISFIABLE");
			}
		}
		catch(InvalidNameException e) {
			System.out.println("INVALID INPUT");
		}
		
		
	}
	
	//This function gets the input from the cnf file and constructs both the implication graph and its transpose
	public static void getInput() throws InvalidNameException {
		//Create a scanner object to get input
		Scanner consoleInput = null;
		System.out.println("Enter the location of the CNF test file. i.e.: D:\\Users\\2D\\Testcases\\test1.cnf");
		Scanner read = new Scanner(System.in);
		String loc = read.nextLine() ;
		loc = loc.replaceAll("\\\\", "/");
		//System.out.println("Reading " + loc );
		read.close();
		
		if (!loc.endsWith(".cnf")) {
			throw new InvalidNameException();
		}
		
		try {
			consoleInput = new Scanner(new FileReader(loc));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found. Exiting program...");
		}
		
		//Use a while loop to get valid input
		while (consoleInput.hasNext()) {
			if (consoleInput.hasNextInt()) {
				int val = consoleInput.nextInt();			
				int v1,v2;
				
				//0 indicates end of clause
				if (val != 0) {	
					 //If a valid value is found get the other literal of the clause and store them as v1 and v2
					 v1 = val;											
					 v2 = consoleInput.nextInt();	
					 
					 //Print out valid input
					 //System.out.println("v1: " + v1 + " v2: " + v2);
					 
					 //See below for function definition
					 putKey(v1,v2,graphMap);
					 putKey(-v1,-v2,graphMapTrans);
				}
			}
			
			if (consoleInput.hasNext("p")|| consoleInput.hasNext("c")) {
				consoleInput.nextLine();
			}
			
			
		}
		consoleInput.close();
		
		//Print out the hash maps that represent both the implication graph and its transpose
		//System.out.println(graphMap.toString());
		//System.out.println(graphMapTrans.toString());
	}
	
	
	/*This function will insert the vertices and create both the implication graphs and also mark the vertices 
	as unvisited in both the visit record hash maps*/
	public static void putKey(int v1,int v2,Map<Integer,ArrayList<Integer>> graphMap) {
		 //Mark vertices as unvisited which will be used for the first dfs 
		 visitedOriginal.put(-v1, false);
		 visitedReverse.put(-v1, false);
		 visitedOriginal.put(v2, false);
		 visitedReverse.put(v2, false);
		
		 //Mark vertices as unvisited which will be be used for the second dfs
		 visitedOriginal.put(-v2, false);
		 visitedReverse.put(-v2, false);
		 visitedOriginal.put(v1, false);
		 visitedReverse.put(v1, false);
		
		 //If graph contains the edge -v1, check whether it has a edge to v2. If not insert v2
		 if (graphMap.containsKey(-v1)){
			 if (!graphMap.get(-v1).contains(v2)) {
				 //System.out.println("Adding val " + v2);
				 graphMap.get(-v1).add(v2);
			 }
		 }
		 
		 
		 //If graph does not contain the edge -v1, create -v1 vertex and add v2 as an edge to it
		 if (!graphMap.containsKey(-v1)) {
			 ArrayList<Integer> x = new ArrayList<>();
			 x.add(v2);
			 graphMap.put(-v1,x);
			 
			 x = null;
		 }
		 
		 
		 //If graph contains the edge -v2, check whether it has a edge to v1. If not insert v1
		 if (graphMap.containsKey(-v2)){
			 if (!graphMap.get(-v2).contains(v1)) {
				 graphMap.get(-v2).add(v1);
			 }
		 }
		 
		 //If graph does not contain the edge -v2, create -v2 vertex and add v1 as an edge to it
		 if (!graphMap.containsKey(-v2)){
			 ArrayList<Integer> x = new ArrayList<>();
			 x.add(v1);
			 graphMap.put(-v2,x);
			 visitedOriginal.put(-v2, false);
			 visitedReverse.put(-v2, false);
			 x = null;
		 }
	}
}
