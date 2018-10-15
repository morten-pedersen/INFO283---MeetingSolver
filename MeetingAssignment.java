

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An assignment of variables in a meeting problem. It may not be complete as it
 * mainly is used as a node in a backtracking search for solution to the meeting
 * problem
 * 
 * @author Bjørnar Tessem
 *
 */
public class MeetingAssignment {

	/**
	 * the variables that can have assignments
	 */
	ArrayList<MeetingNode> variables = new ArrayList<MeetingNode>();

	/**
	 * all the defined constraints
	 */
	Set<MeetingConstraint> constraints = new HashSet<MeetingConstraint>();

	/**
	 * the corresponding assignments for the variables assignment.get(i) is the
	 * assignment given to variables.get(i) This list needs to be maintained so
	 * it is not forgotten during search as all manipulation of assignments are
	 * done on the few meeting nodes
	 */
	ArrayList<MeetingSlot> assignments;

	/**
	 * the number of assigned variables
	 */
	private int assignedCount = 0;

	/**
	 * Initial constructor
	 * 
	 * @param variables
	 *            the variables to be assigned meeting slots
	 * @param constraints
	 *            the constraints
	 */
	public MeetingAssignment(Set<MeetingNode> variables, Set<MeetingConstraint> constraints) {
		this.variables.addAll(variables);
		this.constraints = constraints;
		assignments = new ArrayList<MeetingSlot>(); // no assignments are made,
													// hence empty list
	}

	/**
	 * Copy constructor
	 * 
	 * @param meetingAssignment
	 */
	public MeetingAssignment(MeetingAssignment meetingAssignment) {
		// copy variables and constraints
		this.variables.addAll(meetingAssignment.variables);
		this.constraints.addAll(meetingAssignment.constraints);

		// make assignment as copy of original assignments
		assignments = new ArrayList<MeetingSlot>();
		assignments.addAll(meetingAssignment.assignments);
	}

	/**
	 * finds the consistent neighbours of an assignment
	 * 
	 * @return a collection of consistent assignments to next variable in line
	 */
	public Collection<MeetingAssignment> neighbours() {

		// some initialization
		ArrayList<MeetingAssignment> result = new ArrayList<MeetingAssignment>();

		// if already all assignments
		if (allAssigned())
			return result;
		MeetingAssignment n;

		// find the meeting node to be assigned value
		MeetingNode next = variables.get(assignedCount);

		// find its domain
		Set<MeetingSlot> domain = next.domain;

		for (MeetingSlot slot : domain) { // for all values in domain
			n = copyAndAddAssignment(slot); // copy assignments and add one more
			if (n != null && n.consistent())
				result.add(n); // if new assignments are ok add to result
		}
		return result;
	}

	/**
	 * Copies an assigment and adds one more assignment
	 * 
	 * @param slot
	 *            the assignement value for the next variable
	 * @return a single new MeetingAssignment with one new variabled assigned a
	 *         value
	 */
	private MeetingAssignment copyAndAddAssignment(MeetingSlot slot) {
		MeetingAssignment n = new MeetingAssignment(this); // make a new
															// assignment as a
															// copy
		n.assignments.add(slot); // add an assignment
		n.assignedCount = this.assignedCount + 1; // set number of assigned
													// variables
		return n;
	}

	/**
	 * 
	 * @return true if alle variables have an assignment
	 */
	public boolean allAssigned() {
		return assignedCount == variables.size();
	}

	/**
	 * Checks if a partial assignment is consistent
	 * 
	 * @return true if assignment is consistent so far
	 */
	public boolean consistent() {
		applyAssignment(); // sets the assignment to the meeting nodes
		if (!MeetingProblem.getMeetingProblem().otherConstraints(variables.subList(0, assignedCount))) // check
																										// otherConstraint
			return false;
		for (MeetingConstraint mc : constraints) {
			boolean applies = checkApplication(mc); // tests if constraint
													// applies,
													// i.e. only if all nodes in
													// constraint mc has
													// been assigned
			if (applies) {
				if (!mc.constraint())
					return false; // if the constraint applies test it
			}
		}
		return true; // passes all constraints
	}

	/**
	 * Sets the meeting nodes' assignment
	 */
	private void applyAssignment() {
		for (int i = 0; i < assignments.size(); i++) {
			variables.get(i).assignment = assignments.get(i);
		}
	}

	/**
	 * Checks if the variables so far assigned is involved in a constraint
	 * 
	 * @param mc
	 * @return
	 */
	private boolean checkApplication(MeetingConstraint mc) {

		// find the assigned variables
		List<MeetingNode> assigned = variables.subList(0, assignedCount);

		// check all variables in constraint if it has been assigned
		for (MeetingNode n : mc.nodes) {
			if (!assigned.contains(n))
				return false;
		}
		return true;
	}

	/**
	 * a string representation of a complete assignment
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (MeetingNode n : variables) {
			result.append(n.name + " : " + n.assignment + "  ");
		}
		return result.toString();
	}

}
