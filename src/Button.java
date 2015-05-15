import java.awt.Color;
import java.util.Vector;

import acm.graphics.*;


public class Button extends GCompound {

	int ADD = 0;
	int VIEW = 1;
	
	public Button(double arg0, double arg1, RoomType roomType, int buttonType) {
		GRoundRect shape = new GRoundRect(arg0, arg1);
		this.roomType = roomType;
		this.buttonType = buttonType;
		shape.setFillColor(roomType.color());
		shape.setFilled(true);
		add(shape);
		if (buttonType == ADD) {
			add(new GLabel("ADD"), 12, 20);
		} else if (buttonType == VIEW) {
			add(new GLabel("VIEW"), 9, 20);
		}
		constraints = new Vector<Constraint>();
		cc = new CountConstraint(roomType);
		constraints.add(cc);
	}
	
	public RoomType getRoomType(){
		return roomType;
	}
	
	public int getButtonType() {
		return buttonType;
	}
	
	public Vector<Constraint> getConstraints(){
		return constraints;
	}
	
	public CountConstraint getCountConstraint(){
		return cc;
	}
	
	private
		RoomType roomType;
		int buttonType;
		Vector<Constraint> constraints;
		CountConstraint cc;
	
}

