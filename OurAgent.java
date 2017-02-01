package prog1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OurAgent implements Agent{
	
	private Random random = new Random();
	private ArrayList<Pair> obsticles = new ArrayList<Pair>();
	private ArrayList<Pair> dirts = new ArrayList<Pair>();
	private Pair home = new Pair(-1, -1);
	private Pair size = new Pair(-1, -1);

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
								dirts.add(new Pair(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
							}
							else if(m.group(1).equals("OBSTACLE")) {
								obsticles.add(new Pair(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
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
		/*
		 * for lykkjur til að athuga hvort öll dirts og obsticles hafi 
		 * ekki örruglega farið í Arraylist.
		 *  
		System.out.println("****Skrifa ut dirts****");
		for(Pair dirt : dirts)
		{
			System.out.println("dirt: " + dirt.geX() + ", " + dirt.getY());
		}
		System.out.println("****Skrifa ut obsticles****");
		for(Pair obsticle : obsticles)
		{
			System.out.println("obsticle: " + obsticle.geX() + ", " + obsticle.getY());
		}
		*/
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
