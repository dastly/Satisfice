import java.awt.Color;

import acm.graphics.*;


public class Floorplan extends GCompound {

	GPoint[] vertices = {new GPoint(0,0),
			new GPoint(0,500),
			new GPoint(500,500),
			new GPoint(500, 260),
			new GPoint(260,260),
			new GPoint(260,0)};
	
	double CELL_WIDTH = 20;
	
	public Floorplan() {
		GPolygon outline = new GPolygon(vertices);
		outline.setFillColor(Color.LIGHT_GRAY);
		outline.setFilled(true);
		add(outline);
		addGridLines(outline);
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

}
