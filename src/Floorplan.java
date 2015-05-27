import java.awt.Color;

import acm.graphics.*;


public class Floorplan extends GCompound {
	
	//ft*4 = pxl
	double CELL_WIDTH = 4.0 * Math.sqrt(10);
	double NUM_CELLS = 40;
	double FLOOR_WIDTH = CELL_WIDTH * NUM_CELLS;
	double FLOOR_HEIGHT = CELL_WIDTH * NUM_CELLS;
	double FLOOR_MID_WIDTH = CELL_WIDTH * NUM_CELLS/2 + CELL_WIDTH;
	double FLOOR_MID_HEIGHT = CELL_WIDTH * NUM_CELLS/2 + CELL_WIDTH;
	
	GPoint[] vertices = {new GPoint(0, 0),
			new GPoint(0, FLOOR_HEIGHT),
			new GPoint(FLOOR_WIDTH, FLOOR_HEIGHT),
			new GPoint(FLOOR_WIDTH, FLOOR_MID_HEIGHT),
			new GPoint(FLOOR_MID_WIDTH, FLOOR_MID_HEIGHT),
			new GPoint(FLOOR_MID_WIDTH, 0)};
	
	
	public Floorplan() {
		outline = new GPolygon(vertices);
		outline.setFillColor(Color.LIGHT_GRAY);
		outline.setFilled(true);
		add(outline);
		addGridLines(outline);
	}
	
	public double midWidth(){
		return FLOOR_MID_WIDTH;
	}
	
	public double midHeight(){
		return FLOOR_MID_HEIGHT;
	}

	private void addGridLines(GPolygon outline) {
		double width = outline.getBounds().getWidth();
		double height = outline.getBounds().getHeight();
		double x = outline.getBounds().getX();
		double y = outline.getBounds().getY();
		for(double i = 0; i < width; i = i + CELL_WIDTH){
			add(new GLine(i + x,y,i+x,y+height));
		}
		add(new GLine(width + x,y,width+x,y+height));
		for(double j = 0; j < height; j = j + CELL_WIDTH){
			add(new GLine(x,y+j,x+width,y+j));
		}
		add(new GLine(x,y+height,x+width,y+height));
	}
	
	//snaps to closest grid square
	public GPoint snapToGrid(GPoint pt){
		double x = getX();
		double y = getY();
		int cx = (int) ((pt.getX() - x)/CELL_WIDTH);
		int cy = (int) ((pt.getY() - y)/CELL_WIDTH);
		if(pt.getX() - (x + cx*CELL_WIDTH) > CELL_WIDTH/2) cx++;
		if(pt.getY() - (y + cy*CELL_WIDTH) > CELL_WIDTH/2) cy++;
		return new GPoint(x + cx*CELL_WIDTH, y + cy*CELL_WIDTH);
	}
	
	//Snaps to upper left corner of closest grid square
//	public GPoint snapToGrid(GPoint pt){
//		double x = getX();
//		double y = getY();
//		int cx = (int) ((pt.getX() - x)/CELL_WIDTH);
//		int cy = (int) ((pt.getY() - y)/CELL_WIDTH);
//		return new GPoint(x + cx*CELL_WIDTH, y + cy*CELL_WIDTH);
//	}
	
	private
		GPolygon outline;

}
