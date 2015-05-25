
/*
 * File: Room.java
 * ---------------
 * Currently, the room is a GRect.
 * 
 * If we want full rotation, it needs to be a GPolygon.
 * Code is commented out for this implementation.
 */

import java.awt.Color;
import java.util.Vector;

import acm.graphics.*;

public class Room extends GCompound {
	
	double DEFAULT_WIDTH = 100.0;
	double DEFAULT_HEIGHT = 100.0;
	double PXL_TO_FT = 25.0/400.0;
	
	public Room(RoomType type) {
		createRoom(DEFAULT_WIDTH, DEFAULT_HEIGHT, type);
	}
	
	public Room(double width, double height, RoomType type) {
		createRoom(width, height, type);
	}
	
	public void createRoom(double width, double height, RoomType type) {
		
		shape = new GRect(width, height);
		
		//Rotation requires polygons
		/*GPoint[] vertices = {new GPoint(0,0), new GPoint(width,0), new GPoint(width,height), new GPoint(0,height)};
		room = new GPolygon(vertices);*/
		
		
		shape.setFillColor(Color.BLUE);
		shape.setFilled(true);
		add(shape);
		
		lowerRight = new ResizeBlock(10,10);
		lowerRight.setFillColor(Color.RED);
		lowerRight.setFilled(true);
		add(lowerRight);
		
		upperRight = new RemoveCircle(10,10);
		add(upperRight);
		
		/*GPoint[] dvertices = {new GPoint(0,5), new GPoint(5,0), new GPoint(0,-5), new GPoint(-5,0)};
		lowerLeft = new RotateDiamond(dvertices);
		lowerLeft.setFillColor(Color.GREEN);
		lowerLeft.setFilled(true);
		add(lowerLeft);*/
		
		placeControls();
		this.type = type;
		shape.setFillColor(type.color());
		add(new GLabel(type.label(), 5, 15));
		size = new GLabel(getSqFootage() + "ft.^2", 5, 25);
		add(size);
		
		addRoomConstraints();
		
	}
	
	private void addRoomConstraints() {
		constraints = new Vector<Constraint>();
		ac = new AdjacencyConstraint(this);
		sc = new SizeConstraint(this);
		constraints.add(ac);
		constraints.add(sc);
	}

	@Override
	public double getWidth(){
		return shape.getWidth();
	}
	
	@Override
	public double getHeight(){
		return shape.getHeight();
	}
	
//	public double getHighestX(){
//		return highestX;
//	}
//	
//	public double getHighestY(){
//		return highestY;
//	}
//	
//	public void setHighestXY(double highestX, double highestY){
//		this.highestX = highestX;
//		this.highestX = highestY;
//	}
	
	public void setSize(double width, double height) {
		//if(width < 0) width = 0;
		//(height < 0) height = 0;
		shape.setSize(width, height);
		placeControls();
		size.setLabel(getSqFootage() + "ft.^2");
	}
	
//	GPolygon rotation
//	public void rotate(double theta) {
//		room.rotate(theta);
//		placeControls();
//	}
	
	public double getSqFootage(){
		int roundedDown = (int) Math.floor((shape.getWidth()*shape.getHeight())*PXL_TO_FT);
		if(roundedDown % 10 == 9) roundedDown += 1;
		return (double) roundedDown;
	}
	
	public void highlight(){
		shape.setColor(Color.YELLOW);
	}
	
	public void markRed() {
		shape.setColor(Color.RED);		
	}
	
	public void unhighlight(){
		shape.setColor(Color.BLACK);
	}

	private void placeControls() {
		lowerRight.setLocation(shape.getWidth()-5,shape.getHeight()-5);
		upperRight.setLocation(shape.getWidth()-5,-5);
		//lowerLeft.setLocation(0,room.getHeight());
		
	}
	
	public RoomType getType(){
		return type;
	}
	
	public Vector<Constraint> getConstraints(){
		return constraints;
	}
	
	public void addConstraint(Constraint constraint){
		constraints.add(constraint);
	}
	
	public void removeConstraint(Constraint constraint){
		constraints.remove(constraint);
	}
	
	public AdjacencyConstraint getAdjacencyConstraint(){
		return ac;
	}
	
	public SizeConstraint getSizeConstraint(){
		return sc;
	}
	
	private
		RoomType type;
		GRect shape;
		ResizeBlock lowerRight;
		RemoveCircle upperRight;
		GLabel size;
		Vector<Constraint> constraints;
		AdjacencyConstraint ac;
		SizeConstraint sc;
//		double highestX;
//		double highestY;
		//GPolygon room; //if using rotate
		//RotateDiamond lowerLeft;

		
}
