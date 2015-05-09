

import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Satisficer extends GraphicsProgram {
	
	GObject object = null;
	GLabel score = new GLabel("Score: ", 20, 10);
	GRect exec;
	GRect storage;
	
	public void run() {
		
		setSize(1800, 500);
		GRect lab = new GRect(20,10,100,100);
		lab.setFillColor(Color.RED);
		lab.setFilled(true);
		exec = new GRect(120,10,100,100);
		exec.setFillColor(Color.BLUE);
		exec.setFilled(true);
		GRect exam = new GRect(220,10,100,100);
		exam.setFillColor(Color.YELLOW);
		exam.setFilled(true);
		GRect wait = new GRect(320,10,100,100);
		wait.setFillColor(Color.GREEN);
		wait.setFilled(true);
		storage = new GRect(420,10,100,100);
		storage.setFillColor(Color.ORANGE);
		storage.setFilled(true);
		add(lab);
		add(exec);
		add(exam);
		add(wait);
		add(storage);
		add(score);
		printScore();
		/*add(new GRect(20, 10, 100, 100)); // head
		add(new GOval(40, 30, 20, 20)); // left eye
		add(new GOval(80, 30, 20, 20)); // right eye
		add(new GOval(65, 60, 10, 10)); // nose
		add(new GLine(40, 80, 50, 90)); // mouth
		add(new GLine(50, 90, 90, 90)); // mouth
		add(new GLine(90, 90, 100, 80)); // mouth
		add(new GLabel("Smile!", 20, 140));*/
		// TODO: finish this program
		addMouseListeners();
	}
	
	public void mousePressed(MouseEvent e){
		object = getElementAt(e.getX(),e.getY());
	}
	public void mouseDragged(MouseEvent e){
		if(object != null) object.setLocation(e.getX(),e.getY());
	}
	public void mouseReleased(MouseEvent e){
		//if(object != null) System.out.println(object.getX() - 10);
		object = null;
		printScore();
	}
	
	private void printScore(){
		String scoreString =  "Score: ";
		int scoreNum = 0;
		if(distance(exec.getLocation(), storage.getLocation()) < 150) scoreNum += -100;
		System.out.println(distance(exec.getLocation(), storage.getLocation()));
		scoreString += scoreNum;
		score.setLabel(scoreString);
	}
	
	private double distance(GPoint a, GPoint b){
		return Math.sqrt(Math.pow(a.getX() - b.getX(),2) + Math.pow(a.getY() - b.getY(),2));
	}
}
