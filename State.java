package prog1;

import java.util.ArrayList;
import java.util.Map;



public class State {
	

	/*
	 *  Implement a class “State” that contains all the information about a state that you need to
		keep track of. The State class should have a method that returns a list of all moves that are
		legal in the state and a method that takes a move and returns the state that results from
		executing the move.
		Distinguish between nodes of the search tree and states! Each node has an associated
		state, but there may be several nodes that have the same state associated. In addition to
		a state, a node contains a reference to its parent (unless it is the root node), the move that
		was taken to get to the node and (if necessary) the path cost associated for getting from the
		root node to the node.
	 *
	*/
	
	// member variables for state, just ideas nothing final.
	private Pair location;
	private boolean north;
	private boolean south;
	private boolean west;
	private boolean east;
	private boolean dirt;
	private boolean initial;
	private boolean obsticle;
	private Orientation orientation; 


	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	State(Pair location, boolean north, boolean south, boolean west, boolean east, boolean dirt, boolean initial, boolean obsticle, Orientation orientation)
	{
		this.location    = new Pair(location.getX(), location.getY());
		this.north       = north;
		this.south       = south;
		this.west        = west;
		this.east        = east;
		this.dirt        = dirt;
		this.initial     = initial;
		this.obsticle    = obsticle;
		this.orientation = orientation;
	}
	
	public ArrayList<String> legalMoves()
	{
		ArrayList<String> moves = new ArrayList<String>();
		
		if(north)
		{
			moves.add("North");
		}
		if(south)
		{
			moves.add("South");
		}
		if(west)
		{
			moves.add("West");
		}
		if(east)
		{
			moves.add("East");
		}
		
		return moves;
	}
	
	public State nextState(String move, Map<Pair, State> map)
	{
		if(north && move.equals("North"))
		{
			return map.get(new Pair(location.getX(), location.getY() + 1));
		}
		else if(south && move.equals("south"))
		{
			return map.get(new Pair(location.getX(), location.getY() - 1));
		}
		else if(west && move.equals("West"))
		{
			return map.get(new Pair(location.getX() - 1, location.getY()));
		}
		else if(east && move.equals("East"))
		{
			return map.get(new Pair(location.getX() + 1, location.getY()));
		}
		
		return null;
		
	}
	
	//getters
	public boolean isObsticle() {
		return obsticle;
	}
	public Pair getLocation() {
		return location;
	}

	public boolean isNorth() {
		return north;
	}

	public boolean isSouth() {
		return south;
	}

	public boolean isWest() {
		return west;
	}
	
	public boolean isEast() {
		return east;
	}

	public boolean isDirt() {
		return dirt;
	}

	public boolean isInitial() {
		return initial;
	}

}
