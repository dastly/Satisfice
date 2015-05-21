
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
import java.awt.event.KeyEvent;
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
	int WINDOW_HEIGHT = 700;
	int BAR_X = 100;
	int BAR_Y = 60;
	int VIS_X = WINDOW_WIDTH - 500;
	int VIS_Y = 60;
	int ADD = 0; // add button
	int VIEW = 1; // view button
	int CURRENT = 0;
	int HIGHEST = 1;
	int LABEL_OFFSET_BOTTOM = 25;
	int LABEL_WIDTH = 100;
	int LABEL_HEIGHT = 50;
	int LABEL_SPACING = 10;
	int BUTTON_WIDTH = 46;
	int BUTTON_HEIGHT = 30;
	int BUTTON_SPACING = 9;
	int BUTTON_OFFSET_BOTTOM = 35;
	int BUTTON_OFFSET_LEFT = 300;
	int ROOM_OFFSET_BOTTOM = 10;
	int DESCRIPTION_OFFSET_BOTTOM = 10;
	double PXL_TO_FT = 25.0/400.0; //Also in Room.java and SizeConstraint.java	
	int UP = 0, DOWN = 1; // for scroll buttons
	int SCROLL_RIGHT_OFFSET = 440;
	int SCROLL_BOT_OFFSET = 280;
	int SCROLL_Y = 27;
	
	//Globals
	Vector<Room> rooms = new Vector<Room>();
	Vector<Room> highestRooms = new Vector<Room>();
	Vector<Button> buttons = new Vector<Button>();
	Floorplan floor = null;
	ConstraintBar bar = null;
	Visualiser visualiser = null;
	TaskDescription description = null;
	ScrollButton up = null;
	ScrollButton down = null;
	double highScore = 0;
	StateButton highest = null;
	StateButton current = null;
	
	/*
	 * (non-Javadoc)
	 * @see acm.program.GraphicsProgram#run()
	 * --------------------------------------
	 * Initializes window.  All other functionality handled by mouse/event listeners.
	 */
	public void run() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);	
		
		floor = new Floorplan();
		floor.setLocation((WINDOW_WIDTH-floor.getWidth())/2 - 300, (WINDOW_HEIGHT-floor.getHeight())/2);
		add(floor);

		//Adds button for adding every type of room
	    int i = BUTTON_OFFSET_LEFT, j = BUTTON_OFFSET_LEFT;
	    
	    for(RoomType roomType: RoomType.values()){
			GLabel label = new GLabel(roomType.label());
			label.setLocation(LABEL_SPACING + i, WINDOW_HEIGHT - LABEL_HEIGHT - LABEL_OFFSET_BOTTOM);
			add(label);
			   
			Button addButton = new Button(BUTTON_WIDTH, BUTTON_HEIGHT, roomType, ADD);
			addButton.setLocation(BUTTON_SPACING + j, WINDOW_HEIGHT - BUTTON_HEIGHT - BUTTON_OFFSET_BOTTOM);
			j += BUTTON_WIDTH + BUTTON_SPACING;
			add(addButton);
			   
			Button viewButton = new Button(BUTTON_WIDTH, BUTTON_HEIGHT, roomType, VIEW);
			viewButton.setLocation(BUTTON_SPACING + j, WINDOW_HEIGHT - BUTTON_HEIGHT - BUTTON_OFFSET_BOTTOM);
			j += BUTTON_WIDTH + BUTTON_SPACING;
			add(viewButton);
			   
			i += LABEL_WIDTH + LABEL_SPACING;
			buttons.add(addButton);
			buttons.add(viewButton);
	    }
	    
	    highest = new StateButton("HIGHEST", HIGHEST, Color.GRAY);
	    highest.setLocation(0, 20);
	    add(highest);
	    
	    current = new StateButton("CURRENT", CURRENT, Color.GREEN);
	    current.setLocation(100, 20);
	    add(current);
	    
	    description = new TaskDescription();
	    description.setLocation(BUTTON_SPACING, WINDOW_HEIGHT - DESCRIPTION_OFFSET_BOTTOM);
	    add(description);
	    
		Vector<Flag> flags = new Vector<Flag>();
		Flag adj = new Flag("ADJ", buttons, rooms, FlagType.ADJACENCY, true);
		Flag size = new Flag("SIZE", buttons, rooms, FlagType.SIZE, true);
		Flag count = new Flag("COUNT", buttons, rooms, FlagType.COUNT, true);
		Flag selectedadj = new Flag("SelAdj", buttons, selectedRooms, FlagType.ADJACENCY, false);
		Flag selectedsize = new Flag("SelSize", buttons, selectedRooms, FlagType.SIZE, false);
		flags.add(adj);
		flags.add(size);
		flags.add(count);
		flags.add(selectedadj);
		flags.add(selectedsize);
		
		bar = new ConstraintBar(flags, rooms);
		add(bar);
		bar.setLocation(BAR_X, BAR_Y);
		bar.setFlags();
		
		visualiser = new Visualiser(getSelectedConstraints(), rooms);
		add(visualiser);
		visualiser.setLocation(VIS_X, VIS_Y);
		
		up = new ScrollButton(UP);
		up.setLocation(VIS_X + SCROLL_RIGHT_OFFSET, VIS_Y);
		add(up);
		down = new ScrollButton(DOWN);
		down.setLocation(VIS_X + SCROLL_RIGHT_OFFSET, VIS_Y + SCROLL_BOT_OFFSET);
		add(down);
		
		addMouseListeners();
		addKeyListeners();
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
	boolean shift = false;
	

	private Vector<Constraint> getSelectedConstraints() {
		Vector<Constraint> constraints = new Vector<Constraint>();
		if (selectedRooms.isEmpty()) {
			for (Room room: rooms) {
				constraints.addAll(room.getConstraints());
			}
		} else {
			for (Room room: selectedRooms) {
				constraints.addAll(room.getConstraints());
			}
		}
		return constraints;
	}
	
	/* 
	 * EventHandler: keyPressed(KeyEvent e)
	 * -----------------------
	 * Sets the variable shift to true if the user is pressing the shift key.
	 */
	
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_SHIFT) {
			shift = true;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_SHIFT) {
			shift = false;
		}
	}
	
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
				if (highest.isOn()) {
					rooms = (Vector<Room>) highestRooms.clone();
					highest.setFillColor(Color.GRAY);
					current.setFillColor(Color.GREEN);
				}
				for(Room sroom: selectedRooms){
					rooms.remove(sroom);
					remove(sroom);
				}
				selectedRooms.clear();
			}
		}
		if (selected instanceof StateButton) {
			if (((StateButton)selected).getType() == CURRENT) {
				showRooms(CURRENT);
				highest.setFillColor(Color.GRAY);
				current.setFillColor(Color.GREEN);
			} else if (((StateButton)selected).getType() == HIGHEST) {
				showRooms(HIGHEST);
				highest.setFillColor(Color.GREEN);
				current.setFillColor(Color.GRAY);
			}
		}
		update();
	}
	
	private void showRooms(int state) {
		if (state == CURRENT) {
			for (Room room: highestRooms) {
				remove(room);
			}
			for (Room room: rooms) {
				add(room);
			}
			System.out.println(rooms);
		} else if (state == HIGHEST) {
			for (Room room: rooms) {
				remove(room);
			}
			for (Room room: highestRooms) {
				add(room);
			}
			System.out.println(highestRooms);
		}
	}

	/*
	 * Function: pressButton(Button button)
	 * ----------------------
	 * Creates a room of the type specified by the button, placing it above the button.
	 * Adds room to room list.
	 */
	public void pressButton(Button button){
		RoomType roomType = button.getRoomType();
		int buttonType = button.getButtonType();
		if (buttonType == ADD) {
			Room room = new Room(roomType.width(), roomType.height(), roomType);
			room.setLocation(button.getX(),button.getY()-room.getHeight()-ROOM_OFFSET_BOTTOM);
			add(room);
			if (highest.isOn()) {
				rooms = (Vector<Room>) highestRooms.clone();
				highest.setFillColor(Color.GRAY);
				current.setFillColor(Color.GREEN);
			}
			rooms.add(room);
			update();
		} else if (buttonType == VIEW) {
			description.update(roomType.index());
		}
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
	 * Also allows multiselect/unselect if the shift key is pressed.
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
			} else if (!(selectedInnerObject instanceof RemoveCircle)) {
				moving = true;
			}// else if (selectedInnerObject instanceof RemoveCircle) return;
			if (shift) { // if we're pressing shift while clicking
				if (selectedRooms.contains(room)) {
					// if we're clicking a selected room, we unselect it
					selectedRooms.remove(room);
					room.unhighlight();
				} else {
					// if we're clicking an unselected room, we select it
					selectedRooms.addElement(room);
					room.highlight();
				}
			}
			else if (!selectedRooms.contains(room)){
				for(Room sroom: selectedRooms){
					sroom.unhighlight();
				}
				selectedRooms.clear();
				selectedRooms.add(room);
				room.highlight();
			}
		} else if (selected instanceof ScrollButton) {
			if (((ScrollButton)selected).type() == UP) {
				visualiser.table.shift(-SCROLL_Y);
			} else if (((ScrollButton)selected).type() == DOWN) {
				visualiser.table.shift(SCROLL_Y);
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
				room.saveLocation(room.getLocation());
			}
		}
		if(resizing) {
			for(Room room: selectedRooms){
				room.setSize(room.getWidth() + deltaX, room.getHeight() + deltaY);
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
		update();
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
					for(Room room: selectedRooms){
						GPoint pt = floor.snapToGrid(new GPoint(room.getX()+room.getWidth(), room.getY()+room.getHeight()));
						room.setSize(pt.getX()-room.getX(), pt.getY()-room.getY());
					}
//					GPoint pt = floor.snapToGrid(new GPoint(e.getX(),e.getY()));
//					double deltaX = pt.getX() - pressX;
//					double deltaY = pt.getY() - pressY;
//					for(Room room: selectedRooms){
//						room.resize(deltaX, deltaY);
//					}
				}
			}
		}
		update();
	}
	
	public void update() {
		bar.setFlags();
		visualiser.update(getSelectedConstraints(), rooms);
		if (bar.getScore() > highScore) {
			highScore = bar.getScore();
			highestRooms = (Vector<Room>) rooms.clone();
		}
	}
	
	
