

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * The constraint graph representation and methods for doing arc consistency for
 * the meeting problem
 * 
 * @author Bjørnar Tessem
 *
 */
public class MeetingConstraintGraph {

	/**
	 * the meeting problem
	 */
	private MeetingProblem theProblem;

	/**
	 * sets the meeting problem
	 */
	public MeetingConstraintGraph() {
		theProblem = MeetingProblem.getMeetingProblem();
	}

	/**
	 * a main method, runs arc consistency and print final domains
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MeetingConstraintGraph cg = new MeetingConstraintGraph();
		cg.arcConsistency();
		cg.printDomains();
	}

	/**
	 * prints the domains of the problem
	 */
	private void printDomains() {
		for (MeetingNode n : theProblem.variables) {
			System.out.println(n.name);
			for (MeetingSlot slot : n.domain) {
				System.out.println(slot);
			}
		}

	}

	/**
	 * the arc consistency algorithm
	 */
	public void arcConsistency() {
		// make a linked list that works as queue of GCArcs
		LinkedList<CGArc> tda = new LinkedList<CGArc>();

		// for all constraints add one arc for each node involved in
		// the constraint
		for (MeetingConstraint mc : theProblem.constraints) {
			for (MeetingNode n : mc.nodes) {
				tda.add(new CGArc(mc, n));
			}
		}

		while (!tda.isEmpty()) { // there are still CGArcs to test

			CGArc arc = tda.remove(); // get the first CGArc
			Set<MeetingSlot> newDomain = getNewDomain(arc); // compute the new
															// domain for the
															// arc
			if (!arc.variable.domain.equals(newDomain)) { // if domain changed

				arc.variable.domain = newDomain; // change domain to new domain
				for (MeetingConstraint mc : theProblem.constraints) {
					if (mc != arc.constraint && mc.nodes.contains(arc.variable)) { 
															// if some constraint
															// contains the
															// changed variable
						for (MeetingNode n : mc.nodes) { // add new arcs to the
															// tda queue
							if (n != arc.variable)
								addCGArc(tda, n, mc);
						}
					}
				}
				checkSingletons(arc, tda); // problem specific check for domains
											// with only one possible meeting
											// slot
			}

		}

	}

	/**
	 * finds singleton domains and updates the tda based on this This is a
	 * method special for this problem due to the no double booking constraint.
	 * It is added for efficiency
	 * 
	 * @param e
	 *            an arc
	 * @param tda
	 *            the tda queue
	 */
	private void checkSingletons(CGArc e, LinkedList<CGArc> tda) {
		if (e.variable.domain.size() == 1) { // only do something if arcs domain
												// is a singleton

			MeetingSlot fixed = getSingleSlot(e); // gets this single slot which
													// now is fixed
													// to a particular meeting
			for (MeetingNode n : theProblem.variables) { // for all other
															// variables
				if (n != e.variable && n.domain.remove(fixed)) { // remove this
																	// used slot
																	// if
																	// removed
					for (MeetingConstraint mc : theProblem.constraints) { // check
																			// all
																			// constraints
						if (mc.nodes.contains(n)) { // if the constraint
													// involves n go through the
													// other
													// nodes in the constraint
													// and add
													// to tda
							Iterator<MeetingNode> it2 = mc.nodes.iterator();
							while (it2.hasNext()) {
								MeetingNode n2 = it2.next();
								if (n2 != n)
									addCGArc(tda, n2, mc); //
							}
						}
					}
				}
			}
		}
	}

	/**
	 * creates a CGArc and adds to the tda
	 * 
	 * @param tda
	 * @param n
	 *            the node in the arc
	 * @param m
	 *            the constraint in the arc
	 */
	public void addCGArc(LinkedList<CGArc> tda, MeetingNode n, MeetingConstraint m) {
		CGArc newArc = new CGArc(m, n);
		if (!tda.contains(newArc)) { // check if already there
			tda.add(newArc);
		}
	}

