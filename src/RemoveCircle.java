
/*
 * File: RemoveCircle.java
 * ----------------------
 * No different from GOval. (Needed to use instanceof)
 */

import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GLine;
import acm.graphics.GOval;


public class RemoveCircle extends GCompound {

	public RemoveCircle(double arg0, double arg1) {
		upperRight = new GOval(arg0, arg1);
		upperRight.setFillColor(Color.RED);
		upperRight.setFilled(true);
		add(upperRight);
		
		xLine1 = new GLine(0, 0, 10, 10);
		xLine2 = new GLine(10, 0, 0, 10);
		xLine1.setColor(Color.BLACK);
		xLine2.setColor(Color.BLACK);
		add(xLine1);
		add(xLine2);
	}
	
	private
		GOval upperRight;
		GLine xLine1;
		GLine xLine2;
}
