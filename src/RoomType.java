/*
 * Enum: RoomType
 * --------------
 * Every room type has an associated color and label.
 * Can be extended to include various constraint constants and other constants.
 */


import java.awt.Color;

//Cell 20px = 5ft
// CELL_WIDTH = 4.0 * Math.sqrt(10);



public enum RoomType {
	FACULTY ("FACULTY", Color.GREEN, 24.0 * Math.sqrt(10), 12.0 * Math.sqrt(10), 0, 0, 1, 5), //180 sq.ft
	MEETING ("MEETING", Color.YELLOW, 20.0 * Math.sqrt(10), 16.0 * Math.sqrt(10), 1, 0, 1, 5), //200 sq. ft
	AUDITORIUM ("AUD", Color.CYAN, 80.0 * Math.sqrt(10), 50.0 * Math.sqrt(10), 2, 1, 0, 0), //2500
	CAFETERIA ("CAFE", Color.GRAY, 40.0 * Math.sqrt(10), 30 * Math.sqrt(10), 3, 1, 0, 0), //750
	SMALLCLASSROOM ("SmallC", Color.ORANGE, 40.0 * Math.sqrt(10), 20.0 * Math.sqrt(10), 4, 0, 1, 5), //500
	LARGECLASSROOM ("LargeC", Color.MAGENTA, 40.0 * Math.sqrt(10), 32.0 * Math.sqrt(10), 5, 0, 1, 2), //800
	MEP ("MEP", Color.DARK_GRAY, 40.0 * Math.sqrt(10), 30.0 * Math.sqrt(10), 6, 1, 0, 0); //750
	
	private final String label;
	private final Color color;
	private final double width;
	private final double height;
	private final int index;
	private final int strict;
	private final int min;
	private final int best;
	
	RoomType(String label, Color color, double width, double height, int index, int strict, int min, int best){
		this.label = label;
		this.color = color;
		this.width = width;
		this.height = height;
		this.index = index;
		this.strict = strict;
		this.min = min;
		this.best = best;
	}
	
	public String label() {return label;}
	public Color color() {return color;}
	public double width() {return width;}
	public double height() {return height;}
	public int index() {return index;}
	public int strict() {return strict;}
	public int min() {return min;}
	public int best() {return best;}
	
}