	/**
	 * the slot of a singleton domain
	 * 
	 * @param e
	 *            the CGArc
	 * @return the singleton slot
	 */
	public MeetingSlot getSingleSlot(CGArc e) {
		Iterator<MeetingSlot> it = e.variable.domain.iterator();
		MeetingSlot fixed = it.next();
		return fixed;
	}

	/**
	 * the method that finds the new domain for a node in an CGArc
	 * 
	 * @param arc
	 * @return a new domain
	 */
	private Set<MeetingSlot> getNewDomain(CGArc arc) {
		Set<MeetingSlot> result = new HashSet<MeetingSlot>();

		// represent all nodes in an arraylist (easier to compute with than a
		// set)
		ArrayList<MeetingNode> nodesInConstraint = new ArrayList<MeetingNode>();
		nodesInConstraint.addAll(arc.constraint.nodes);

		// if domain is empty for some of the nodes, then all are empty
		if (someEmptyDomain(nodesInConstraint))
			return result;

		// the position of the arc's variable among the nodes in the constraint
		int positionOfVariable = nodesInConstraint.indexOf(arc.variable);

		// the domain values for all nodes in the constraint represented in
		// an arrayList where each element is an array list of meeting slots
		ArrayList<ArrayList<MeetingSlot>> domainValues = new ArrayList<ArrayList<MeetingSlot>>();
		for (int i = 0; i < nodesInConstraint.size(); i++) {
			ArrayList<MeetingSlot> slots = new ArrayList<MeetingSlot>();
			slots.addAll(nodesInConstraint.get(i).domain);
			domainValues.add(slots);
		}

		// To test all possible combinations of values we need to loop
		// through an assignment of values which is best represented in an array
		// of indices into the domain lists
		// (at least if you want to do something general)
		int[] assignmentIndexes = new int[nodesInConstraint.size()];

		// before we start looping through assignments we need to initialize
		// the indices
		for (int i = 0; i < assignmentIndexes.length; i++)
			assignmentIndexes[i] = 0;
		assignmentIndexes[0] = -1;

		// each domain has different sizes
		int[] maxAssignmentIndexes = new int[nodesInConstraint.size()];
		for (int i = 0; i < maxAssignmentIndexes.length; i++)
			maxAssignmentIndexes[i] = domainValues.get(i).size();

		// we do the first increment of indexes
		boolean successfulIncrement = increment(assignmentIndexes, maxAssignmentIndexes);

		while (successfulIncrement) {

			// we set the next assignment for all variables in constraint
			for (int i = 0; i < assignmentIndexes.length; i++) {
				nodesInConstraint.get(i).assignment = domainValues.get(i).get(assignmentIndexes[i]);
			}

			// is the assignment consistent we have a new possible value for the
			// arc's variable
			if (theProblem.otherConstraints(nodesInConstraint) && arc.constraint.constraint()) {
				result.add(domainValues.get(positionOfVariable).get(assignmentIndexes[positionOfVariable]));
			}

			// increment again
			successfulIncrement = increment(assignmentIndexes, maxAssignmentIndexes);
		}

		return result;
	}

	/**
	 * checks if some domain is empty for some nodes in a constraint
	 * 
	 * @param nodesInConstraint
	 * @return true if one domain is empty
	 */
	private boolean someEmptyDomain(ArrayList<MeetingNode> nodesInConstraint) {
		for (MeetingNode m : nodesInConstraint) {
			if (m.domain.size() == 0)
				return true;
		}
		return false;
	}

	/**
	 * the smart increment algorithm for assignment indices
	 * 
	 * @param assignmentIndexes
	 * @param maxAssignmentIndexes
	 * @return true if increment is successful
	 */
	private boolean increment(int[] assignmentIndexes, int[] maxAssignmentIndexes) {
		for (int i = 0; i < assignmentIndexes.length; i++) {
			assignmentIndexes[i]++;
			if (assignmentIndexes[i] < maxAssignmentIndexes[i])
				return true;
			assignmentIndexes[i] = 0;
		}
		return false;
	}

}
