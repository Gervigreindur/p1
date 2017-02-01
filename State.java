package prog1;

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
	
	private Pair currentLocation;
	private String currentOrientation;
	private int dirtsLeft;
	

}
