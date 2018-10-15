

import java.util.HashSet;
import java.util.Set;

/**
 * The format of a meeting constraint. It has nodes/variables and a single test
 * 
 * @author Bjørnar Tessem
 *
 */
public abstract class MeetingConstraint {

	Set<MeetingNode> nodes; // the set of nodes involved in a constraint
	String name;

	public MeetingConstraint(String name, MeetingNode... nodes) {
		this.name = name;
		this.nodes = new HashSet<MeetingNode>();
		for (int i = 0; i < nodes.length; i++)
			this.nodes.add(nodes[i]);
	}

	/**
	 * 
	 * @return true if the constraint is satisfied
	 */
	public abstract boolean constraint();

	public String toString() {
		return name;
	}

}
