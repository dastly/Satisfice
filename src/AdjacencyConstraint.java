import java.util.Vector;


public class AdjacencyConstraint extends Constraint {

	double ADJACENCY_THRESHOLD = 20.0;
	
	public AdjacencyConstraint(Room room) {
		super(room);
	}
	
	public double evaluate(Vector<Room> rooms, int[][] affinityMatrix){
		Room cRoom = getRoom();
		int adjacencies = 0;
		int adjacencyScore = 0;
		for(Room room: rooms){
			if(!room.equals(cRoom)){
				if(distance(room, cRoom) <= ADJACENCY_THRESHOLD){
					adjacencies++;
					adjacencyScore += affinity(room, cRoom, affinityMatrix);
				}
			}
		}
		if(adjacencies == 0) return 0.5;
		return ((double)adjacencyScore/(double)adjacencies + 2)/4.0;
	}
	
	private double distance(Room a, Room b){
		double xDiff1 = Math.max(b.getX() - (a.getX() + a.getWidth()),0);
		double xDiff2 = Math.max(a.getX() - (b.getX() + b.getWidth()),0);
		double yDiff1 = Math.max(b.getY() - (a.getY() + a.getHeight()),0);
		double yDiff2 = Math.max(a.getY() - (b.getY() + b.getHeight()),0);
		return Math.sqrt(Math.pow(xDiff1,2) + Math.pow(xDiff2,2) + Math.pow(yDiff1,2) + Math.pow(yDiff2,2));
	}
	
	private int affinity(Room a, Room b, int[][] affinityMatrix) {
		return affinityMatrix[a.getType().index()][b.getType().index()];
	}

}
