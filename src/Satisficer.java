
/*
 * File: Satisficer.java
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

public class Satisficer extends GraphicsProgram {
	
	//Constants
	int WINDOW_WIDTH = 1800;
	int WINDOW_HEIGHT = 600;
	int BUTTON_OFFSET_BOTTOM = 25;
	int BUTTON_WIDTH = 100;
	int BUTTON_HEIGHT = 50;
	int BUTTON_SPACING = 10;
	int ROOM_OFFSET_BOTTOM = 10;
	
	//Globals
	Vector<Room> rooms = new Vector<Room>();
	Floorplan floor = null;
	ConstraintBar bar = null;
	GLabel score = null;
	
	/*
	 * (non-Javadoc)
	 * @see acm.program.GraphicsProgram#run()
	 * --------------------------------------
	 * Initializes window.  All other functionality handled by mouse/event listeners.
	 */
	public void run() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		floor = new Floorplan();
		floor.setLocation((WINDOW_WIDTH-floor.getWidth())/2, (WINDOW_HEIGHT-floor.getHeight())/2);
		add(floor);
		
		bar = new ConstraintBar(10, 5); // ConstraintBar takes in #total soft constraints, #total hard constraints
		add(bar);
		
	    
		//Adds button for adding every type of room
	    int i = 0;
	    for(RoomType type: RoomType.values()){
		   Button b1 = new Button(BUTTON_WIDTH, BUTTON_HEIGHT, type);
		    b1.setLocation(BUTTON_SPACING + i, WINDOW_HEIGHT - BUTTON_HEIGHT - BUTTON_OFFSET_BOTTOM);
		    add(b1);
		    i += BUTTON_WIDTH + BUTTON_SPACING;
	    }
	    
	    score = new GLabel("Score: ", 20, 10);
	    add(score);
		
		//printScore();
		addMouseListeners();
	}
	
	//Globals shared between Mouse events
	Room selectedRoom = null;
	GObject selectedObject = null;
	
	double initialX; //Initial location or room
	double initialY;
	double pressX; //Where cursor was pressed
	double pressY;
	
	boolean moving = false;
	boolean resizing = false;
	boolean rotating = false;
	
	/*
	 * EventHandler: mouseClicked(...)
	 * ----------------------
	 * Runs action for buttons or DeleteCircles (remove room).
	 */
	
	public void mouseClicked(MouseEvent e){
		if (selectedObject instanceof Button){	//selectedObject set by mousePressed(...).
			pressButton();
		}
		if (selectedObject instanceof Room){
			Room room = (Room) selectedObject;
			GPoint pt = room.getLocalPoint(e.getX(),e.getY());
			GObject selectedInnerObject = room.getElementAt(pt.getX(),pt.getY());
			if (selectedInnerObject instanceof RemoveCircle){
				rooms.remove((Room)selectedObject);
				remove(selectedObject);
			}
		}
	}
	
	/*
	 * Function: pressButton()
	 * ----------------------
	 * Creates a room of the type specified by the button, placing it above the button.
	 * Adds room to room list.
	 */
	
	public void pressButton(){
		Room room = new Room(((Button) selectedObject).type);
		room.setLocation(selectedObject.getX(),selectedObject.getY()-room.getHeight()-ROOM_OFFSET_BOTTOM);
		add(room);
		rooms.add(room);
	}
	
	/*
	 * EventHandler: mousePressed(...)
	 * ----------------------
	 * Records which item is pressed, if it is a Room or Button.
	 * 
	 * Different modes are set for different parts of a Room pressed.
	 * Room 		-> 	moving
	 * ResizeBlock 	-> 	resizing
	 * RotateDiamond -> rotating 
	 * 
	 * Button actions and RemoveCircle action handled by clicks only.
	 */
	public void mousePressed(MouseEvent e){
		resizing = false;
		moving = false;
		rotating = false;
		selectedObject = getElementAt(e.getX(),e.getY());
		
		if (selectedObject instanceof Room){
			Room room = (Room) selectedObject;
			GPoint pt = room.getLocalPoint(e.getX(),e.getY());
			GObject selectedInnerObject = room.getElementAt(pt.getX(),pt.getY());
			if(selectedInnerObject instanceof ResizeBlock){
				resizing = true;
				initialX = room.getX();
				initialY = room.getY();
			} else if (selectedInnerObject instanceof RotateDiamond){
				rotating = true;
				initialX = room.getX();
				initialY = room.getY();
			} else if (selectedInnerObject instanceof GRect) {
				moving = true;
				pressX = e.getX();
				pressY = e.getY();
			}
		} else if (!(selectedObject instanceof Button)){
			selectedObject = null;
		}
	}

	/*
	 * EventHandler: mouseDragged(...)
	 * ----------------------
	 * Moves, resizes, or rotates, based on mode set.
	 * 
	 * Should also update visualization.
	 */
	public void mouseDragged(MouseEvent e){
		if(selectedObject == null) return;
		if(moving) {
			selectedObject.move(e.getX()-pressX,e.getY()-pressY);
			pressX = e.getX();
			pressY = e.getY();
		}
		if(resizing) {
			((Room)selectedObject).resize(initialX, initialY, e.getX(),e.getY());
		}
		// Not working very well (so I made RotateDiamond currently invisible)
		if(rotating){
			((Room)selectedObject).rotate(Math.tan((e.getX()-initialX)/(e.getY()-initialY)));
		}
	}

	/*
	 * EventHandler: mouseReleased(...)
	 * ----------------------
	 * Snaps room to grid after resizing or moving.
	 * 
	 * Should also update visualization.
	 */
	public void mouseReleased(MouseEvent e){
		//if(object != null) System.out.println(object.getX() - 10);
		if(floor != null && selectedObject != null && moving) selectedObject.setLocation(floor.snapToGrid(selectedObject.getLocation()));
		if(floor != null && selectedObject != null && resizing){
			GPoint pt = floor.snapToGrid(new GPoint(e.getX(),e.getY()));
			((Room)selectedObject).resize(initialX, initialY, pt.getX(), pt.getY());
		}
	}
	
	/*
	 * Function: distance(GPoint a, GPoint b)
	 * ----------------------
	 * Distance between points.
	 */
	private double distance(GPoint a, GPoint b){
		return Math.sqrt(Math.pow(a.getX() - b.getX(),2) + Math.pow(a.getY() - b.getY(),2));
	}
	
	/*
	 * Function: distance(Room a, Room b)
	 * ----------------------
	 * Distance between two rooms.
	 * Equal to length or shortest line between them.
	 */
	private double distance(Room a, Room b){
		double xDiff1 = Math.max(b.getX() - (a.getX() + a.getWidth()),0);
		double xDiff2 = Math.max(a.getX() - (b.getX() + b.getWidth()),0);
		double yDiff1 = Math.max(b.getY() - (a.getY() + a.getHeight()),0);
		double yDiff2 = Math.max(a.getY() - (b.getY() + b.getHeight()),0);
		return Math.sqrt(Math.pow(xDiff1,2) + Math.pow(xDiff2,2) + Math.pow(yDiff1,2) + Math.pow(yDiff2,2));
	}
}

