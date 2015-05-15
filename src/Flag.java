import java.util.Vector;

import acm.graphics.*;


public class Flag extends GCompound {
	
	public Flag(String name, Vector<Button> buttons, Vector<Room> rooms, FlagType type, boolean onLeft) {
		this.name = name;
		this.buttons = buttons;
		this.rooms = rooms;
		this.type = type;
		this.onLeft = onLeft;
		label = new GLabel(name);
		shape = new GRoundRect(label.getWidth() + 4,label.getHeight());
		shape.setLocation(0, -shape.getHeight()/2);
		label.setLocation(2, label.getAscent()-shape.getHeight()/2);
		add(shape);
		add(label);
	}
	
	public Vector<Constraint> getConstraints(){
		Vector<Constraint> constraints = new Vector<Constraint>();
		for(Button button: buttons){
			constraints.addAll(button.getConstraints());
		}
		for(Room room: rooms){
			constraints.addAll(room.getConstraints());
		}
		return constraints;
	}
	
	public void setLabel(double satisfaction){
		this.label.setLabel(name + ": " + satisfaction);
		shape.setSize(this.label.getWidth() + 4,this.label.getHeight());
		shape.setLocation(0, -shape.getHeight()/2);
		this.label.setLocation(2, this.label.getAscent()-shape.getHeight()/2);
	}
	
	public double satisfaction(Vector<Room> allRooms) {
		double score = 0.0;
		
		if(type == FlagType.COUNT){
			for(Button button: buttons){
				score += button.getCountConstraint().evaluate(rooms);
			}
			return score/buttons.size();
		}
		if(rooms.isEmpty()) return 0.0;
		for(Room room: rooms){
			//if(floorPlanContains(room))
				if(type == FlagType.ADJACENCY)
					score += room.getAdjacencyConstraint().evaluate(allRooms);
				if(type == FlagType.SIZE)
					score += room.getSizeConstraint().evaluate();
		}
		return score/rooms.size();
	}
	
	public Vector<Room> getRooms(){
		return rooms;
	}
	
	public Vector<Button> getButtons(){
		return buttons;
	}
	
	public boolean onLeft(){
		return onLeft;
	}
	
	private
		String name;
		Vector<Button> buttons;
		Vector<Room> rooms;
		GRoundRect shape;
		GLabel label;
		boolean onLeft;
		FlagType type;
}
