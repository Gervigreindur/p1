package prog1;

import java.util.*;

import org.omg.CORBA.Environment;


class Graph
{
	 private int V; 
	 private LinkedList<Integer> adj[];
	 private boolean goals[];
	 private int parent[];
	 public boolean visited[] = new boolean[V];
	 private Map<Pair, State> envi;
	 private ArrayList<Pair> keys;
	 private Pair size;
	 Graph(int v, Map<Pair, State> environment, Pair size, ArrayList<Pair> keys)
	 {
	     V = v;
	     adj = new LinkedList[v];
	     goals = new boolean[v];
	     parent = new int[v];
	     for (int i=0; i<v; ++i)
	     {
	    	 adj[i] = new LinkedList();
	         parent[i] = -1;
	     }
	     this.envi = environment;
	     this.keys = keys;
	     this.size = size;
	     
	     for(Map.Entry<Pair, State> env : environment.entrySet()) 
		 {
			int block = CoordsAndInts.coordinatesToInt(env.getKey().getX(), env.getKey().getY(), size);
			
			//System.out.println(block);
			if(!env.getValue().isObsticle())
			{
				if(env.getValue().isEast())
				{
					addEdge(block, block+1);
				}
				if(env.getValue().isWest())
				{
					addEdge(block, block-1);
				}
				if(env.getValue().isNorth())
				{
					addEdge(block, block + size.getX());
				}
				if(env.getValue().isSouth())
				{
					addEdge(block, block - size.getX());
				}
			}
		 }
	 }
	
	 void addEdge(int v,int w)
	 {
	     adj[v].add(w);
	 }
	
	 Stack<Integer> BFS(int s)
	 {
		 LinkedList<Integer> queue = new LinkedList<Integer>();
		 boolean visited[] = new boolean[V];
		 visited[s]=true;
		 queue.add(s);
		 
		 while (!queue.isEmpty())
		 {
			 s = queue.poll();
			 System.out.print(s+" ");
			 
			 if(envi.get(keys.get(s)).isDirt())
			 {
				 envi.get(keys.get(s)).setDirt(false);
				 Stack<Integer> path = new Stack<Integer>();
				 path.add(s);
		        	 
				 while(parent[s] != -1) 
				 {
					 path.add(parent[s]);
					 s = parent[s];
				 }
		        	 
				 //System.out.println("\n" + path);
				 return path;
			 }
			 Iterator<Integer> i = adj[s].listIterator();
			 while (i.hasNext())
			 {
				 int n = i.next();
		         
				 if (!visited[n])
				 {
					 parent[n] = s;
					 visited[n] = true;
					 queue.add(n);
		         }
			 }
		 }
		 return null;
	 }

	 Stack<Integer> DFS(int s)
	 {
		 boolean visited[] = new boolean[V];
		 LinkedList<Integer> stack = new LinkedList<Integer>();
		 
			
		 visited[s]=true;		    
		 stack.push(s);
		 
		 while (!stack.isEmpty())
		 {
			 s = stack.poll();
			 if(goals[s])
			 {
				 Stack<Integer> path = new Stack<Integer>();
				 
				 path.add(s);
		        	 
				 while(parent[s] != -1) 
				 {
					 path.add(parent[s]);
					 s = parent[s];
				 }
		        	 
				 //System.out.println("\n" + path);
				 return path;
			 }
			 Iterator<Integer> i = adj[s].listIterator();
			 while (i.hasNext())
			 {
				 int n = i.next();
				 if(!visited[n])
				 {
					 visited[n] = true;
					 System.out.println(n);
					 stack.push(n);
				 }
			 }
		 }
		 return null;
	 }
	 
}

