abstract class Player implements IPlayer {

    protected String name; 
    protected int sum = 0; 

    Player(String name) { this.name = name; }
    
    // ------------------------------------------------------------------

    // return name
    public String name() { return this.name; }
    public int sum() { return this.sum; }

    abstract public void inform(String msg); 

    abstract public void turn(Turn t);
}

