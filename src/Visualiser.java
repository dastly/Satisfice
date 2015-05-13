import acm.graphics.GCompound;
import acm.graphics.GLabel;

import java.awt.Color;
import java.util.Vector;


public class Visualiser extends GCompound {
	int OFFSET_Y = 20;
	int OFFSET_X = 120;
	
	public Visualiser(Vector<Constraint> constraints, Vector<Room> rooms) {
		if (!constraints.isEmpty()) update(constraints, rooms);
	}
	
	public void update(Vector<Constraint> constraints, Vector<Room> rooms) {
		removeAll();
		GLabel label = new GLabel("Constraint Type");
		label.setLocation(0, 0);
		add(label);
		GLabel label3 = new GLabel("Room Type");
		label3.setLocation(OFFSET_X*2, 0);
		add(label3);
		GLabel label2 = new GLabel("Satisfied?");
		label2.setLocation(OFFSET_X, 0);
		add(label2);
		
		int count = 1;
		for (Constraint constraint: constraints) {
			label = new GLabel(constraint.getConstraintType());
			label.setLocation(0, OFFSET_Y*count);
			add(label);
			
			label3 = new GLabel(constraint.getType().toString());
			label3.setLocation(OFFSET_X*2, OFFSET_Y*count);
			add(label3);
			
			if (constraint instanceof AdjacencyConstraint) {
				label2 = new GLabel("TODO");
				label2.setLocation(OFFSET_X, OFFSET_Y*count);
				add(label2);
			} else if (constraint instanceof SizeConstraint) {
				if (((SizeConstraint) constraint).satisfied()) {
					label2 = new GLabel("Complete");
					label2.setColor(Color.GREEN);
				} else {
					label2 = new GLabel("Incomplete");
					label2.setColor(Color.RED);
				}
				label2.setLocation(OFFSET_X, OFFSET_Y*count);
				add(label2);
			} else if (constraint instanceof CountConstraint) {
				String satisfaction = Double.toString(((CountConstraint)constraint).evaluate(rooms));
				label2 = new GLabel(satisfaction);
				label2.setLocation(OFFSET_X, OFFSET_Y*count);
				add(label2);
			} else {
				label2 = new GLabel("MFiller");
				label2.setLocation(OFFSET_X, OFFSET_Y*count);
				add(label2);
			}
			count++;
		}
	}
}
