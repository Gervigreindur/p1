package prog1;

public class Pair {
	
	private int x;
    private int y;

    public Pair(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() 
    {
    	return this.x;
    }
    
    public int getY()
    {
    	return this.y;
    }
    
    public Pair getPair()
    {
    	return this;
    }
    public void setPair(int x, int y)
    {
    	this.x = x;
        this.y = y;
    }

}