//	//TODO
//	double computeRoomSizeScore(){
//		int countScore = 0;
//		int maxScore = 7;
//		for(Room room: rooms){
//			//if(floorPlanContains(room))
//			if(room.getSizeConstraint().satisfied()){
//				countScore++;
//			}
//		}
//		int totalSizeScore = (countScore*100)/maxScore;
//		double hardHeight = totalSizeScore/2.0;
//		return 0.0;
//	}
	
//	private boolean floorPlanContains(Room room){
//		return floor.getBounds().intersects(room.getBounds());
//	}

//	Utils included elsewhere	
	
//	//TODO	
//	private int affinity(Room a, Room b) {
//		return affinityMatrix[a.getType().index()][b.getType().index()];
//	}
//
//	/*
//	 * Function: distance(GPoint a, GPoint b)
//	 * ----------------------
//	 * Distance between points.
//	 */
//	private double distance(GPoint a, GPoint b){
//		return Math.sqrt(Math.pow(a.getX() - b.getX(),2) + Math.pow(a.getY() - b.getY(),2));
//	}
//	
//	/*
//	 * Function: distance(Room a, Room b)
//	 * ----------------------
//	 * Distance between two rooms.
//	 * Equal to length or shortest line between them.
//	 */
//	private double distance(Room a, Room b){
//		double xDiff1 = Math.max(b.getX() - (a.getX() + a.getWidth()),0);
//		double xDiff2 = Math.max(a.getX() - (b.getX() + b.getWidth()),0);
//		double yDiff1 = Math.max(b.getY() - (a.getY() + a.getHeight()),0);
//		double yDiff2 = Math.max(a.getY() - (b.getY() + b.getHeight()),0);
//		return Math.sqrt(Math.pow(xDiff1,2) + Math.pow(xDiff2,2) + Math.pow(yDiff1,2) + Math.pow(yDiff2,2));
//	}
	
	
	
}

