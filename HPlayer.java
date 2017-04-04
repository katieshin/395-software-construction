class HPlayer extends Player {

    HPlayer(String name) { super(name); }
    
    // ------------------------------------------------------------------

    // a medium for communication to/with player
    private IHDisplay io;

    // register a Display area 
    public void registerDisplay(IDisplay d) {
		this.io = (IHDisplay)d; 
    }

    // set an action button's enabled state
    public void setActionEnabled(Turn.Action action, boolean enabled) {
    	this.io.setActionEnabled(action, enabled);
    }

    // receive information from server 
    public void inform(String msg) {
		io.msgDisplay(msg); 
    }

    // Java can only close over fields, not local variables
    private Turn turn; 
    private boolean done = false; 

    // enable the human player to take a turn 
    public void turn(Turn t) {
		this.turn = t;

		// interact with player:
		io.msgDisplay("It's your turn!"); 	
		io.listen
			(new IListener () {
				public void done() {
					turn.hold();
				}
				public int roll() {
					int r = turn.roll();
					sum += r; 
					return r;
				}
				public void skip() {
					turn.skip();
				}
				public int roll2x() {
					int r = turn.rollTwice();
					sum += r;
					return r;
				}
			});
		// end of interaction 

		io.sumDisplay(sum);
		if (sum <= 21)
			io.msgDisplay("Turn's up.");
		else {
			io.msgDisplay("Oops, lost this one.");
		};
    }

    // ------------------------------------------------------------------
    // Examples: 

    static HPlayer h1;
    static SPlayer s1;
    static Turn t1;
    static Turn t2;  
    
    public static void createExamples() {
		if (h1 == null) {
		    h1 = new HPlayer("test1");    
		    s1 = new SPlayer(h1);         
		    t1 = new Turn(s1, new Die()); 
		    t2 = new Turn(s1, new Die());
		}
    }

    // ------------------------------------------------------------------
    // Tests: 

    public static void main(String argv[]) {
		h1.registerDisplay(TestIHDisplay.testIDisplay); 

		h1.inform("welcome to human player 1");
		
		h1.turn(t1);
		h1.turn(t1); 
		Tester.check(s1.done, "human cheat"); 
		
		h1.turn(t2); 

		// cheating wasn't caught
		s1.sum = 22;
		h1.turn(t2);
		Tester.check(s1.done, "human cheat 3"); 
		Tester.check(s1.sum == 0, "human cheat 4"); 
    }

}
