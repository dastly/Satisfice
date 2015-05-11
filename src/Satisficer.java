
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
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
	double PXL_TO_FT = 25.0/400.0; //Also in Room.java
	
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
		
//		Example photo reading
//		BufferedImage img = null;
//		try {
//			    img = ImageIO.read(new File("Captured.PNG"));
//		} catch (IOException e) {
//		}
//		GImage im = new GImage(img);
//		add(im);		
		
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
	GObject selected = null;
	Vector<Room> selectedRooms = new Vector<Room>();
	GRect groupSelector = null;
	
	double pressX; //Where cursor was pressed
	double pressY;
	
	boolean moving = false;
	boolean resizing = false;
	boolean rotating = false;
	boolean selecting = false;
	
	/*
	 * EventHandler: mouseClicked(...)
	 * ----------------------
	 * Runs action for buttons or DeleteCircles (remove room).
	 */
	
	public void mouseClicked(MouseEvent e){
		if (selected instanceof Button){	//selected set by mousePressed(...).
			pressButton((Button) selected);
		}
		if (selected instanceof Room){
			Room room = (Room) selected;
			GPoint pt = room.getLocalPoint(e.getX(),e.getY());
			GObject selectedInnerObject = room.getElementAt(pt.getX(),pt.getY());
			if (selectedInnerObject instanceof RemoveCircle){
				for(Room sroom: selectedRooms){
					rooms.remove(sroom);
					remove(sroom);
				}
			}
		}
	}
	
	/*
	 * Function: pressButton(Button button)
	 * ----------------------
	 * Creates a room of the type specified by the button, placing it above the button.
	 * Adds room to room list.
	 */
	
	public void pressButton(Button button){
		RoomType type = button.type;
		Room room = new Room(type.width(), type.height(), type);
		room.setLocation(button.getX(),button.getY()-room.getHeight()-ROOM_OFFSET_BOTTOM);
		add(room);
		rooms.add(room);
	}
	
	/*
	 * EventHandler: mousePressed(...)
	 * ----------------------
	 * Records which item is pressed, if it is a Room or Button.
	 * 
	 * Different modes are set.
	 * Room 		-> 	moving
	 * ResizeBlock 	-> 	resizing
	 * RotateDiamond -> rotating 
	 * Outside 		-> selecting
	 * 
	 * Button actions and RemoveCircle action handled by clicks only.
	 */
	public void mousePressed(MouseEvent e){
		
		resizing = false;
		moving = false;
		rotating = false;
		selecting = false;
		pressX = e.getX();
		pressY = e.getY();
		
		selected = getElementAt(e.getX(),e.getY());
		if(selected instanceof Button) return;
		if(selected instanceof Room){
			Room room = (Room) selected;
			GPoint pt = room.getLocalPoint(e.getX(),e.getY());
			GObject selectedInnerObject = room.getElementAt(pt.getX(),pt.getY());
			if(selectedInnerObject instanceof ResizeBlock){
				resizing = true;
			} else if (selectedInnerObject instanceof RotateDiamond){
				rotating = true;
			} else if (selectedInnerObject instanceof GRect) {
				moving = true;
			}// else if (selectedInnerObject instanceof RemoveCircle) return;
			if(!selectedRooms.contains(room)){
				for(Room sroom: selectedRooms){
					sroom.unhighlight();
				}
				selectedRooms.clear();
				selectedRooms.add(room);
				room.highlight();
			}
		} else {
			for(Room sroom: selectedRooms){
				sroom.unhighlight();
			}
			selectedRooms.clear();
			groupSelector = new GRect(e.getX(), e.getY(), 0, 0);
			add(groupSelector);
			selecting = true;
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
		double deltaX = e.getX() - pressX;
		double deltaY = e.getY() - pressY;
		//if(selectedObject == null) return;
		if(moving) {
			for(Room room: selectedRooms){
				room.move(deltaX,deltaY);
			}
		}
		if(resizing) {
			for(Room room: selectedRooms){
				room.resize(deltaX, deltaY);
			}
		}
		// Not working very well (so I made RotateDiamond currently invisible)
		if(rotating){
			for(Room room: selectedRooms){
				room.rotate(Math.tan((deltaX)/(deltaY)));
			}
		}
		if(selecting){
			groupSelector.setSize(groupSelector.getWidth() + deltaX, groupSelector.getHeight() + deltaY);
		}
		pressX = e.getX();
		pressY = e.getY();
//		bar.setSoftSatisfied(TODO);
//		bar.setHardSatisfied(TODO);
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
		
		if(selecting){
			for(Room room: rooms){
				if(groupSelector.getBounds().intersects(room.getBounds())){
					room.highlight();
					selectedRooms.add(room);
				}
			}
			remove(groupSelector);
			groupSelector = null;
		} else {
			if(floor != null){
				if(moving){
					for(Room room: selectedRooms){
						room.setLocation(floor.snapToGrid(room.getLocation()));
					}
				}
				if(resizing){
					GPoint pt = floor.snapToGrid(new GPoint(e.getX(),e.getY()));
					double deltaX = pt.getX() - pressX;
					double deltaY = pt.getY() - pressY;
					for(Room room: selectedRooms){
						room.resize(deltaX, deltaY);
					}
				}
			}
		}
		
//		bar.setSoftSatisfied(TODO);
//		bar.setHardSatisfied(TODO);
	}
	
	//TODO
	double ADJACENCY_THRESHOLD = 20.0;
	public double computeAdjacencyScore(){
		int adjacencies = 0;
		int adjacencyScore = 0;
		for(int i = 0; i < rooms.size(); i++){
			for(int j = i+1; j < rooms.size(); j++){
				if(distance(rooms.elementAt(i),rooms.elementAt(j)) <= ADJACENCY_THRESHOLD){
					adjacencies++;
					adjacencyScore += affinity(rooms.elementAt(i), rooms.elementAt(j));
				}
			}
		}
		return 0.0;
	}
	
	//TODO
	double computeRoomSizeScore(){
		for(Room room: rooms){
			if(room.getSqFootage() < PXL_TO_FT * room.getType().width()*room.getType().height()){
				
			}
		}
		return 0.0;
	}
	
	//TODO
	double computeRoomCountScore(){
		return 0.0;
	}
	
	//TODO	
	private int affinity(Room a, Room b) {
		return 0;
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
