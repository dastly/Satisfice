/*
 * Enum: RoomType
 * --------------
 * Every room type has an associated color and label.
 * Can be extended to include various constraint constants and other constants.
 */


import java.awt.Color;

//Cell 20px = 5ft

public enum RoomType {
	FACULTY ("FACULTY", Color.GREEN, 60, 40, 0),
	MEETING ("MEETING", Color.YELLOW, 80, 40, 1),
	AUDITORIUM ("AUD", Color.CYAN, 240, 200, 2),
	CAFETERIA ("CAFE", Color.GRAY, 160, 160, 3),
	SMALLCLASSROOM ("SmallC", Color.ORANGE, 200, 40, 4),
	LARGECLASSROOM ("LargeC", Color.MAGENTA, 160, 80, 5),
	MEP ("MEP", Color.DARK_GRAY, 160, 100, 6);
	
	private final String label;
	private final Color color;
	private final double width;
	private final double height;
	private final int index;
	
	RoomType(String label, Color color, double width, double height, int index){
		this.label = label;
		this.color = color;
		this.width = width;
		this.height = height;
		this.index = index;
	}
	
	public String label() {return label;}
	public Color color() {return color;}
	public double width() {return width;}
	public double height() {return height;}
	public int index() {return index;}
	
}

