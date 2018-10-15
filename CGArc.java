

/**
 * This class is a represention of an arc in a constraint graph
 * 
 * @author Bjørnar Tessem
 *
 */
public class CGArc {

	/**
	 * 
	 * @param mc
	 *            a single meeting constraint
	 * @param n
	 *            a single variable
	 */
	public CGArc(MeetingConstraint mc, MeetingNode n) {
		constraint = mc;
		variable = n;
	}

	// the fields of the GCArc
	MeetingNode variable;
	MeetingConstraint constraint;

	/**
	 * CGArcs are equal if they have the same variable and constraint
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof CGArc) {
			CGArc e = (CGArc) o;
			return e.variable == this.variable && e.constraint == this.constraint;
		} else {
			return false;
		}
	}

	public String toString() {
		return constraint + " " + variable;
	}
}
