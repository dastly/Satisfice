
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
	
	//Settings
	boolean TREATMENT = false; //Just change this between treatment and control
	boolean AFFINITY_MATRIX = true; //Will only show or not in treatment condition
	boolean SELECTED_FLAGS = true;
	boolean ADDITIONAL_HIGH_SCORES = true;
	boolean VERBOSE = true; //change if you want
	boolean RED_FOR_OVERLAP = true; //change if you want
	boolean DO_NOT_SAVE_PLANS_WITH_OVERLAPS = true; 
	boolean DO_NOT_SAVE_PLANS_OUTSIDE_BOUNDS = true;
	boolean DO_NOT_SAVE_PLANS_OUTSIDE_GRAY_BOUNDS = true;
	
	//Constants
	int WINDOW_WIDTH = 1800;
	int WINDOW_HEIGHT = 700;
	int BAR_X = 100;
	int BAR_Y = 60;
	int VIS_X = WINDOW_WIDTH - 500;
	int VIS_Y = 60;
	int ADD = 0; // add button
	int VIEW = 1; // view button
	int LABEL_OFFSET_BOTTOM = 25;
	int LABEL_WIDTH = 100;
	int LABEL_HEIGHT = 50;
	int LABEL_SPACING = 10;
	int BUTTON_WIDTH = 46;
	int BUTTON_HEIGHT = 30;
	int BUTTON_SPACING = 9;
	int BUTTON_OFFSET_BOTTOM = 30;
	int BUTTON_OFFSET_LEFT = 300;
	int ROOM_OFFSET_BOTTOM = 10;
	int DESCRIPTION_OFFSET_BOTTOM = 10;
	double PXL_TO_FT = 25.0/400.0; //Also in Room.java and SizeConstraint.java	
	int UP = 0, DOWN = 1; // for scroll buttons
	int SCROLL_RIGHT_OFFSET = 440;
	int SCROLL_BOT_OFFSET = 280;
	int SCROLL_Y = 27;
	
	//Globals
	Vector<Room> currentRooms = new Vector<Room>();
	Vector<Room> highestRooms = new Vector<Room>();
	Vector<Room> highestAdjacencyRooms = new Vector<Room>();
	Vector<Room> highestSizeRooms = new Vector<Room>();
	Vector<Room> highestCountRooms = new Vector<Room>();
	Vector<Room> selectedRooms = new Vector<Room>();
	
	Vector<Button> buttons = new Vector<Button>();
	double highScore = 0;
	double highAdjacencyScore = 0;
	double highSizeScore = 0;
	double highCountScore = 0;
	StateButton current = null;
	StateButton highest = null;
	StateButton highestAdjacency = null;
	StateButton highestSize = null;
	StateButton highestCount = null;
	Vector<StateButton> stateButtons = new Vector<StateButton>();
	StateType state = null;
	
	Floorplan floor = null;
	ConstraintBar bar = null;
	Visualiser visualiser = null;
	TaskDescription description = null;
	ScrollButton up = null;
	ScrollButton down = null;
	
	
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
	    
	    current = new StateButton("CURRENT", StateType.CURRENT, Color.GREEN);
	    current.setLocation(20, 20);
	    if(TREATMENT) add(current);
	    state = StateType.CURRENT;
	    
	    highest = new StateButton("HIGHEST", StateType.HIGHEST, Color.GRAY);
		highest.setLocation(120, 20);
		if(TREATMENT) add(highest);
		    
		highestAdjacency = new StateButton("HIGHEST ADJ", StateType.HIGHEST_ADJACENCY, Color.GRAY);
		highestAdjacency.setLocation(220, 20);
		if(TREATMENT && ADDITIONAL_HIGH_SCORES) add(highestAdjacency);
	    
	    highestSize = new StateButton("HIGHEST SIZE", StateType.HIGHEST_SIZE, Color.GRAY);
	    highestSize.setLocation(320, 20);
		if(TREATMENT && ADDITIONAL_HIGH_SCORES) add(highestSize);
	    
	    highestCount = new StateButton("HIGHEST COUNT", StateType.HIGHEST_COUNT, Color.GRAY);
	    highestCount.setLocation(420, 20);
		if(TREATMENT && ADDITIONAL_HIGH_SCORES) add(highestCount);
		
		stateButtons.add(current);
		stateButtons.add(highest);
		stateButtons.add(highestAdjacency);
		stateButtons.add(highestCount);
		stateButtons.add(highestSize);
	    
	    description = new TaskDescription();
	    description.setLocation(BUTTON_SPACING, WINDOW_HEIGHT - DESCRIPTION_OFFSET_BOTTOM);
	    if(TREATMENT) add(description);
	    
		Vector<Flag> flags = new Vector<Flag>();
		Flag adj = new Flag("ADJ", FlagType.ADJACENCY, true);
		Flag size = new Flag("SIZE", FlagType.SIZE, true);
		Flag count = new Flag("COUNT", FlagType.COUNT, true);
		Flag selectedadj = new Flag("ADJ (Selected)", FlagType.SELADJACENCY, false);
		Flag selectedsize = new Flag("SIZE (Selected)", FlagType.SELSIZE, false);
		flags.add(adj);
		flags.add(size);
		flags.add(count);
		if(SELECTED_FLAGS) flags.add(selectedadj);
		if(SELECTED_FLAGS) flags.add(selectedsize);
		
		bar = new ConstraintBar(flags);
		if(TREATMENT) add(bar);
		bar.setLocation(BAR_X, BAR_Y);
		bar.setFlags(buttons, currentRooms, selectedRooms);
		
		visualiser = new Visualiser(getSelectedConstraints(currentRooms), currentRooms, TREATMENT, AFFINITY_MATRIX);
		if(TREATMENT) add(visualiser);
		visualiser.setLocation(VIS_X, VIS_Y);
		
		up = new ScrollButton(UP);
		up.setLocation(VIS_X + SCROLL_RIGHT_OFFSET, VIS_Y);
		if(TREATMENT) add(up);
		down = new ScrollButton(DOWN);
		down.setLocation(VIS_X + SCROLL_RIGHT_OFFSET, VIS_Y + SCROLL_BOT_OFFSET);
		if(TREATMENT) add(down);
		
		addMouseListeners();
		addKeyListeners();
	}
	
	//Globals shared between Mouse events
	GObject selected = null;
	GRect groupSelector = null;
	
	double pressX; //Where cursor was pressed
	double pressY;
	
	boolean moving = false;
	boolean resizing = false;
	boolean rotating = false;
	boolean selecting = false;
	boolean shift = false;
	
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
		if (code == KeyEvent.VK_0){
			if(TREATMENT) TREATMENT = false;
			else TREATMENT = true;
			showRooms(state, state);
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
				if (state != StateType.CURRENT) {
					switchToCurrent();
				}
				for(Room sroom: selectedRooms){
					currentRooms.remove(sroom);
					remove(sroom);
				}
				selectedRooms.clear();
			}
		}
		if (selected instanceof StateButton) {
			StateType stateType = ((StateButton)selected).getType();
			for(StateButton stateButton: stateButtons){
				if(stateButton.getType() == stateType){
					stateButton.setFillColor(Color.GREEN);
				} else {
					stateButton.setFillColor(Color.GRAY);
				}
			}
			showRooms(state, stateType);
			state = stateType;
		}
		update();
	}
	
	private void showRooms(StateType previousStateType, StateType currentStateType) {
		removeAll();
		add(floor);
		for(Button button: buttons){
			add(button);
		}
		if(TREATMENT){
			add(description);
			add(visualiser);
			add(up);
			add(down);
			add(bar);
			for(StateButton stateButton: stateButtons){
				add(stateButton);
			}
		}
		
//		for(Room room : roomsSwitch(previousStateType)){
//			remove(room);
//		}
		
		for(Room room : roomsSwitch(currentStateType)){
			add(room);
		}
		if(VERBOSE) System.out.println("Room switched on display");
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
			if (state != StateType.CURRENT) {
				switchToCurrent();
			}
			currentRooms.add(room);
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
			selected.sendToFront();
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
//					room.unhighlight();
				} else {
					// if we're clicking an unselected room, we select it
					selectedRooms.addElement(room);
//					room.highlight();
				}
			}
			else if (!selectedRooms.contains(room)){
//				for(Room sroom: selectedRooms){
//					sroom.unhighlight();
//				}
				selectedRooms.clear();
				selectedRooms.add(room);
//				room.highlight();
			}
		} else if (selected instanceof ScrollButton) {
			if (((ScrollButton)selected).type() == UP) {
				visualiser.table.shift(-SCROLL_Y);
			} else if (((ScrollButton)selected).type() == DOWN) {
				visualiser.table.shift(SCROLL_Y);
			}
		} else {
//			for(Room sroom: selectedRooms){
//				sroom.unhighlight();
//			}
			selectedRooms.clear();
			groupSelector = new GRect(e.getX(), e.getY(), 0, 0);
			add(groupSelector);
			selecting = true;
		}
		resetHighlights();
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
		if(state != StateType.CURRENT) switchToCurrent();
		if(selecting){
			groupSelector.setSize(groupSelector.getWidth() + deltaX, groupSelector.getHeight() + deltaY);
		} 
		if(moving) {
			for(Room room: selectedRooms){
				room.move(deltaX,deltaY);
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
		resetHighlights();
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
		if(selecting){
			Vector<Room> rooms = roomsSwitch(state);//(state == StateType.CURRENT) ? currentRooms : highestRooms;
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
		resetHighlights();
		update();
	}
	
	private void resetHighlights(){
		for(Room room: roomsSwitch(state)){
			for(Room room2: roomsSwitch(state)){
				if(!room.equals(room2)){
					if(RED_FOR_OVERLAP && overlap(room, room2)) {
						room.markRed();
					} else if(selectedRooms.contains(room)) {
						room.highlight();
					} else {
						room.unhighlight();
					}
				}
			}
		}
	}
	
	double OVERLAP = .01;
	private boolean overlap(Room a, Room b){
		boolean overlapbX = b.getX() > a.getX() + OVERLAP && b.getX() + OVERLAP < a.getX() + a.getWidth();
		boolean overlapaX = a.getX() > b.getX() + OVERLAP && a.getX() + OVERLAP < b.getX() + b.getWidth();
		boolean overlapbY = b.getY() > a.getY() + OVERLAP && b.getY() + OVERLAP < a.getY() + a.getHeight();
		boolean overlapaY = a.getY() > b.getY() + OVERLAP && a.getY() + OVERLAP < b.getY() + b.getHeight();
		return (overlapbX || overlapaX) && (overlapbY || overlapaY);
	}
	
	private boolean inBounds(Room a){
		return a.getX() >= floor.getX() && a.getX() + a.getWidth() < floor.getX()+floor.getWidth()
		&& a.getY() >= floor.getY() && a.getY() + a.getHeight() < floor.getY()+floor.getHeight(); 
	}
	
	private boolean inGrayBounds(Room a){
		return (a.getX() >= floor.getX() && a.getX() + a.getWidth() < floor.getX()+floor.getWidth()
		&& a.getY() >= floor.getY() + floor.midHeight() && a.getY() + a.getHeight() < floor.getY()+floor.getHeight())
		|| (a.getX() >= floor.getX() && a.getX() + a.getWidth() < floor.getX()+floor.midWidth()
				&& a.getY() >= floor.getY() && a.getY() + a.getHeight() < floor.getY()+floor.getHeight())
				; 
	}
	
	private void switchToCurrent(){
		currentRooms = roomsSwitch(state);//highestRooms;
		deepCloneFromCurrent(state);
		
		for(StateButton stateButton: stateButtons){
			if(stateButton.getType() == StateType.CURRENT){
				stateButton.setFillColor(Color.GREEN);
			} else {
				stateButton.setFillColor(Color.GRAY);
			}
		}
		showRooms(state, StateType.CURRENT);
		state = StateType.CURRENT;
		if(VERBOSE) System.out.print("Switching to current (clone highest from current)");
	}
	
	public Vector<Room> roomsSwitch(StateType stateType){
		switch(stateType){
			case CURRENT:
				return currentRooms;
			case HIGHEST:
				return highestRooms;
			case HIGHEST_ADJACENCY:
				return highestAdjacencyRooms;
			case HIGHEST_SIZE:
				return highestSizeRooms;
			case HIGHEST_COUNT:
				return highestCountRooms;
		}
		return null;
	}
	
	public void update() {		
		
		bar.setFlags(buttons, roomsSwitch(state), selectedRooms);
		visualiser.update(getSelectedConstraints(roomsSwitch(state)), roomsSwitch(state));
		if(DO_NOT_SAVE_PLANS_WITH_OVERLAPS && overlapExists()) return;
		if(DO_NOT_SAVE_PLANS_OUTSIDE_BOUNDS && planOutsideBounds()) return;
		if(DO_NOT_SAVE_PLANS_OUTSIDE_GRAY_BOUNDS && planOutsideGrayBounds()) return;
		if (getScore(true, null, StateType.CURRENT) >= highScore) {
			highScore = getScore(true, null, StateType.CURRENT);
			deepCloneFromCurrent(StateType.HIGHEST);
			if(VERBOSE) System.out.println("Highest TOTAL set: "+highScore);
		}
		if (higherScoring(false, FlagType.ADJACENCY, StateType.HIGHEST_ADJACENCY)) {
			highAdjacencyScore = getScore(false, FlagType.ADJACENCY, StateType.CURRENT);
			deepCloneFromCurrent(StateType.HIGHEST_ADJACENCY);
			if(VERBOSE) System.out.println("Highest ADJ set: "+highAdjacencyScore);
		}
		if (higherScoring(false, FlagType.SIZE, StateType.HIGHEST_SIZE)) {
			highSizeScore = getScore(false, FlagType.SIZE, StateType.CURRENT);
			deepCloneFromCurrent(StateType.HIGHEST_SIZE);
			if(VERBOSE) System.out.println("Highest SIZE set: "+highSizeScore);
		}
		if (higherScoring(false, FlagType.COUNT, StateType.HIGHEST_COUNT)) {
			highCountScore = getScore(false, FlagType.COUNT, StateType.CURRENT);
			deepCloneFromCurrent(StateType.HIGHEST_COUNT);
			if(VERBOSE) System.out.println("Highest COUNT set: "+highCountScore);
		}
	}
	
	//Does not work for current.  Calculates based off of saved rooms
	private boolean higherScoring(boolean TOTAL, FlagType flagType, StateType stateType){
		if(getScore(TOTAL, flagType, StateType.CURRENT) > getScore(TOTAL, flagType, stateType)) return true;
		if(getScore(TOTAL, flagType, StateType.CURRENT) == getScore(TOTAL, flagType, stateType)){
			return getScore(true, null, StateType.CURRENT) > getScore(true, null, stateType);
		}
		return false;
	}
	
	private boolean overlapExists(){
		for(Room room: currentRooms){
			for(Room room2: currentRooms){
				if(!room.equals(room2) && overlap(room, room2)) {
					if(VERBOSE) System.out.println("Overlap found");
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean planOutsideBounds(){
		for(Room room: currentRooms){
			//if(!floor.getBounds().intersects(room.getBounds())) {
			if(!inBounds(room)){
				if(VERBOSE) System.out.println("Outside Bounds.");
				return true;
			}
		}
		return false;
	}
	private boolean planOutsideGrayBounds(){
		for(Room room: currentRooms){
			//if(!floor.getBounds().intersects(room.getBounds())) {
			if(!inGrayBounds(room)){
				if(VERBOSE) System.out.println("Outside Gray Bounds.");
				return true;
			}
		}
		return false;
	}
	
	private double getScore(boolean TOTAL, FlagType flagType, StateType stateType) {
		double satisfaction = 0.0;
		int count = 0;
		for (Flag flag: bar.flags) {
			if (flag.getType() == FlagType.ADJACENCY || flag.getType() == FlagType.COUNT || flag.getType() == FlagType.SIZE) {	
				if (TOTAL || flag.getType() == flagType) {
					satisfaction += flag.satisfaction(buttons, roomsSwitch(stateType), selectedRooms);
				}
			count++;
			}
		}
		return satisfaction/(double)count;
	}
	
	//Needed to work with the Stanford Graphics Program
	private void deepCloneFromCurrent(StateType stateType) {
		Vector<Room> copyRooms = new Vector<Room>();
		for(Room room: currentRooms){
			Room copyRoom = new Room(room.getWidth(),room.getHeight(),room.getType());
			copyRoom.setLocation(room.getX(),room.getY());
			copyRooms.add(copyRoom);
		}
		switch(stateType){
			case CURRENT:
				currentRooms = copyRooms;
			case HIGHEST:
				highestRooms = copyRooms;
			case HIGHEST_ADJACENCY:
				highestAdjacencyRooms = copyRooms;
			case HIGHEST_SIZE:
				highestSizeRooms = copyRooms;
			case HIGHEST_COUNT:
				highestCountRooms = copyRooms;
		}
		if(VERBOSE) System.out.println("Cloned");
	}

	private Vector<Constraint> getSelectedConstraints(Vector<Room> rooms) {
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
