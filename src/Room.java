import java.awt.Color;

import acm.graphics.*;

public class Room extends GCompound {

	public Room(double width, double height, RoomType type) {
		
		//Rotation requires polygons
		/*GPoint[] vertices = {new GPoint(0,0), new GPoint(width,0), new GPoint(width,height), new GPoint(0,height)};
		room = new GPolygon(vertices);*/
		
		room = new GRect(width, height);
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
		setType(type);
		
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
		//room.scale((x-initialX)/room.getWidth(), (y-initialY)/room.getHeight());
		room.setSize(x-initialX,y-initialY);
		placeControls();
	}
	
	public void rotate(double theta) {
		room.rotate(theta);
		placeControls();
	}
	
	public double getSqFootage(){
		return room.getWidth()*room.getHeight();
	}

	private void placeControls() {
		lowerRight.setLocation(room.getWidth()-5,room.getHeight()-5);
		//lowerLeft.setLocation(0,room.getHeight());
		upperRight.setLocation(room.getWidth()-5,-5);
	}
	
	private void setType(RoomType type){
		this.type = type;
		switch(type){
			case LAB:
				room.setFillColor(Color.BLUE);
				name = "LAB";
				break;
			case EXEC:
				room.setFillColor(Color.GREEN);
				name = "EXEC";
				break;
			case EXAM:
				room.setFillColor(Color.YELLOW);
				name = "EXAM";
				break;
			case WAITING:
				room.setFillColor(Color.ORANGE);
				name = "WAITING";
				break;
			case STORAGE:
				room.setFillColor(Color.GREEN);
				name = "STORAGE";
				break;
			default:
				room.setFillColor(Color.WHITE);
		}
		upperLeft = new GLabel(name, 5, 15);
		add(upperLeft);
	}
	
	private
		String name = "";
		RoomType type;
		GRect room;
		//GPolygon room; //if using rotate
		ResizeBlock lowerRight;
		RemoveCircle upperRight;
		//RotateDiamond lowerLeft;
		GLabel upperLeft;

}
