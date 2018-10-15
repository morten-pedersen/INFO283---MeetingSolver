

import java.util.HashSet;
import java.util.Set;

/**
 * Represent the meeting nodes, i.e. variables that are involved in a meeting
 * problem
 * 
 * @author Bjørnar Tessem
 *
 */
public class MeetingNode {

	/**
	 * the meeting slots this node can have
	 */
	Set<MeetingSlot> domain = new HashSet<MeetingSlot>();

	/**
	 * an assignment of this meeting node
	 */
	MeetingSlot assignment = null;

	/**
	 * the name of the meeting node
	 */
	String name;

	/**
	 * 
	 * @param name
	 */
	public MeetingNode(String name) {
		// initially the domain of a meeting node is alle possible slots
		domain.add(MeetingSlot.MORNING_A);
		domain.add(MeetingSlot.MORNING_B);
		domain.add(MeetingSlot.BEFORENOON_A);
		domain.add(MeetingSlot.BEFORENOON_B);
		domain.add(MeetingSlot.AFTERNOON_A);
		domain.add(MeetingSlot.AFTERNOON_B);
		domain.add(MeetingSlot.LATE_A);
		domain.add(MeetingSlot.LATE_B);
		this.name = name;
	}

	public String toString() {
		StringBuffer result = new StringBuffer(name);
		// for (MeetingSlot ms : domain) result.append(ms + " ");
		return result.toString();
	}

}
