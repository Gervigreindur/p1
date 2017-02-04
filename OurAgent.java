package prog1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OurAgent implements Agent{
	
	private Random random = new Random();
	private Map<String, Pair> obsticles = new HashMap<String, Pair>();
	private Map<String, Pair> dirts = new HashMap<String, Pair>();
	private Pair home = new Pair(-1, -1);
	private Pair size = new Pair(-1, -1);
	private Map<Pair, State> environment = new HashMap<Pair, State>();;

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
					}
				} else {
					System.out.println("other percept:" + percept);	
					if (perceptName.equals("AT")) {
						Matcher m = Pattern.compile("\\(\\s*AT\\s+([A-Z]+)\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
						if (m.matches()) {
							if(m.group(1).equals("DIRT")) {
								dirts.put(new String(m.group(2) + m.group(3)), new Pair(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
							}
							else if(m.group(1).equals("OBSTACLE")) {
								obsticles.put(new String(m.group(2) + m.group(3)), new Pair(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
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
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		
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
		/*for(Map.Entry<Pair, State> env : environment.entrySet())
		{
			System.out.println("loc: " +  env.getValue().getLocation().getX() + ", " + env.getValue().getLocation().getY() + ", " + "isDirt: " + env.getValue().isDirt() + ", isEast: " + env.getValue().isEast() + ", isNorth: " + env.getValue().isNorth() + ", isSouth: " + env.getValue().isSouth() + ", isWest: " + env.getValue().isWest() + ", intial: " + env.getValue().isInitial() + ", isObsticle: " + env.getValue().isObsticle());
		}*/
	}

	private void createEnviorment() {
		
		for(int x = 1; x <= size.getX(); x++)
		{
			for(int y = 1; y <= size.getY(); y++)
			{
				boolean west = true;
				boolean east = true;
				boolean north = true;
				boolean south = true;
				boolean goal = false;
				boolean initial = false;
				boolean obsticle = false;
				
				if(obsticles.get(Integer.toString(x) + Integer.toString(y)) != null)
				{
					obsticle = true;
				}
				else
				{
					if(x == 1 || obsticles.get(Integer.toString(x - 1) + Integer.toString(y)) != null)
					{
						west = false;
					}
					if(y == 1 || obsticles.get(Integer.toString(x) + Integer.toString(y - 1)) != null)
					{
						south = false;
					}
					if(x == size.getX() || obsticles.get(Integer.toString(x + 1) + Integer.toString(y)) != null)
					{
						east = false;
					}
					if(y == size.getY()|| obsticles.get(Integer.toString(x) + Integer.toString(y + 1)) != null)
					{
						north = false;
					}
					if(dirts.get(Integer.toString(x) + Integer.toString(y)) != null)
					{
						goal = true;
					}
					if(x == home.getX() && y == home.getY())
					{
						initial = true;
					}
				}
				
				Pair pair = new Pair(x, y);
				environment.put(pair, new State(pair, north, south, west, east, goal, initial, obsticle));
			}
		}
		
	}

	@Override
	public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		return actions[random.nextInt(actions.length)];
	}

}
