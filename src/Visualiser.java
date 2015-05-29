import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRect;

import java.awt.event.MouseEvent;

import java.awt.Color;
import java.util.Vector;


public class Visualiser extends GCompound {
	int OFFSET_Y = 20;
	int OFFSET_X = 120;
	int MATRIX_Y = 400;
	int MATRIX_X = 60;
	
	int TOP_OFFSET = 96;
	int BOTTOM_OFFSET = 330;
	
	int UP = 0, DOWN = 1; // for scroll buttons
	
	int affinityMatrix[][] = { //also in Adjacency Constraint
		      { 1, 0, -2,  1, -2, -2, -1},
		      { 0,  1, -1,  1,  2,  2,  0},
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
	
	ConstraintsTable table = null;
	
	public Visualiser(Vector<Constraint> constraints, Vector<Room> rooms, boolean TREATMENT, boolean AFFINITY_MATRIX) {
		if(AFFINITY_MATRIX) showAffinityMatrix();
		table = new ConstraintsTable(constraints, rooms);
		add(table);
		update(constraints, rooms);
	}
	
	private void setupScrollbar() {
		GRect top = new GRect(600, 100);
		GRect bot = new GRect(600, 600);
		
		top.setFillColor(Color.WHITE);
		top.setColor(Color.WHITE);
		top.setFilled(true);
		top.setLocation(0, -TOP_OFFSET);
		add(top);
		
		bot.setFillColor(Color.WHITE);
		bot.setColor(Color.WHITE);
		bot.setFilled(true);
		bot.setLocation(0, BOTTOM_OFFSET);
		add(bot);
	}
	
	public void update(Vector<Constraint> constraints, Vector<Room> rooms) {
		if(table == null) return;
		removeAll();
		add(table);
		table.update(constraints, rooms);
		setupScrollbar();
		updateHeaders();
		showAffinityMatrix();
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
	
	private void updateHeaders() {
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
