

/**
 * A class representing a meeting slot, i.e. a time and a room
 * 
 * @author Bjørnar Tessem
 *
 */
public class MeetingSlot {

	// Time and Room are enumerations
	enum Time {
		MORNING, BEFORENOON, AFTERNOON, LATE
	};

	enum Room {
		RoomA, RoomB
	};

	// The fields of a meeting slot
	private Time time;
	private Room room;

	/**
	 * 
	 * @param t
	 *            the time
	 * @param r
	 *            the room
	 */
	public MeetingSlot(Time t, Room r) {
		time = t;
		room = r;
	}

	/**
	 * 
	 * @return the time
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * 
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}

	// Eight legal meeting slots
	public final static MeetingSlot MORNING_A = new MeetingSlot(Time.MORNING, Room.RoomA);
	public final static MeetingSlot MORNING_B = new MeetingSlot(Time.MORNING, Room.RoomB);
	public final static MeetingSlot BEFORENOON_A = new MeetingSlot(Time.BEFORENOON, Room.RoomA);
	public final static MeetingSlot BEFORENOON_B = new MeetingSlot(Time.BEFORENOON, Room.RoomB);
	public final static MeetingSlot AFTERNOON_A = new MeetingSlot(Time.AFTERNOON, Room.RoomA);
	public final static MeetingSlot AFTERNOON_B = new MeetingSlot(Time.AFTERNOON, Room.RoomB);
	public final static MeetingSlot LATE_A = new MeetingSlot(Time.LATE, Room.RoomA);
	public final static MeetingSlot LATE_B = new MeetingSlot(Time.LATE, Room.RoomB);

	/**
	 * a string representation of a meeting slot
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		switch (time) {
		case MORNING:
			result.append("08:00");
			break;
		case BEFORENOON:
			result.append("10:00");
			break;
		case AFTERNOON:
			result.append("12:00");
			break;
		case LATE:
			result.append("14:00");
			break;
		}
		result.append(" - ");
		switch (room) {
		case RoomA:
			result.append("Room A");
			break;
		case RoomB:
			result.append("Room B");
		}
		return result.toString();
	}

}