//////////OLD JUNK

/*
	// Dustin: Just testing a sample constraint
	private void printScore(){
		String scoreString =  "Score: ";
		int scoreNum = 0;
		if(distance(exec.getLocation(), storage.getLocation()) < 150) scoreNum += -100;
		System.out.println(distance(exec.getLocation(), storage.getLocation()));
		scoreString += scoreNum;
		score.setLabel(scoreString);
	}*/

//I am not sure how to place buttons in the graphics window.  This code makes GIANT buttons :(
//You have to resize window for buttons to appear
/*JButton b1 = new JButton("Add lab");
b1.setVerticalTextPosition(AbstractButton.CENTER);
b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
//b1.setMnemonic(KeyEvent.VK_D);
b1.setActionCommand("Add lab");
b1.addActionListener(this);
add(b1);

JButton b2 = new JButton("Add exam");
b2.setVerticalTextPosition(AbstractButton.CENTER);
b2.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
//b2.setMnemonic(KeyEvent.VK_D);
b2.setActionCommand("Add exam");
b2.addActionListener(this);
add(b2);*/

//Adds rooms based on buttons clicked
//public void actionPerformed(ActionEvent e) {
//	Room room = null;
//    if ("Add lab".equals(e.getActionCommand())) {
//		room = new Room(100, 100, RoomType.LAB);
//    }
//    if ("Add exam".equals(e.getActionCommand())) {
//    	room = new Room(100, 100, RoomType.EXAM);
// 	}
//    room.setLocation(20,10);
//    add(room);
//    rooms.add(room);
//}
