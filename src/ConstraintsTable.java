import java.awt.Color;
import java.util.Vector;

import acm.graphics.GCompound;
import acm.graphics.GLabel;


public class ConstraintsTable extends GCompound {
	int INITIAL_Y = 20;
	int INITIAL_X = 120;
	Color color1 = Color.GREEN;
	Color color2 = Color.RED;

	public ConstraintsTable(Vector<Constraint> constraints, Vector<Room> rooms) {
		this.offset_y = 0;
		
		update(constraints, rooms);
	}
	
	
	public void shift(int y) {
		this.offset_y += y;
	}
	
	public void update(Vector<Constraint> constraints, Vector<Room> rooms) {
		removeAll();
		int count = 1;
		for (Constraint constraint: constraints) {
			GLabel label = new GLabel(constraint.getConstraintType());
			label.setLocation(0, offset_y + (INITIAL_Y*count));
			add(label);
			
			GLabel label3 = new GLabel(constraint.getType().toString());
			label3.setLocation(INITIAL_X*2, offset_y + (INITIAL_Y*count));
			add(label3);
			
			updateSatisfaction(constraint, count, rooms);
			count++;
		}
	}
	
	private void updateSatisfaction(Constraint constraint, int count, Vector<Room> rooms) {
		GLabel label;
		if (constraint instanceof AdjacencyConstraint) {
			double satisfactionDouble = ((AdjacencyConstraint)constraint).evaluate(rooms);
			String satisfaction = Double.toString((Math.floor(satisfactionDouble * 100) / 100));
			label = new GLabel(satisfaction);
			label.setLocation(INITIAL_X, offset_y + (INITIAL_Y*count));
			float ratio = (float) 1.0 - (float) satisfactionDouble;
	        int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
	        int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
	        int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
	        Color stepColor = new Color(red, green, blue);
			label.setColor(stepColor);
			add(label);
		} else if (constraint instanceof SizeConstraint) {
			if (((SizeConstraint) constraint).satisfied()) {
				label = new GLabel("Complete");
				label.setColor(Color.GREEN);
			} else {
				label = new GLabel("Incomplete");
				label.setColor(Color.RED);
			}
			label.setLocation(INITIAL_X, offset_y + (INITIAL_Y*count));
			add(label);
		} else if (constraint instanceof CountConstraint) {
			String satisfaction = Double.toString(((CountConstraint)constraint).evaluate(rooms));
			label = new GLabel(satisfaction);
			label.setLocation(INITIAL_X, offset_y + (INITIAL_Y*count));
			add(label);
		} else {
			label = new GLabel("MFiller");
			label.setLocation(INITIAL_X, offset_y + (INITIAL_Y*count));
			add(label);
		}
	}
	
	private
		int offset_y;

}
