
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
	int FLAG_OFFSET = 10;
	Color color1 = Color.GREEN;
	Color color2 = Color.RED;
	
	GRect bar = null;
	Vector<Flag> flags = null;
	
	public ConstraintBar(Vector<Flag> flags, Vector<Room> rooms) {
		this.rooms = rooms;
		bar = new GRect(BAR_WIDTH, BAR_HEIGHT);
		bar.setFillColor(Color.BLUE);
		add(bar);
		paint(bar);
		
		this.flags = flags;
		for(Flag flag: flags){
			add(flag);
		}
		
	}
	
	public void paint(GRect cBar) {
	  
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
	
	public void setFlags(StateType state){
		for(Flag flag: flags){
			double satisfaction = flag.satisfaction(rooms);
			
			int red = (int) (color2.getRed() * (1 - satisfaction) + color1.getRed() * satisfaction);
	        int green = (int) (color2.getGreen() * (1 - satisfaction) + color1.getGreen() * satisfaction);
	        int blue = (int) (color2.getBlue() * (1 - satisfaction) + color1.getBlue() * satisfaction);
	        Color stepColor = new Color(red, green, blue);
	        flag.setColor(stepColor);
	        
			flag.setLabel(Math.floor(satisfaction * 100) / 100);
			double x = (flag.onLeft()) ? - FLAG_OFFSET - flag.getWidth() : BAR_WIDTH + FLAG_OFFSET;
			flag.setLocation(x, BAR_HEIGHT * (1 - satisfaction));
		}
		adjustOverlaps();
		setScore(state);
	}
	
	public double getScore(StateType state) {
		if (state == StateType.CURRENT) {
			return currentScore;
		} else {
			return highestScore;
		}
	}
	
	private void setScore(StateType state) {
		double satisfaction = 0;
		for (Flag flag: flags) {
			if (flag.getName() == "ADJ" || flag.getName() == "COUNT" || flag.getName() == "SIZE") {
				satisfaction += flag.satisfaction(rooms);
			}
		}
		if (state == StateType.CURRENT) {
			this.currentScore = satisfaction;
		} else if (state == StateType.HIGHEST) {
			this.highestScore = satisfaction;
		}
	}
	
	private void adjustOverlaps(){
		for(Flag flag: flags){
			while(overlapsFlag(flag) != null){
				Flag otherFlag = overlapsFlag(flag);
				double satisfaction = flag.satisfaction(rooms);
				double satisfactionOther = otherFlag.satisfaction(rooms);
				if(satisfaction <= satisfactionOther){
					flag.move(0, heightDiff(flag, otherFlag) + 1);
				} else {
					otherFlag.move(0, heightDiff(flag, otherFlag) + 1);
				}
			}
		}
	}
	
	private double heightDiff(Flag flag, Flag otherFlag){
		if(flag.equals(otherFlag) || !flag.getBounds().intersects(otherFlag.getBounds())) return 0.0;
		return Math.abs(flag.getY()-flag.getY());
	}
	
	private Flag overlapsFlag(Flag flag){
		for(Flag otherFlag: flags){
			if(!flag.equals(otherFlag) && flag.getBounds().intersects(otherFlag.getBounds())){
					return otherFlag;
			}
		}
		return null;
	}
	
	private
		Vector<Room> rooms;
		double currentScore;
		double highestScore;
	
	
//	private void setSoftFlag(SoftFlag flag) {
//	double satisfaction = flag.satisfaction(allRooms);
//	flag.setLabel(Math.floor(satisfaction * 100) / 100);
//	flag.setLocation(-FLAG_OFFSET - flag.getWidth(), BAR_HEIGHT*(1 - satisfaction));
//}
//
//private void setHardFlag(HardFlag flag) {
//	double satisfaction = flag.satisfaction();
//	flag.setLabel(Math.floor(satisfaction * 100) / 100);
//	flag.setLocation(BAR_WIDTH + FLAG_OFFSET, BAR_HEIGHT*(1 - satisfaction));
//}
	
	
	
//	OLD JUNK
	
//	public void setSoftHeight(double satisfied) {
//		softSatisfied = satisfied;
//		soft.setLocation(-75, BAR_HEIGHT - getSoftSatisfaction());
//		soft.setLabel("SOFT: " + satisfied);
//	}
	
//	public void setHardHeight(double satisfied) {
//		//hardSatisfied = satisfied;
//		//hard.setLocation(BAR_WIDTH+BAR_X+20, getHardSatisfaction());
//		hard.setLocation(BAR_WIDTH+20, BAR_HEIGHT-(satisfied*10));
//		double totalScore=satisfied*2;
//		hard.setLabel("HARD: "+totalScore +"%");
//	}
	
//	private double getSoftSatisfaction() {
//		double softSatisfaction = softSatisfied/totalSoftConstraints;
//		double softLabelPosition = softSatisfaction * BAR_HEIGHT;
//		return softLabelPosition;
//	}
	
//	private double getHardSatisfaction() {
//		double hardSatisfaction = hardSatisfied/totalHardConstraints;
//		double hardLabelPosition = hardSatisfaction * BAR_HEIGHT;
//		return hardLabelPosition;
//	}
}