interface IPlayer {
    // the name of the player
    String name(); 

    // provide a display area for i/o
    void registerDisplay(IDisplay d);

    // it's your turn, choose an action to take using/during it
    void turn(Turn t); 

    // you've got mail 
    void inform(String m); 
}
