import acm.graphics.GCompound;
import acm.graphics.GLabel;

import java.awt.Color;
import java.util.Vector;


public class Visualiser extends GCompound {
	int OFFSET_Y = 20;
	int OFFSET_X = 120;
	int MATRIX_Y = 400;
	int MATRIX_X = 60;
	
	int affinityMatrix[][] = { //also in Adjacency Constraint
		      { 1, -2, -2,  1, -2, -2, -1},
		      {-2,  1, -1,  1,  2,  2,  0},
		      {-2,  1,  1,  2,  2,  2,  0},
		      { 1,  1,  2,  1,  2,  2,  0},
		      {-2,  2,  2,  2,  1,  2,  0},
		      {-2,  2,  2,  2,  2,  1,  0},
		      {-1, -1,  0,  0,  0,  0,  1}
	};
	
	String instructions[][] = {
			{"Room Type", "Min Size", "#Rooms"},
			{"FACULTY", "180", "5 or more"},
			{"MEETING", "200", "5 or more"},
			{"AUD", "2500", "1"},
			{"CAFE", "700", "1"},
			{"SmallC", "500", "5 or more"},
			{"LargeC", "800", "2 or more"},
			{"MEP", "750", "1"}
	};
	
	String affinityLabels[] = {"FACULTY", "MEETING", "AUD", "CAFE", "SmallC", "LargeC", "MEP"};
	
	public Visualiser(Vector<Constraint> constraints, Vector<Room> rooms) {
		showAffinityMatrix();
		update(constraints, rooms);
	}
	
	public void update(Vector<Constraint> constraints, Vector<Room> rooms) {
		updateHeaders();
		showAffinityMatrix();
		int count = 1;
		for (Constraint constraint: constraints) {
			GLabel label = new GLabel(constraint.getConstraintType());
			label.setLocation(0, OFFSET_Y*count);
			add(label);
			
			GLabel label3 = new GLabel(constraint.getType().toString());
			label3.setLocation(OFFSET_X*2, OFFSET_Y*count);
			add(label3);
			
			updateSatisfaction(constraint, count, rooms);
			count++;
		}
	}
	
	private void showAffinityMatrix() {
		GLabel label, label2;
		label = new GLabel("AFFINITY MATRIX: used to calculate a weighted score for adjacency constraints.");
		label.setLocation(-MATRIX_X, MATRIX_Y-OFFSET_Y);
		add(label);
		for (int i=0; i<affinityMatrix.length; i++) {
			label = new GLabel(affinityLabels[i]);
			label.setLocation(MATRIX_X*i, MATRIX_Y);
			add(label);
			label2 = new GLabel(affinityLabels[i]);
			label2.setLocation(-MATRIX_X, MATRIX_Y + (OFFSET_Y*(i+1)));
			add(label2);
			for (int j=0; j<affinityMatrix.length; j++) {
				label = new GLabel(Integer.toString(affinityMatrix[i][j]));
				label.setLocation(MATRIX_X*j, MATRIX_Y + (OFFSET_Y*(i+1)));
				add(label);
			}
		}
	}
	
	private void updateSatisfaction(Constraint constraint, int count, Vector<Room> rooms) {
		GLabel label;
		if (constraint instanceof AdjacencyConstraint) {
			String satisfaction = Double.toString(((AdjacencyConstraint)constraint).evaluate(rooms));
			label = new GLabel(satisfaction);
			label.setLocation(OFFSET_X, OFFSET_Y*count);
			add(label);
		} else if (constraint instanceof SizeConstraint) {
			if (((SizeConstraint) constraint).satisfied()) {
				label = new GLabel("Complete");
				label.setColor(Color.GREEN);
			} else {
				label = new GLabel("Incomplete");
				label.setColor(Color.RED);
			}
			label.setLocation(OFFSET_X, OFFSET_Y*count);
			add(label);
		} else if (constraint instanceof CountConstraint) {
			String satisfaction = Double.toString(((CountConstraint)constraint).evaluate(rooms));
			label = new GLabel(satisfaction);
			label.setLocation(OFFSET_X, OFFSET_Y*count);
			add(label);
		} else {
			label = new GLabel("MFiller");
			label.setLocation(OFFSET_X, OFFSET_Y*count);
			add(label);
		}
	}
	
	private void updateHeaders() {
		removeAll();
		GLabel label = new GLabel("Constraint Type");
		label.setLocation(0, 0);
		add(label);
		GLabel label2 = new GLabel("Room Type");
		label2.setLocation(OFFSET_X*2, 0);
		add(label2);
		GLabel label3 = new GLabel("Satisfied?");
		label3.setLocation(OFFSET_X, 0);
		add(label3);
	}
}
