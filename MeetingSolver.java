

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * The backtracking search CSP solver for the meeting problem
 * 
 * @author Bjørnar
 * 
 */
public class MeetingSolver {

	/**
	 * the main function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Get current time
		long start = System.currentTimeMillis();
		MeetingSolver s = new MeetingSolver(); // make a new solver
		s.initialize(); // initialize
		MeetingAssignment sol = s.search(); // find a first solution
		int numberOfSolutions = 0;
		while (sol != null) {
			numberOfSolutions++;
			System.out.println("Romfordeling " + sol); // print the solution
			sol = s.continueSearch(); // find the next solution
		}

		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		System.out.println("Search time: " + elapsedTimeSec);
		System.out.println("Visited internal nodes in search: " + s.visited.size());
		System.out.println("Number of solutions (leafs): " + numberOfSolutions);
	}

	/**
	 * the data structure to handle the backtracking (depth-first
	 * search-algorithm)
	 */
	private Stack<MeetingAssignment> frontier;

	/**
	 * visited assignments
	 */
	private HashSet<MeetingAssignment> visited = new HashSet<MeetingAssignment>();

	/**
	 * the variables of the problem
	 */
	private Set<MeetingNode> variables;

	/**
	 * the constraints of the problem
	 */
	private Set<MeetingConstraint> constraints;

	/**
	 * runs an initialization of search
	 * 
	 */
	private void initialize() {
		// first of all, runs an arcConsistency algorithm
		// on the same problem
		MeetingConstraintGraph cg = new MeetingConstraintGraph();
		cg.arcConsistency();

		// initiates the variables and constraints
		variables = MeetingProblem.getMeetingProblem().variables;
		constraints = MeetingProblem.getMeetingProblem().constraints;
	}

	/**
	 * run an initialization and a search
	 * 
	 * @return a solution meeting assignment
	 */
	public MeetingAssignment search() {
		initializeFrontier();
		return continueSearch();
	}

	/**
	 * make frontier an empty stack and pushes an initial meeting assignment (0
	 * assigments)
	 */
	private void initializeFrontier() {
		frontier = new Stack<MeetingAssignment>();
		frontier.push(new MeetingAssignment(variables, constraints));
	}

	public MeetingAssignment continueSearch() {
		while (!frontier.isEmpty()) { // still some nodes to be checked
			MeetingAssignment node = frontier.pop();// get the top node of the
													// stack
			if (goal(node)) { // if it is a goal node we are finished
				return node;
			}
			updateFrontier(node); // if not find new node for the stack
		}
		return null; // if no solutions found return null
	}

	private void updateFrontier(MeetingAssignment node) {
		Collection<MeetingAssignment> newNodes = node.neighbours(); // find
																	// neighbours
																	// of node
		visited.add(node); // add node to visited
		for (MeetingAssignment n : newNodes) {
			if (!visited.contains(n)) { // if a node n has been visited we do
										// not need to do it again
				frontier.push(n); // add n to the frontier stack
			}
		}
	}

	private boolean goal(MeetingAssignment node) {
		return (node != null && // a null node is not a goal
				node.allAssigned() && // a node need to have all guests assigned
										// (invited or not)
				node.consistent()); // a node need to be consistent with the
									// requirements in its definition
	}
}
