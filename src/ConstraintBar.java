
/*
 * File: ConstraintBar.java
 * ---------------------
 * Sets up graphic applet and runs main program.
 */

import acm.graphics.*;
import acm.gui.HPanel;
import acm.program.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ConstraintBar extends GCompound {
	
	int BAR_HEIGHT = 400;
	int BAR_WIDTH = 60;
	
	
	GRect bar = null;
	GLabel soft = null;
	GLabel hard = null;
	
	double totalSoftConstraints = 0;
	double totalHardConstraints = 0;
	double softSatisfied = 0;
	double hardSatisfied = 0;
	
	public ConstraintBar(double totalSoft, double totalHard) {
		bar = new GRect(BAR_WIDTH, BAR_HEIGHT);
		bar.setFillColor(Color.BLUE);
		soft = new GLabel("SOFT", -50, BAR_HEIGHT);
		hard = new GLabel("HARD", BAR_WIDTH+20, BAR_HEIGHT);
		totalSoftConstraints = totalSoft;
		totalHardConstraints = totalHard;
		
		add(bar);
		add(soft);
		add(hard);
	}
	
	public void setSoftSatisfied(double satisfied) {
		softSatisfied = satisfied;
		soft.setLocation(-50, BAR_HEIGHT - getSoftSatisfaction());
	}
	
	public void setHardSatisfied(double satisfied) {
		hardSatisfied = satisfied;
		hard.setLocation(BAR_WIDTH+20, getHardSatisfaction());
	}
	
	private double getSoftSatisfaction() {
		double softSatisfaction = softSatisfied/totalSoftConstraints;
		double softLabelPosition = softSatisfaction * BAR_HEIGHT;
		return softLabelPosition;
	}
	
	private double getHardSatisfaction() {
		double hardSatisfaction = hardSatisfied/totalHardConstraints;
		double hardLabelPosition = hardSatisfaction * BAR_HEIGHT;
		return hardLabelPosition;
	}
}