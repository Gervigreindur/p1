package prog1;

import java.util.*;
import java.util.Comparator;
import org.omg.CORBA.Environment;


class Graph
{
	 private int V; 
	 private LinkedList<LinkedList<Integer>> adj;
	 private int parent[];
	 public boolean visited[] = new boolean[V];
	 private Map<Pair, State> envi;
	 private ArrayList<Pair> keys;
	 private Pair size;
	 Comparator<Integer> order = Integer::compare;
	 Graph(int v, Map<Pair, State> environment, Pair size, ArrayList<Pair> keys)
	 {
	     V = v;
	     adj = new LinkedList<LinkedList<Integer>>();
	     parent = new int[v];
	     for (int i=0; i<v; ++i)
	     {
	    	 adj.add(new LinkedList<Integer>());
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
	     for(LinkedList<Integer> adjacent : adj)
	     {
	    	adjacent.sort(order);
	    	//System.out.println(adjacent);
	     }
	 }
	
	 void addEdge(int v,int w)
	 {
	     adj.get(v).add(w);
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
			 Iterator<Integer> i = adj.get(s).listIterator();
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
		 Stack<Integer> stack = new Stack<Integer>();
		 visited[s]=true;		    
		 stack.push(s);
		 
		 while (!stack.isEmpty())
		 {
			 s = stack.pop();
			 
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
			 Iterator<Integer> i = adj.get(s).listIterator();
			 while (i.hasNext())
			 {
				 if(envi.get(keys.get(s)).isDirt())
				 {
					 envi.get(keys.get(s)).setDirt(false);
					 //System.out.println(path);
					 stack.sort(order.reversed());
					 return stack;
				 }
				 int n = i.next();
				 if(!visited[n])
				 {
					 parent[n] = s;
					 visited[n] = true;
					 System.out.println(n);
					 stack.push(n);
				 }
			 }
		 }
		 return null;
	 }
}

