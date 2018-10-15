

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is a representation of a constraint satisfaction problem that is
 * about assigning meeting rooms and times to a set of meetings. The meeting
 * problem is represented as a singleton pattern that can be obtained through
 * MeetingProblem.getMeetingProblem();
 * 
 * @author Bjørnar Tessem
 *
 */
public class MeetingProblem {

	Set<MeetingNode> variables; // the variables to be assigned in the CSP
	Set<MeetingConstraint> constraints; // the constraints of the CSP

	// a collection of meetings
	final MeetingNode Meeting1 = new MeetingNode("Planlegging prosjekt A");
	final MeetingNode Meeting2 = new MeetingNode("Planlegging prosjekt B");
	final MeetingNode Meeting3 = new MeetingNode("Styremøte");
	final MeetingNode Meeting4 = new MeetingNode("Kontraktmøte kunde");
	final MeetingNode Meeting5 = new MeetingNode("Kaffemøte");
	final MeetingNode Meeting6 = new MeetingNode("Spesialmøte 1");
	final MeetingNode Meeting7 = new MeetingNode("Krisemøte!");
	final MeetingNode Meeting8 = new MeetingNode("Kakemøte");

	// Constraint: meeting1 should be held before meeting2
	final MeetingConstraint M1BeforeM2 = new MeetingConstraint("M1<M2", Meeting1, Meeting2) {
		@Override
		public boolean constraint() {
			return Meeting1.assignment.getTime().ordinal() < Meeting2.assignment.getTime().ordinal();
		}
	};

	// Constraint: meeting3 cannot be held at same time as meeting4
	final MeetingConstraint M3UnequalM4 = new MeetingConstraint("M3!=M4", Meeting3, Meeting4) {
		@Override
		public boolean constraint() {
			return Meeting3.assignment.getTime().ordinal() != Meeting4.assignment.getTime().ordinal();
		}
	};

	// Constraint: meeting3 needs to be held in Room3
	final MeetingConstraint M3RoomB = new MeetingConstraint("M3R3", Meeting3) {
		@Override
		public boolean constraint() {
			return Meeting3.assignment.getRoom() == MeetingSlot.Room.RoomB;
		}
	};

	// Constraint: meeting 5 must be held after meeting 3
	final MeetingConstraint M5AfterM3 = new MeetingConstraint("M5AfterM3", Meeting5, Meeting3) {
		@Override
		public boolean constraint() {
			return Meeting5.assignment.getTime().ordinal() > Meeting3.assignment.getTime().ordinal();
		}
	};

	//  Constraint: meeting 8 cannot be at the same time as meeting 5
	final MeetingConstraint M8UnequalM5 = new MeetingConstraint("M8UnequalM5", Meeting8, Meeting5) {
		@Override
		public boolean constraint() {
			return Meeting8.assignment.getTime().ordinal() != Meeting5.assignment.getTime().ordinal();
		}
	};

	// Constraint: Meeting 7 must be in the morning at 08:00
	final MeetingConstraint M7Time = new MeetingConstraint("M7Time", Meeting7) {
		@Override
		public boolean constraint() {
			return Meeting7.assignment.getTime() == MeetingSlot.Time.MORNING;
		}
	};

	// Constraint: Meeting 6 can only be later than 08:00 and after Meeting 7
	final MeetingConstraint M6NotMorning = new MeetingConstraint("M6NotMorning", Meeting6, Meeting7) {
		@Override
		public boolean constraint() {
			return Meeting6.assignment.getTime() != MeetingSlot.Time.MORNING &&
					Meeting6.assignment.getTime().ordinal() > Meeting7.assignment.getTime().ordinal();
		}
	};

	// Constraint: Meeting 8 must be in room A
	final MeetingConstraint M8RoomA = new MeetingConstraint("M8RoomA", Meeting8) {
		@Override
		public boolean constraint() {
			return Meeting8.assignment.getRoom() == MeetingSlot.Room.RoomA;
		}
	};



	/**
	 * The problem also has a constraint that two meetings cannot be held in the
	 * same room at the same time. This method ensures this. This could have
	 * been modeled as added constraints, but that would be very cumbersome with
	 * many meetings. So this is a method that makes both the search and the
	 * problem specification more efficient
	 * 
	 * @param assignedNodes
	 *            the nodes/variables that has an assignment
	 * @return true if the assignment does not contain double bookings
	 */
	public boolean otherConstraints(List<MeetingNode> assignedNodes) {
		Set<MeetingSlot> used = new HashSet<MeetingSlot>(); // a list of used
															// slots
		for (int i = 0; i < assignedNodes.size(); i++) { // check all assigned
															// Nodes
			if (used.contains(assignedNodes.get(i).assignment)) // if an
																// assignment is
																// used,
				return false; // the constraint is broken
			used.add(assignedNodes.get(i).assignment); // remember assignment
		}
		return true; // no double bookings
	}

	/**
	 * the constructor for the meeting problem. It is private as it can only be
	 * used in this class. To access the meeting problem you need to use the
	 * static method getMeetingProblem
	 */
	private MeetingProblem() {
		variables = new HashSet<MeetingNode>();
		constraints = new HashSet<MeetingConstraint>();

		// add all meetings to variables
		variables.add(Meeting1);
		variables.add(Meeting2);
		variables.add(Meeting3);
		variables.add(Meeting4);
		variables.add(Meeting5);
		variables.add(Meeting6);
		variables.add(Meeting7);
		variables.add(Meeting8);

		// add constraints to set of constraints
		constraints.add(M1BeforeM2);
		constraints.add(M3UnequalM4);
		constraints.add(M3RoomB);
		constraints.add(M5AfterM3);
		constraints.add(M8UnequalM5);
		constraints.add(M7Time);
		constraints.add(M6NotMorning);
		constraints.add(M8RoomA);
	}

	// The single meeting problem object
	private static MeetingProblem theMeetingProblem = null;

	/**
	 *
	 * @return the singleton meeting problem
	 */
	public static MeetingProblem getMeetingProblem() {
		if (theMeetingProblem == null) { // if not yet initiated
			theMeetingProblem = new MeetingProblem(); // initiate the meeting
														// problem
		}
		return theMeetingProblem;
	}

}
