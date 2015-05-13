import java.util.Vector;


public class Constraint {

	public Constraint(Room room) {
		this.room = room;
		this.type = room.getType();
	}
	
	public Constraint(RoomType type) {
		this.type = type;
	}
	
	public Room getRoom(){
		return room;
	}
	
	public RoomType getType(){
		return type;
	}
	
	public String getConstraintType() {
		if (this instanceof AdjacencyConstraint) {
			return "Adjacency";
		} else if (this instanceof SizeConstraint) {
			return "Size";
		} else if (this instanceof CountConstraint) {
			return "Count";
		} else {
			return "Misc";
		}
	}
	
	private
		Room room;
		RoomType type;

}
