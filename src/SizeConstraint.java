import java.util.Vector;


public class SizeConstraint extends Constraint {

	double PXL_TO_FT = 25.0/400.0;
	double DROP_OFF_FACTOR = 100.0;
	
	public SizeConstraint(Room room) {
		super(room);
		// TODO Auto-generated constructor stub
	}
	
	//Evaluates to true of false
	public boolean satisfied(){
		Room room = getRoom();
		return room.getSqFootage() >= PXL_TO_FT * room.getType().width()*room.getType().height();
	}

	public double evaluate() {
		if(!satisfied()) return 0.0;
		Room room = getRoom();
		double min_size = room.getType().width()*room.getType().height()*PXL_TO_FT;
		//System.out.println((- room.getSqFootage() + min_size));
		return 1/(1.0 + Math.exp((- room.getSqFootage() + min_size)/DROP_OFF_FACTOR));
	}

}
