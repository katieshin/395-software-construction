interface ITurn {
    // player rolls the die for this turn, record the result with state 
    int /* 1 .. 6 */ roll();

	// whether the turn was skipped
	boolean skipped = false;

    // player skips this turn 
    void skip(); 
}