//////////OLD JUNK: Flavia


//int totFaculty = 0;
//int totMeeting = 0;
//int totAuditorium = 0;
//int totCafeteria = 0;
//int totSmallC = 0;
//int totLargeC = 0;
//int totMEP = 0;
//int countScore =0;
//int maxScore =14;
//for (Room room: rooms){
//	if (room.getType()==RoomType.FACULTY){
//		totFaculty++;
//	} else if (room.getType()==RoomType.MEETING){
//		totMeeting++;
//	} else if (room.getType()==RoomType.AUDITORIUM){
//		totAuditorium++;
//	} else if (room.getType()==RoomType.CAFETERIA){
//		totCafeteria++;
//	} else if (room.getType()==RoomType.SMALLCLASSROOM){
//		totSmallC++;
//	} else if (room.getType()==RoomType.LARGECLASSROOM){
//		totLargeC++;
//	} else if (room.getType()==RoomType.MEP){
//		totMEP++;
//	}
//}
//// Adding Faculty points
//if (totFaculty>0&&totFaculty<5){
//	countScore++;
//} else if (totFaculty>=5){
//	countScore=countScore+2;
//}
////Adding Meeting points
//if (totMeeting>0&&totMeeting<5){
//	countScore++;
//} else if (totMeeting>=5){
//	countScore=countScore+2;
//}
////Adding Auditorium points
//if (totAuditorium==1){
//	countScore=countScore+2;
//}
////Adding Cafeteria points
//if (totCafeteria==1){
//	countScore=countScore+2;
//}
//// Adding Small Classroom points
//if (totSmallC>0&&totSmallC<5){
//	countScore++;
//} else if (totSmallC>=5){
//	countScore=countScore+2;
//}
//// Adding Large Classroom points
//if (totLargeC>0&&totLargeC<2){
//	countScore++;
//} else if (totLargeC>=2){
//	countScore=countScore+2;
//}
////Adding MEP points
//if (totMEP==1){
//	countScore=countScore+2;
//}
//int totalRoomScore = (countScore*100)/maxScore;
//double hardHeight = totalRoomScore/2.0;
//String labelscore = "Hard Score: " + totalRoomScore +"%";
//
//System.out.println(labelscore);
//bar.setHardSatisfied(hardHeight);
//return 0.0;

//////////OLD JUNK: Dustin


//int adjacencies = 0;
//int adjacencyScore = 0;
//for(int i = 0; i < rooms.size(); i++){
//	for(int j = i+1; j < rooms.size(); j++){
//		if(distance(rooms.elementAt(i),rooms.elementAt(j)) <= ADJACENCY_THRESHOLD){
//			adjacencies++;
//			adjacencyScore += affinity(rooms.elementAt(i), rooms.elementAt(j));
//		}
//	}
//}
//System.out.println(adjacencies);
//if(adjacencies == 0) return 0.5;
//System.out.println("Score: " + ((double)adjacencyScore/(double)adjacencies + 2)/4.0);
//return ((double)adjacencyScore/(double)adjacencies + 2)/4.0;

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

//Example photo reading
//BufferedImage img = null;
//try {
//	    img = ImageIO.read(new File("Captured.PNG"));
//} catch (IOException e) {
//}
//GImage im = new GImage(img);
//add(im);	
