
public class SizeConstraint extends Constraint {

	double PXL_TO_FT = 25.0/400.0;
	
	public SizeConstraint(Room room) {
		super(room);
		// TODO Auto-generated constructor stub
	}
	
	//Evaluates to true of false
	public boolean satisfied(){
		Room room = getRoom();
		return room.getSqFootage() >= PXL_TO_FT * room.getType().width()*room.getType().height();
	}

}
