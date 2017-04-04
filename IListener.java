interface IListener {
    // the play rolls the die
    int roll(); 

    // the player chooses to skip this turn
    void skip();

    // the player stops playing with the current score
    void done(); 

    // the player chooses to roll the die twice
    int roll2x();
}
