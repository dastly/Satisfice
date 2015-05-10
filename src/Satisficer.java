

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
	
	
	Vector<Room> rooms = new Vector<Room>();
	Floorplan floor = null;
	int WINDOW_WIDTH = 1800;
	int WINDOW_HEIGHT = 600;
	GLabel score = new GLabel("Score: ", 20, 10);
	
	public void run() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		floor = new Floorplan();
		floor.setLocation((WINDOW_WIDTH-floor.getWidth())/2, (WINDOW_HEIGHT-floor.getHeight())/2);
		add(floor);
		
		Room room = new Room(100,100, RoomType.EXAM);
		room.setLocation(20,10);
	    add(room);
	    rooms.add(room);
	    
	    Room room2 = new Room(100,100, RoomType.LAB);
	    room2.setLocation(120,10);
	    add(room2);
	    rooms.add(room2);
	    
	    //I am not sure how to place buttons in the graphics window.  This code makes GIANT buttons :(
	    //You have to resize window for buttons to appear
	    JButton b1 = new JButton("Add lab");
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
	    add(b2);
	    
	    
	    add(score);
		
		//printScore();
		addMouseListeners();
	}
	
	//Adds rooms based on buttons clicked
	public void actionPerformed(ActionEvent e) {
		Room room = null;
	    if ("Add lab".equals(e.getActionCommand())) {
			room = new Room(100, 100, RoomType.LAB);
	    }
	    if ("Add exam".equals(e.getActionCommand())) {
	    	room = new Room(100, 100, RoomType.EXAM);
	 	}
	    room.setLocation(20,10);
	    add(room);
	    rooms.add(room);
	}
	
	//Globals shared between Mouse events
	Room selectedRoom = null;
	GObject selectedObject = null;
	boolean resizing = false;
	double initialX = 0;
	double initialY = 0;
	boolean moving = false;
	boolean rotating = false;
	//boolean rotating = false;
	
	//Records the object selected, and if the resize box is selected
	public void mousePressed(MouseEvent e){
		resizing = false;
		moving = false;
		rotating = false;
		selectedObject = getElementAt(e.getX(),e.getY());
		
		if (selectedObject instanceof Floorplan){
			selectedObject = null;
			return;
		}
		
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
			} else if (selectedInnerObject instanceof RemoveCircle){
				rooms.remove((Room)selectedObject);
				remove(selectedObject);
			} else {
				moving = true;
			}
		}
	}

	//Moves or resizes object
	public void mouseDragged(MouseEvent e){
		if(selectedObject == null) return;
		if(moving) selectedObject.setLocation(e.getX(),e.getY());
		if(resizing) {
			((Room)selectedObject).resize(initialX, initialY, e.getX(),e.getY());
		}
		if(rotating){
			((Room)selectedObject).rotate(Math.tan((e.getX()-initialX)/(e.getY()-initialY)));
		}
	}

	//Snaps object to grid when moved or resized
	public void mouseReleased(MouseEvent e){
		//if(object != null) System.out.println(object.getX() - 10);
		if(floor != null && selectedObject != null && moving) selectedObject.setLocation(floor.snapToGrid(selectedObject.getLocation()));
		if(floor != null && selectedObject != null && resizing){
			GPoint pt = floor.snapToGrid(new GPoint(e.getX(),e.getY()));
			((Room)selectedObject).resize(initialX, initialY, pt.getX(), pt.getY());
		}
		//System.out.println(distance(rooms.elementAt(0),rooms.elementAt(1)));
		//printScore();
	}
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
	
	private double distance(GPoint a, GPoint b){
		return Math.sqrt(Math.pow(a.getX() - b.getX(),2) + Math.pow(a.getY() - b.getY(),2));
	}
	
	//Gives distance between two rooms.  Equals length of shortest line connecting the two rooms.
	private double distance(Room a, Room b){
		double xDiff1 = Math.max(b.getX() - (a.getX() + a.getWidth()),0);
		double xDiff2 = Math.max(a.getX() - (b.getX() + b.getWidth()),0);
		double yDiff1 = Math.max(b.getY() - (a.getY() + a.getHeight()),0);
		double yDiff2 = Math.max(a.getY() - (b.getY() + b.getHeight()),0);
		return Math.sqrt(Math.pow(xDiff1,2) + Math.pow(xDiff2,2) + Math.pow(yDiff1,2) + Math.pow(yDiff2,2));
	}
}
