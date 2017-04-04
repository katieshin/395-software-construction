interface ITurn {
    // player rolls the die for this turn, record the result with state
    int /* 1 .. 6 */ roll();

    // player chooses to hold their current score for the remainder of the game
    void hold();

    // player chooses to roll the die twice and sum the results
    int rollTwice();

    // player skips this turn
    void skip();
}
