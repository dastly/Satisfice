
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
	
	int BAR_HEIGHT = 500;
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
		paint(bar);
	}
	public void paint(GRect cBar) {
	  Color color1 = Color.GREEN;
	  Color color2 = Color.RED;
	  int steps = BAR_HEIGHT/10;
	  int stepHeight = 10;
	  
	  for (int i=0; i<steps; i++){
		  float ratio = (float) i / (float) steps;
          int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
          int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
          int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
          Color stepColor = new Color(red, green, blue);
          GRect stepBar = new GRect (0, (i*stepHeight), BAR_WIDTH,stepHeight);
          stepBar.setFillColor(stepColor);
          stepBar.setFilled(true);
          add(stepBar);
	  }
	}

	public void setSoftSatisfied(double satisfied) {
		softSatisfied = satisfied;
		soft.setLocation(-75, BAR_HEIGHT - getSoftSatisfaction());
		soft.setLabel("SOFT: " + satisfied);
	}
	
	public void setHardSatisfied(double satisfied) {
		//hardSatisfied = satisfied;
		//hard.setLocation(BAR_WIDTH+BAR_X+20, getHardSatisfaction());
		hard.setLocation(BAR_WIDTH+20, BAR_HEIGHT-(satisfied*10));
		double totalScore=satisfied*2;
		hard.setLabel("HARD: "+totalScore +"%");
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