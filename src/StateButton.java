import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRoundRect;


public class StateButton extends GCompound {
	int WIDTH = 80;
	int HEIGHT = 20;
	
	public StateButton(String name, int buttonType) {
		this.buttonType = buttonType;
		GRoundRect button = new GRoundRect(WIDTH, HEIGHT);
		button.setFillColor(Color.GREEN);
		button.setFilled(true);
		add(button);
		
		GLabel label = new GLabel(name);
		label.setLocation(10, 15);
		add(label);
	}
	
	public int getType() {
		return buttonType;
	}
	
	private
		int buttonType;
}
