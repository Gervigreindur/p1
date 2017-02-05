package prog1;
//Java program to print BFS traversal from a given source vertex.
//BFS(int s) traverses vertices reachable from s.

/* Bygg�um ofan � �tf�rslu fengna fr� GeeksforGeeks.org
 * http://www.geeksforgeeks.org/breadth-first-traversal-for-a-graph/
 */
import java.io.*;
import java.util.*;


//This class represents a directed graph using adjacency list
//representation
class Graph
{
 private int V;   // No. of vertices
 private LinkedList<Integer> adj[]; //Adjacency Lists
 private boolean goals[];
 private int parent[];

 // Constructor
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

 // Function to add an edge into the graph
 void addEdge(int v,int w)
 {
     adj[v].add(w);
 }

 // prints BFS traversal from a given source s
Stack<Integer> BFS(int s)
 {
     // Mark all the vertices as not visited(By default
     // set as false)
     boolean visited[] = new boolean[V];

     // Create a queue for BFS
     LinkedList<Integer> queue = new LinkedList<Integer>();

     // Mark the current node as visited and enqueue it
     visited[s]=true;
     queue.add(s);

     while (queue.size() != 0)
     {
         // Dequeue a vertex from queue and print it
         s = queue.poll();
         System.out.print(s+" ");
         if(goals[s])
         {
        	 Stack<Integer> path = new Stack<Integer>();
        	 
        	 path.add(s);
        	 
        	 while(parent[s] != -1) {
        		 path.add(parent[s]);
        		 s = parent[s];
        		 //System.out.println("2n " + n);
        		 //System.out.println("2parent " + parent[n]);
        		
        	 }
        	 
        	 System.out.println("\n" + path);
        	 return path;
         }

         // Get all adjacent vertices of the dequeued vertex s
         // If a adjacent has not been visited, then mark it
         // visited and enqueue it
         Iterator<Integer> i = adj[s].listIterator();
         while (i.hasNext())
         {
             int n = i.next();
             
             /*System.out.println("n " + n);
    		 System.out.println("parent " + parent[n]);
             System.out.println("parent af 0 " + parent[0]);*/
             
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

 // Driver method to
 public static void main(String args[])
 {/*
     Graph g = new Graph(4);

     g.addEdge(0, 1);
     g.addEdge(0, 2);
     g.addEdge(1, 2);
     g.addEdge(2, 0);
     g.addEdge(2, 3);
     g.addEdge(3, 3);

     System.out.println("Following is Breadth First Traversal "+
                        "(starting from vertex 2)");

     g.BFS(2);
     */
 }

public int V() {
	// TODO Auto-generated method stub
	return 0;
}
}
//This code is contributed by Aakash Hasija
