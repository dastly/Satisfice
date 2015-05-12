import java.util.Vector;


public class CountConstraint extends Constraint {

	public CountConstraint(RoomType type) {
		super(type);
	}
	
	public double evaluate(Vector<Room> rooms){
		int count = 0;
		for(Room room: rooms){
			if(room.getType() == getType()){
				count++;
			}
		}
		if(type.strict() > 0) {
			return (type.strict() == count) ? 2.0 : 0.0;
		}
		if(count >= type.min() && count < type.best()) return 1.0;
		if(count >= type.best()) return 2.0;
		return 0.0;
	}

}
