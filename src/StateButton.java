import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRoundRect;


public class StateButton extends GCompound {
	int WIDTH = 100;
	int HEIGHT = 20;
	
	public StateButton(String name, StateType buttonType, Color color) {
		this.buttonType = buttonType;
		this.button = new GRoundRect(WIDTH, HEIGHT);
		button.setFillColor(color);
		button.setFilled(true);
		add(button);
		
		GLabel label = new GLabel(name);
		label.setLocation(10, 15);
		add(label);
	}
	
	public StateType getType() {
		return buttonType;
	}
	
	public void setFillColor(Color color) {
		this.color = color;
		button.setFillColor(color);
	}
	
	public boolean isOn() {
		if (color == Color.GREEN) return true;
		return false;
	}
	
	private
		StateType buttonType;
		GRoundRect button;
		Color color;
}
