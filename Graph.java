package prog1;

import java.util.*;


class Graph
{
	 private int V; 
	 private LinkedList<Integer> adj[];
	 private boolean goals[];
	 private int parent[];
	 public boolean visited[] = new boolean[V];
	
	 Graph(int v, boolean goal[])
	 {
	     V = v;
	     adj = new LinkedList[v];
	     goals = new boolean[v];
	     parent = new int[v];
	     for (int i=0; i<v; ++i)
	     {
	    	 adj[i] = new LinkedList();
	         goals[i] = goal[i];
	         parent[i] = -1;
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

