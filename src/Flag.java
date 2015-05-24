import java.awt.Color;
import java.util.Vector;

import acm.graphics.*;


public class Flag extends GCompound {
	
	public Flag(String name, FlagType type, boolean onLeft) {
		this.name = name;
		this.type = type;
		this.onLeft = onLeft;
		label = new GLabel(name);
		shape = new GRoundRect(label.getWidth() + 4,label.getHeight());
		shape.setLocation(0, -shape.getHeight()/2);
		label.setLocation(2, label.getAscent()-shape.getHeight()/2);
		add(shape);
		add(label);
	}
	
//	public Vector<Constraint> getConstraints(){
//		Vector<Constraint> constraints = new Vector<Constraint>();
//		for(Button button: buttons){
//			constraints.addAll(button.getConstraints());
//		}
//		for(Room room: rooms){
//			constraints.addAll(room.getConstraints());
//		}
//		return constraints;
//	}
	
	public void setLabel(double satisfaction){
		this.label.setLabel(name + ": " + satisfaction);
		shape.setSize(this.label.getWidth() + 4,this.label.getHeight());
		shape.setLocation(0, -shape.getHeight()/2);
		this.label.setLocation(2, this.label.getAscent()-shape.getHeight()/2);
	}
	
	//Returns value roughly between 0.0 and 1.0
	public double satisfaction(Vector<Button> buttons, Vector<Room> allRooms, Vector<Room> selectedRooms) {
		double score = 0.0;
		Vector<Room> flagRooms = (type == FlagType.SELADJACENCY || type == FlagType.SELSIZE) ? selectedRooms : allRooms;
		
		if(type == FlagType.COUNT){
			for(Button button: buttons){
				score += button.getCountConstraint().evaluate(allRooms);
			}
			return score/buttons.size();
		}
		if(flagRooms.isEmpty()) return 0.0;
		for(Room room: flagRooms){
			//if(floorPlanContains(room))
				if(type == FlagType.ADJACENCY || type == FlagType.SELADJACENCY)
					score += room.getAdjacencyConstraint().evaluate(allRooms);
				if(type == FlagType.SIZE || type == FlagType.SELSIZE)
					score += room.getSizeConstraint().evaluate();
		}
		return score/flagRooms.size();
	}
	
	public boolean onLeft(){
		return onLeft;
	}
	
	public void setColor(Color color){
		shape.setFillColor(color);
		shape.setFilled(true);
	}
	
	public String getName() {
		return name;
	}
	
	public FlagType getType(){
		return type;
	}
	
	private
		String name;
		GRoundRect shape;
		GLabel label;
		boolean onLeft;
		FlagType type;
}
