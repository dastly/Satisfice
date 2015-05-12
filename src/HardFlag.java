import java.util.Vector;


public class HardFlag extends Flag {

	public HardFlag(String name, Vector<Button> buttons, Vector<Room> rooms) {
		super(name, buttons, rooms);
		// TODO Auto-generated constructor stub
	}
	
	//evaluates to a number between 0.0 and 1.0
	@Override
	public double satisfaction() {	
		return computeRoomCountScore();
	}
	
	//evaluates to a number between 0.0 and 1.0
	private double computeRoomCountScore(){
		int countScore = 0;
		int maxScore = 14;
		for(Button button: getButtons()){
			countScore += button.getCountConstraint().evaluate(getRooms());
		}
		return countScore / (1.0 *maxScore);
	}
	
	//evaluates to a number between 0.0 and 1.0
	private double computeRoomSizeScore(){
		int countScore = 0;
		int maxScore = 7;
		for(Room room: getRooms()){
			if(room.getSizeConstraint().satisfied()){
				countScore++;
			}
		}
		return (countScore*1.0)/maxScore;
	}
}
