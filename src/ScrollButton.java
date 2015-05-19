import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GPolygon;

public class ScrollButton extends GCompound {
	int WIDTH = 20;
	int HEIGHT = 20;
	
	int UP = 0;
	int DOWN = 1;
	
	public ScrollButton(int buttonType) {
		this.buttonType = buttonType;
		GPolygon button = new GPolygon();

		if (buttonType == UP) {
			button.addEdge(10, 0);
			button.addEdge(-10, 10);
			button.addEdge(20, 0);
		} else if (buttonType == DOWN) {
			button.addEdge(10, 0);
			button.addEdge(10, -10);
			button.addEdge(-20, 0);
		}
		button.setFillColor(Color.BLUE);
		button.setFilled(true);
		add(button);
	}
	
	public int type() {
		return buttonType;
	}
	
	private
		int buttonType;
}
