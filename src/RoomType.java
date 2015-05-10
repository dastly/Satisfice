import java.awt.Color;



public enum RoomType {
	LAB ("LAB", Color.CYAN),
	EXAM ("EXAM", Color.GREEN),
	EXEC ("EXEC", Color.YELLOW),
	STORAGE ("STORAGE", Color.GRAY),
	WAITING ("WAITING", Color.ORANGE);
	
	private final String label;
	private final Color color;
	
	RoomType(String label, Color color){
		this.label = label;
		this.color = color;
	}
	
	public String label() {return label;}
	public Color color() {return color;}
	
}

