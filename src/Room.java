
/*
 * File: Room.java
 * ---------------
 * Currently, the room is a GRect.
 * 
 * If we want full rotation, it needs to be a GPolygon.
 * Code is commented out for this implementation.
 */

import java.awt.Color;

import acm.graphics.*;

public class Room extends GCompound {
	
	double DEFAULT_WIDTH = 100.0;
	double DEFAULT_HEIGHT = 100.0;
	
	public Room(RoomType type) {
		createRoom(DEFAULT_WIDTH, DEFAULT_HEIGHT, type);
	}
	
	public Room(double width, double height, RoomType type) {
		createRoom(width, height, type);
	}
	
	public void createRoom(double width, double height, RoomType type) {
		
		
		room = new GRect(width, height);
		
		//Rotation requires polygons
		/*GPoint[] vertices = {new GPoint(0,0), new GPoint(width,0), new GPoint(width,height), new GPoint(0,height)};
		room = new GPolygon(vertices);*/
		
		
		room.setFillColor(Color.BLUE);
		room.setFilled(true);
		add(room);
		
		
		
		lowerRight = new ResizeBlock(10,10);
		lowerRight.setFillColor(Color.RED);
		lowerRight.setFilled(true);
		add(lowerRight);
		
		upperRight = new RemoveCircle(10,10);
		upperRight.setFillColor(Color.RED);
		upperRight.setFilled(true);
		add(upperRight);
		
		/*GPoint[] dvertices = {new GPoint(0,5), new GPoint(5,0), new GPoint(0,-5), new GPoint(-5,0)};
		lowerLeft = new RotateDiamond(dvertices);
		lowerLeft.setFillColor(Color.GREEN);
		lowerLeft.setFilled(true);
		add(lowerLeft);*/
		
		placeControls();
		this.type = type;
		room.setFillColor(type.color());
		add(new GLabel(type.label(), 5, 15));
		
	}
	
	@Override
	public double getWidth(){
		return room.getWidth();
	}
	
	@Override
	public double getHeight(){
		return room.getHeight();
	}

	public void resize(double initialX, double initialY, double x, double y) {
		room.setSize(x-initialX,y-initialY);
		//room.scale((x-initialX)/room.getWidth(), (y-initialY)/room.getHeight()); //GPolygon resize
		placeControls();
	}
	
//	GPolygon rotation
//	public void rotate(double theta) {
//		room.rotate(theta);
//		placeControls();
//	}
	
	public double getSqFootage(){
		return room.getWidth()*room.getHeight();
	}

	private void placeControls() {
		lowerRight.setLocation(room.getWidth()-5,room.getHeight()-5);
		upperRight.setLocation(room.getWidth()-5,-5);
		//lowerLeft.setLocation(0,room.getHeight());
		
	}
	
	private
		RoomType type;
		GRect room;
		ResizeBlock lowerRight;
		RemoveCircle upperRight;
		
		//GPolygon room; //if using rotate
		//RotateDiamond lowerLeft;
}
