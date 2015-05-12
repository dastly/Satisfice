/*
 * Enum: RoomType
 * --------------
 * Every room type has an associated color and label.
 * Can be extended to include various constraint constants and other constants.
 */


import java.awt.Color;

//Cell 20px = 5ft

public enum RoomType {
	FACULTY ("FACULTY", Color.GREEN, 60, 40, 0, 0, 1, 5),
	MEETING ("MEETING", Color.YELLOW, 80, 40, 1, 0, 1, 5),
	AUDITORIUM ("AUD", Color.CYAN, 240, 200, 2, 1, 0, 0),
	CAFETERIA ("CAFE", Color.GRAY, 160, 160, 3, 1, 0, 0),
	SMALLCLASSROOM ("SmallC", Color.ORANGE, 200, 40, 4, 0, 1, 5),
	LARGECLASSROOM ("LargeC", Color.MAGENTA, 160, 80, 5, 0, 1, 2),
	MEP ("MEP", Color.DARK_GRAY, 160, 100, 6, 1, 0, 0);
	
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

