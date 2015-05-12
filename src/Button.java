import java.awt.Color;
import java.util.Vector;

import acm.graphics.*;


public class Button extends GCompound {

	public Button(double arg0, double arg1, RoomType type) {
		GRoundRect shape = new GRoundRect(arg0, arg1);
		this.type = type;
		shape.setFillColor(type.color());
		shape.setFilled(true);
		add(shape);
		add(new GLabel("ADD " + type.label(), 5, 15), 5, 15);
		constraints = new Vector<Constraint>();
		cc = new CountConstraint(type);
		constraints.add(cc);
	}
	
	public RoomType getType(){
		return type;
	}
	
	public Vector<Constraint> getConstraints(){
		return constraints;
	}
	
	public CountConstraint getCountConstraint(){
		return cc;
	}
	
	private
		RoomType type;
		Vector<Constraint> constraints;
		CountConstraint cc;
	
}

