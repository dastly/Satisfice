import java.awt.Color;

import acm.graphics.*;


public class Button extends GCompound {

	public Button(double arg0, double arg1, RoomType type) {
		GRoundRect shape = new GRoundRect(arg0, arg1);
		this.type = type;
		shape.setFillColor(type.color());
		shape.setFilled(true);
		add(shape);
		add(new GLabel("ADD " + type.label(), 5, 15), 5, 15);
	}
	
	public
		RoomType type;
	
}

