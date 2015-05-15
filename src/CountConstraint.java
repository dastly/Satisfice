import java.util.Vector;


public class CountConstraint extends Constraint {

	public CountConstraint(RoomType type) {
		super(type);
	}
	
	//Evaluates to a number between 0.0 and 2.0
	public double evaluate(Vector<Room> rooms){
		int count = 0;
		for(Room room: rooms){
			if(room.getType() == getType()){
				count++;
			}
		}
		if(type.strict() > 0) {
			return (type.strict() == count) ? 1.0 : 0.0;
		}
		if(count >= type.min() && count < type.best()) return (count - type.min() + 1) * 1.0/(type.best() - type.min() + 1.0);
		if(count >= type.best()) return 1.0;
		return 0.0;
	}

}
