import java.util.Vector;


public class SizeConstraint extends Constraint {

	double PXL_TO_FT = 25.0/400.0;
	//double DROP_OFF_FACTOR = 50.0;
	double INITIAL_SIZE_SCORE = 0.5;
	
	public SizeConstraint(Room room) {
		super(room);
		// TODO Auto-generated constructor stub
	}
	
	//Evaluates to true of false
	public boolean satisfied(){
		Room room = getRoom();
		return room.getSqFootage() >= PXL_TO_FT * room.getType().width()*room.getType().height() - 1;
	}

	public double evaluate() {
		if(!satisfied()) return 0.0;
		Room room = getRoom();
		double min_size = room.getType().width()*room.getType().height()*PXL_TO_FT;
		if(min_size == 0) return 1.0;
		double extraRoom = room.getSqFootage() - min_size;
		double extraRoomPercentage = extraRoom/min_size;
		if (extraRoomPercentage >= 1)extraRoomPercentage = 1;
		return INITIAL_SIZE_SCORE + extraRoomPercentage*(1-INITIAL_SIZE_SCORE);
		//System.out.println((- room.getSqFootage() + min_size));
		//return 1/(1.0 + Math.exp((- room.getSqFootage() + min_size)/DROP_OFF_FACTOR));
	}

}
