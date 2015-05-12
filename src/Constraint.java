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
	
	private
		Room room;
		RoomType type;

}
