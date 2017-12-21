//2-SAT solver implemented using Kosaraju's Algorithm
//Resources : http://www.geeksforgeeks.org/2-satisfiability-2-sat-problem/
//Resources : https://www.youtube.com/watch?v=RpgcYiky7uw
//Resources : https://www.youtube.com/watch?v=5wFyZJ8yH9Q

/*This code will use the implication graph and its transpose created from the Execute.java code 
 * and create a hash map of strongly connected components and indicate whether the system is satisfiable or not.
 * If solvable it will return a possible assignment for all the variables.*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
 
//This class defines methods and data fields that allow to verify and solve the 2-SAT problem
public class Sat2Solve {
	/*Stack to store the visited vertices in topological order which will be used to find strongly 
	connected components in the condensation graph that doesnt have any edges coming towards it*/
	static final Stack<Integer> stack = new Stack<Integer>(); 
	
	//Hash map to create a condensation graph of strongly connected components 
	static final Map<Integer, ArrayList<Integer>> condGraph = new HashMap<>();
	
	//Counter to keep track of the strongly connected component
	static int sccCounter = 1;
	
	//Store the solutions in an Arraylist
	static Map<Integer, Boolean> solMap = new HashMap<>();
	
	
	//This function allows to solve the equation if it is satisfiable
	public static void solve() {
		assignBoolVal(sccCounter);
	}
	
	
	//This DFS allows to visit all the adjacent vertices of any given vertex and add the vertices to the stack
	public static void dfsToFormStack(int u) {
		//Recursion base case 1
		if (Execute.visitedOriginal.get(u)) {
			return;
		}
		
		//Mark vertex as visited
		Execute.visitedOriginal.put(u, true);
		
		//Recursion base case 2
		if (Execute.graphMap.get(u) == null) {
			//System.out.println("Pushing " + u + " to the stack");
			stack.push(u);
			return;
		}
		
		//Recursively visit all the connected vertices for a given vertex
		for (int i = 0; i<Execute.graphMap.get(u).size(); i++) {
			//System.out.println("Recursively visiting " + Execute.graphMap.get(u).get(i));
			dfsToFormStack(Execute.graphMap.get(u).get(i));
		}
		
		//Push the vertex to the stack 
		//System.out.println("Pushing " + u + " to the stack");
		stack.push(u);
	}
	
	//This function runs a DFS on the transpose implication graph to identify the strongly connected components
	public static void reverseDfs(int u) {
		//Recursion base case 1
		if (Execute.visitedReverse.get(u)) {
			return;
		}
		
		//Mark the vertex as visited
		Execute.visitedReverse.put(u, true);
		
		//Recursion base case 2
		if (Execute.graphMapTrans.get(u) == null) {
			//System.out.println(u);
			//System.out.println( u + " belongs to the scc of " + sccCounter);
			createCondenGraph(u, sccCounter,condGraph);
			return;
		}
		
		//Recursively visit the vertices that are connected to a given vertex
		for (int i = 0; i<Execute.graphMapTrans.get(u).size(); i++) {
			//System.out.println(Execute.graphMapTrans.get(u).get(i));
			reverseDfs(Execute.graphMapTrans.get(u).get(i));
		}
		
		//Create the condensation graph
		//System.out.println( u + " belongs to the scc of " + sccCounter);
		createCondenGraph(u, sccCounter,condGraph);
	}
	
	
	//This method processes the input
	public static void process() {
		//Traverse the original implication graph to create the stack 
		for (Integer i: Execute.graphMap.keySet()) {
			//System.out.println(Execute.graphMap.keySet().toString());
			if (!Execute.visitedOriginal.get(i)) {
				dfsToFormStack(i);
			}
		}
		
		
		
		while (!stack.isEmpty()) {
			int val = stack.peek();
			//System.out.println("The top of the stack has a value of " + val);
			stack.pop();
			
			if (!Execute.visitedReverse.get(val)) {
				reverseDfs(val);
				sccCounter++;
			}
		}
		
		sccCounter--;
		//System.out.println(condGraph.toString());
	}
	
	
	//This function allows to create the condensation graph
	public static void createCondenGraph(int u, int scc, Map<Integer, ArrayList<Integer>> m) {
		//If strongly connected entity is not present create one and add the vertex to it. If present add the vertex to it
		if (m.containsKey(scc)) {
			if (m.get(scc) == null) {
				ArrayList<Integer> x = new ArrayList<>();
				x.add(u);
				m.put(scc, x);
			}
			else {
				m.get(scc).add(u);
			}
		}
		
		else {
			ArrayList<Integer> x = new ArrayList<>();
			x.add(u);
			m.put(scc, x);
		}
	}
	
	
	//This function detects for any cycle in the implication curve by checking whether a variable and its
	//negation belong to the same strongly connected component
	public static boolean isValid() {
		for (Integer i: condGraph.keySet()) {
			for (Integer x: condGraph.get(i)) {
				if (condGraph.get(i).contains(-x)) {
					return false;
				}
			}
		}
		return true;
	}
	
	//This function assigns boolean values to the given variables to find a feasible solution to the sat2 problem
	public static void assignBoolVal(int x) {
		for (int i = x; i>=1; i--) {
			for (Integer o: condGraph.get(i)) {
				if (!solMap.containsKey(o)) {
					solMap.put(o, true);
					solMap.put(-o, false);
				}
			}
		}
		
		for (int i = 1; i<=solMap.size()/2;i++) {
			boolean v = solMap.get(i);
			//System.out.println("x" + i + " is assigned " + v);
			if (v == true) {
				System.out.print("1 ");
			}
			else {
				System.out.print("0 ");
			}
		}
		//System.out.println(solMap.size());
	}
}
