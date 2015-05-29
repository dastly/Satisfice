import java.util.Vector;


public class AdjacencyConstraint extends Constraint {
	
	double DROP_OFF_FACTOR = 40.0; //2*CELL_WIDTH, or 10 ft.
	double HIGH_AFFINITY = 6.0;
	int affinityMatrix[][] = { //Also in visualizer
		      { 1, 0, -2,  1, -2, -2, -1},
		      {0,  1, -1,  1,  2,  2,  0},
		      {-2,  1,  1,  2,  2,  2,  0},
		      { 1,  1,  2,  1,  2,  2,  0},
		      {-2,  2,  2,  2,  1,  2,  0},
		      {-2,  2,  2,  2,  2,  1,  0},
		      {-1, -1,  0,  0,  0,  0,  1}
	};
	
	public AdjacencyConstraint(Room room) {
		super(room);
	}
	
	//Evaluates to a number between 0.0 and 1.0
	public double evaluate(Vector<Room> rooms){
		Room cRoom = getRoom();
		int adjacencies = 0;
		double adjacencyScore = 0;
		for(Room room: rooms){
			if(!room.equals(cRoom)){
//				if(distance(room, cRoom) <= ADJACENCY_THRESHOLD){
//					adjacencies++;
//					adjacencyScore += affinity(room, cRoom);
//				}
				//adjacencies++;
				adjacencyScore += affinity(room, cRoom) * 1.0/(1.0+Math.pow(distance(room, cRoom)/DROP_OFF_FACTOR,2));
			}
		}
		//if(adjacencies == 0) return 0.5;
		if(adjacencyScore > HIGH_AFFINITY) adjacencyScore = HIGH_AFFINITY;
		if(adjacencyScore < -HIGH_AFFINITY) adjacencyScore = - HIGH_AFFINITY;
		return (adjacencyScore/HIGH_AFFINITY + 1)/2.0;
	}
	
	public int[][] getAffinityMatrix() {
		return affinityMatrix;
	}
	
	private double distance(Room a, Room b){
		double xDiff1 = Math.max(b.getX() - (a.getX() + a.getWidth()),0);
		double xDiff2 = Math.max(a.getX() - (b.getX() + b.getWidth()),0);
		double yDiff1 = Math.max(b.getY() - (a.getY() + a.getHeight()),0);
		double yDiff2 = Math.max(a.getY() - (b.getY() + b.getHeight()),0);
		return Math.sqrt(Math.pow(xDiff1,2) + Math.pow(xDiff2,2) + Math.pow(yDiff1,2) + Math.pow(yDiff2,2));
	}
	
	private int affinity(Room a, Room b) {
		return affinityMatrix[a.getType().index()][b.getType().index()];
	}

}
