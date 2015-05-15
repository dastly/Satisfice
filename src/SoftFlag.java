import java.util.Vector;


public class SoftFlag extends Flag {

	public SoftFlag(String name, Vector<Button> buttons, Vector<Room> rooms) {
		super(name, buttons, rooms);
	}
	
	double ADJACENCY_THRESHOLD = 20.0;
	
	//Evaluates to a number between 0.0 and 1.0
	@Override
	public double satisfaction() {	
		if(getRooms().isEmpty()) return 0.0;
		double score = 0.0;
		for(Room room: getRooms()){
			//if(floorPlanContains(room))
				score += room.getAdjacencyConstraint().evaluate(getRooms());
		}
		return score/getRooms().size();
	}

}
