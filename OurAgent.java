package prog1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OurAgent implements Agent{
	
	private Random random = new Random();
	private Map<String, Pair> obsticles = new HashMap<String, Pair>();
	private Map<String, Pair> dirts = new HashMap<String, Pair>();
	private Pair home = new Pair(-1, -1);
	private Pair lastGoal = new Pair(-1, -1);
	private Pair size = new Pair(-1, -1);
	private Map<Pair, State> environment = new HashMap<Pair, State>();
	private String orientation;
	private Graph graph;
	private int dirtsLeft;
	private Queue<Stack<Integer>> finalPath = new LinkedList<Stack<Integer>>();
	private ArrayList<Pair> keys;
	private Pair currPos = new Pair(-1,-1);
	private Orientation currOrientation;
	private Queue<String> listOfMoves = new LinkedList<String>();

	/*
		init(Collection<String> percepts) is called once before you have to select the first action. Use it to find a plan. Store the plan and just execute it step by step in nextAction.
	*/

	@Override
	public void init(Collection<String> percepts) {
		/*
		Possible percepts are:
		- "(SIZE x y)" denoting the size of the environment, where x,y are integers
		- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
		- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
		- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
		Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
		The robot is turned off initially, so don't forget to turn it on.
	    */
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
		for (String percept:percepts) {
			//System.out.println("Percept: " + percept);
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("robot is at " + m.group(1) + "," + m.group(2));
						home.setPair(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
						currPos.setPair(home.getX(), home.getY());
					}
				} else {
					System.out.println("other percept:" + percept);	
					if (perceptName.equals("AT")) {
						Matcher m = Pattern.compile("\\(\\s*AT\\s+([A-Z]+)\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
						if (m.matches()) {
							if(m.group(1).equals("DIRT")) {
								dirts.put(new String(m.group(2) + ", " + m.group(3)), new Pair(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
							}
							else if(m.group(1).equals("OBSTACLE")) {
								obsticles.put(new String(m.group(2) + ", " + m.group(3)), new Pair(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
							}	
						}
					}
					else if (perceptName.equals("SIZE"))
					{
						Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
						if (m.matches()) {
							System.out.println("World size is " + m.group(1) + "," + m.group(2));
							size.setPair(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
						}
					}
					else if (perceptName.equals("ORIENTATION"))
					{
						Matcher m = Pattern.compile("\\(\\s*ORIENTATION\\s+([A-Z]+)\\s*\\)").matcher(percept);
						if (m.matches()) {
							orientation = m.group(1);
							currOrientation = Orientation.valueOf(orientation);
							System.out.println("Orientation: " + orientation);
						}
					}
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		keys = new ArrayList<Pair>();
		dirtsLeft = dirts.size();
		 /* for lykkjur til að athuga hvort öll dirts og obsticles hafi 
		 * ekki örruglega farið í Arraylist.
		 *
		System.out.println("****Skrifa ut dirts****");
		for(Map.Entry<String, Pair> dirt : dirts.entrySet())
		{
			System.out.println("dirt: " + dirt.getValue().getX() + ", " + dirt.getValue().getY());
		}
		System.out.println("****Skrifa ut obsticles****");
		for(Map.Entry<String, Pair> obsticle : obsticles.entrySet())
		{
			System.out.println("dirt: " + obsticle.getValue().getX() + ", " + obsticle.getValue().getY());
		}
		*/
		
		createEnviorment();
		for(Map.Entry<Pair, State> env : environment.entrySet())
		{
			System.out.println("loc: " +  env.getValue().getLocation().getX() + ", " + env.getValue().getLocation().getY() + ", " + "isDirt: " + env.getValue().isDirt() + ", isEast: " + env.getValue().isEast() + ", isNorth: " + env.getValue().isNorth() + ", isSouth: " + env.getValue().isSouth() + ", isWest: " + env.getValue().isWest() + ", intial: " + env.getValue().isInitial() + ", isObsticle: " + env.getValue().isObsticle());
		}
		
		lastGoal = new Pair(home.getX(), home.getY());
		for(int i = 0; i < dirtsLeft; i++)
		{
			createSearchGraph();
			Stack<Integer> path = new Stack<Integer>();
			//path = graph.BFS(CoordsAndInts.coordinatesToInt(lastGoal.getX(), lastGoal.getY(), size));
			System.out.println("lastgoal " + lastGoal.getX() + ", " + lastGoal.getY());
			path = graph.DFS(CoordsAndInts.coordinatesToInt(lastGoal.getX(), lastGoal.getY(), size));
			System.out.println(path);
			if(path == null)
			{
				System.out.println("Path er Null");
				break;
			}
			else
			{
				finalPath.add(path);
				System.out.println("Peek: " + path.get(0));
				updateEnv(CoordsAndInts.intToCoord(path.get(0), size));
			}
		}
		System.out.println(finalPath);
		
		makePath();
	}

	private void updateEnv(Pair peek) { 
		System.out.println("Pair peek: " + peek.getX() + ", " + peek.getY());
		lastGoal.setPair(peek.getX(), peek.getY());
		//dirts.remove(Integer.toString(peek.getX()) + ", " + Integer.toString(peek.getY()));

		for(Map.Entry<Pair, State> env : environment.entrySet())
		{
			System.out.println("loc: " +  env.getValue().getLocation().getX() + ", " + env.getValue().getLocation().getY() + ", " + "isDirt: " + env.getValue().isDirt() + ", isEast: " + env.getValue().isEast() + ", isNorth: " + env.getValue().isNorth() + ", isSouth: " + env.getValue().isSouth() + ", isWest: " + env.getValue().isWest() + ", intial: " + env.getValue().isInitial() + ", isObsticle: " + env.getValue().isObsticle());
		}
	}

	private void createSearchGraph() {
		int graphSize = size.getX() * size.getY();
		graph = new Graph(graphSize, environment, size, keys);		
	}

	private void createEnviorment() {
		
		for(int y = 1; y <= size.getY(); y++)
		{
			for(int x = 1; x <= size.getX(); x++)
			{
				boolean west = true;
				boolean east = true;
				boolean north = true;
				boolean south = true;
				boolean goal = false;
				boolean initial = false;
				boolean obsticle = false;
				Orientation direction = Orientation.EMPTY;
				
				if(obsticles.get(Integer.toString(x) + Integer.toString(y)) != null)
				{
					obsticle = true;
				}
				else
				{
					if(x == 1 || obsticles.get(Integer.toString(x - 1) + ", " + Integer.toString(y)) != null)
					{
						west = false;
					}
					if(y == 1 || obsticles.get(Integer.toString(x) + ", " + Integer.toString(y - 1)) != null)
					{
						south = false;
					}
					if(x == size.getX() || obsticles.get(Integer.toString(x + 1) + ", " + Integer.toString(y)) != null)
					{
						east = false;
					}
					if(y == size.getY()|| obsticles.get(Integer.toString(x) + ", " + Integer.toString(y + 1)) != null)
					{
						north = false;
					}
					if(dirts.get(Integer.toString(x) + ", " + Integer.toString(y)) != null)
					{
						goal = true;
					}
					if(x == home.getX() && y == home.getY())
					{
						direction = Orientation.valueOf(orientation);
						initial = true;
					}
				}
				
				Pair pair = new Pair(x, y);
				keys.add(pair);
				environment.put(pair, new State(pair, north, south, west, east, goal, initial, obsticle, direction));
			}
		}
		
	}
	
	private void makePath() {
		listOfMoves.add("TURN_ON");
		
		for(Stack<Integer> path : finalPath )
		{
			while(!path.isEmpty())
			{
				int i = path.pop();
				Pair dest = CoordsAndInts.intToCoord(i, size);
				
				//First we check if there is dirt where we are now...
				if(dirts.containsKey(Integer.toString(currPos.getX()) + ", " + Integer.toString(currPos.getY())))
				{
					//...and suck it up if it is present
					System.out.println("Dirt!");
					listOfMoves.add("SUCK");
					dirts.remove(Integer.toString(currPos.getX()) + ", " + Integer.toString(currPos.getY()));	
				}		
				
				System.out.println("****Skrifa ut dirts****");
				for(Map.Entry<String, Pair> dirt : dirts.entrySet())
				{					
					System.out.println("dirt: " + dirt.getKey());
				}
				
				//System.out.println("CurrPos: " + currPos.getX() + ", " + currPos.getY());
				
				System.out.println("CurrPos: " + currPos.getX() + ", " + currPos.getY() + "\nDestPos: " + dest.getX() + ", " + dest.getY() + "\nOrientation " + currOrientation);

				if(currPos.getX() < dest.getX())
				{
					if(currOrientation == Orientation.WEST)
					{
						//snúa mér til hægri
						currOrientation = Orientation.NORTH;
						//adda turn right
						listOfMoves.add("TURN_RIGHT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.NORTH)
					{
						//snúa mér til hægri
						currOrientation = Orientation.EAST;
						//adda turn right
						listOfMoves.add("TURN_RIGHT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.SOUTH)
					{
						//snúa mér til vinstri
						currOrientation = Orientation.EAST;
						//adda turn left
						listOfMoves.add("TURN_LEFT");
						System.out.println("I turned " + currOrientation);
					}
					
				}
				else if(currPos.getX() > dest.getX())
				{
					if(currOrientation == Orientation.EAST)
					{
						//snúa mér til vinstri
						currOrientation = Orientation.NORTH;
						//adda turn left
						listOfMoves.add("TURN_LEFT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.NORTH)
					{
						//snúa mér til vinstri
						currOrientation = Orientation.WEST;
						//adda turn left
						listOfMoves.add("TURN_LEFT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.SOUTH)
					{
						//snúa mér til hægri
						currOrientation = Orientation.EAST;
						//adda turn right
						listOfMoves.add("TURN_RIGHT");
						System.out.println("I turned " + currOrientation);
					}
				}
				else if(currPos.getY() < dest.getY())
				{
					if(currOrientation == Orientation.SOUTH)
					{
						//snúa mér til hægri
						currOrientation = Orientation.WEST;
						//adda turn right
						listOfMoves.add("TURN_RIGHT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.WEST)
					{
						//snúa mér til hægri
						currOrientation = Orientation.NORTH;
						//adda turn right
						listOfMoves.add("TURN_RIGHT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.EAST)
					{
						//snúa mér til vinstri
						currOrientation = Orientation.NORTH;
						//adda turn left
						listOfMoves.add("TURN_LEFT");
						System.out.println("I turned " + currOrientation);
					}
				}
				else if(currPos.getY() > dest.getY())
				{
					if(currOrientation == Orientation.NORTH)
					{
						//snúa mér til vinstri
						currOrientation = Orientation.WEST;
						//adda turn left
						listOfMoves.add("TURN_LEFT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.WEST)
					{
						//snúa mér til vinstri
						currOrientation = Orientation.SOUTH;
						//adda turn left
						listOfMoves.add("TURN_LEFT");
						System.out.println("I turned " + currOrientation);
					}
					if(currOrientation == Orientation.EAST)
					{
						//snúa mér til hægri
						currOrientation = Orientation.SOUTH;
						//adda turn right
						listOfMoves.add("TURN_RIGHT");
						System.out.println("I turned " + currOrientation);
					}
				}
				
				//adda go!
				if(currPos.getX() != dest.getX() || currPos.getY() != dest.getY())
				{
					System.out.println("GOO");
					listOfMoves.add("GO");
					if(currOrientation == Orientation.EAST) { currPos.setPair(currPos.getX()+1, currPos.getY()); }
					if(currOrientation == Orientation.NORTH) { currPos.setPair(currPos.getX(), currPos.getY()+1); }
					if(currOrientation == Orientation.WEST) { currPos.setPair(currPos.getX()-1, currPos.getY()); }
					if(currOrientation == Orientation.SOUTH) { currPos.setPair(currPos.getX(), currPos.getY()-1); }
				}
				System.out.println("\n\n");
			}
		}
		listOfMoves.add("TURN_OFF");
		System.out.println(listOfMoves);
	}

	@Override
	public String nextAction(Collection<String> percepts) {
		/*System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		return actions[random.nextInt(actions.length)];*/
		return listOfMoves.poll();
	}

}

