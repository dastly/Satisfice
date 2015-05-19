import acm.graphics.GCompound;
import acm.graphics.GLabel;


public class TaskDescription extends GCompound {
	int FACULTY = 0;
	int MEETING = 1;
	int AUD = 2;
	int CAFE = 3;
	int SMALLC = 4;
	int LARGEC = 5;
	int MEP = 6;
	int INSTRUCTION = 7;
	
	int OFFSET = 20;
	
	String tasks[] = {
			"5 or more faculty offices of at least 180 sq. ft.",
			"5 or more meeting rooms of at least 200 sq. ft.",
			"1 auditorium of at least 2500 sq. ft.",
			"1 cafeteria of at least 700 sq. ft.",
			"5 or more small classrooms of at least 500 sq. ft.",
			"2 or more large classrooms of at least 800 sq. ft.",
			"1 mechanical, electrical, and plumbing (MEP) space (this includes restrooms) of at least 750 sq. ft.",
			"Click the 'VIEW' button for any room type to see a description."
	};
	
	public TaskDescription() {
		addHeader();
		update(INSTRUCTION);
	}
	
	public void update(int room) {
		removeAll();
		addHeader();
		GLabel label = new GLabel(tasks[room]);
		label.setLocation(0, OFFSET);
		add(label);
	}
	
	private void addHeader() {
		GLabel label = new GLabel("TASK DESCRIPTION:");
		add(label);
	}
}
