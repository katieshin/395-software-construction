import java.util.*;

class Server implements IServer {

    // singleton pattern: 
    public static Server server = new Server(); 
    private Server() {}

    // ------------------------------------------------------------

    private Vector /* SPlayer */ all_players = new Vector(); 

    private int current = 0;
    private int how_many = 0; 
    private Turn[] lastRound;
    private boolean isFirstRound = true;

    private Die d = new Die(); 


    // add a player to the game 
    public void register(IPlayer p) {
        how_many += 1;
        all_players.add(new SPlayer(p));
    }

    // play the game until all players are done, announce winner 
    public String play() {
        lastRound = new Turn[how_many];

        while (aPlayerGoing() && (isFirstRound || !allPlayersSkipped())) {
            for(int i = 0; i < how_many; i++) {
                SPlayer p = (SPlayer)all_players.elementAt(i); 

                System.out.println("Player " + i + " is a " + (p.getPlayer() instanceof HPlayer ? "Human" : "Machine"));
                System.out.println("Are all other players done? " + allPlayersDoneExcept(p));
                if ((p.getPlayer() instanceof HPlayer) && allPlayersDoneExcept(p)) {
                    p.inform("All other players are done, so you may no longer skip.");
                    HPlayer hp = (HPlayer)p.getPlayer();
                    hp.setActionEnabled(Turn.Action.SKIP, false);
                }

                Turn playerTurn = new Turn(p, d);
                lastRound[i] = playerTurn;
                if (!p.done)
                    p.turn(playerTurn);

                System.out.println("Player " + i + " done = " + (p.done ? "true" : "false"));
            }
            isFirstRound = false;
        }

        return winner(); 
    }

    // ------------------------------------------------------------

    // or map over the done state of all players
    private boolean aPlayerGoing() {
        for(int i = 0; i < how_many; i++) {
            if (!((SPlayer)all_players.elementAt(i)).done) {
                // System.out.println("still going " + i);
                return true; 
            }
        }

        return false; 
    }

    private boolean allPlayersSkipped() {
        boolean allSkipped = true;

        for(int i = 0; i < how_many; i++) {
            Turn t = lastRound[i];
            allSkipped &= (t.actionTaken == Turn.Action.SKIP);
        }

        return allSkipped;
    }

    private boolean allPlayersDoneExcept(SPlayer p) {
        boolean allDone = true;

        for(int i = 0; i < how_many; i++) {
            SPlayer currentPlayer = (SPlayer)all_players.elementAt(i);
            if (currentPlayer != p) {
                allDone &= currentPlayer.done;
            }
        }

        return allDone;
    }

    private static String newline = "\n"; 

    // compose the announcement of winners
    // effect: inform winners
    private String winner() {
        int max = max_score(); 
        String announcement = ""; 
        
        for(int i = 0; i < how_many; i++) {
            SPlayer s = (SPlayer)all_players.elementAt(i);
            if (max == s.sum) {
                s.inform("You're a winner.");
                announcement = 
                    announcement +
                    newline + 
                    s.name() + 
                    " is a winner " +
                    " with a score of " + max;
            }
        }

        return announcement; 
    }

    // compute the maximum score for all players
    private int max_score() {
        int max = 0; 
        
        for(int i = 0; i < how_many; i++) {
            int s = ((SPlayer)all_players.elementAt(i)).sum;
            if (s > max) 
                max = s; 
        }

        return max;
    }

    // ------------------------------------------------------------------
    // Examples: 

    static MPlayer m1;
    static MPlayer m2;

    static public void createExamples() {
        if (m1 == null) {
            m1 = new MPlayer("test1"); 
            m2 = new MPlayer("test2"); 
        }
    }

    // ------------------------------------------------------------------
    // Tests: 

    public static void main(String argv[]) {
        createExamples(); 

        m1.registerDisplay(TestIDisplay.testIDisplay); 
        m2.registerDisplay(TestIDisplay.testIDisplay); 

        server.register(m1); 
        server.register(m2); 
        Tester.check(2 == server.how_many,"register 1"); 
        Tester.check(server.aPlayerGoing(),"register 1"); 

        boolean done0 = test_turn(server.all_players.elementAt(0));
        boolean done1 = test_turn(server.all_players.elementAt(1));
        Tester.check(!done0 && !done1,"one turn");
        Tester.check(server.aPlayerGoing(),"a player going");

        while (true) {
            done0 = test_turn(server.all_players.elementAt(0));
            done1 = test_turn(server.all_players.elementAt(1));
            if (done0 && done1) 
                break;
        }

        Tester.check(!server.aPlayerGoing(),"a player going, done");
        
        int score0 = ((SPlayer)server.all_players.elementAt(0)).sum;
        int score1 = ((SPlayer)server.all_players.elementAt(1)).sum;
        
        String w = server.winner(); 

        if (score0 > score1) {
            Tester.check(server.max_score() == score0,"max score 1"); 
            Tester.check(w.charAt(5) == '1',"winner 1");
        }
        else if (score1 > score0) {
            Tester.check(server.max_score() == score1,"max score 2"); 
            Tester.check(w.charAt(5) == '2',"winner 2");
        }

        // this alters a lot of state; be careful with the tests and their
        // ordering
        server.lastRound = new Turn[server.how_many];
        for (int i = 0; i < server.how_many; i++){ 
            SPlayer p = (SPlayer)server.all_players.elementAt(i); 
            Turn t = new Turn(p, server.d);
            // force all players to look like they skipped their turn
            t.skip();
            server.lastRound[i] = t;
        }
        Tester.check(server.allPlayersSkipped(), "all skip 1");

        // this test also alters a lot of state. We should have some kind of
        // "reset state" method we call between tests.
        // TODO: test reset state method.
        for (int i = 0; i < server.how_many; i++) {
            SPlayer p = (SPlayer)server.all_players.elementAt(i);
            p.done = true;
        }
        SPlayer p1 = (SPlayer)server.all_players.elementAt(0);
        Tester.check(server.allPlayersDoneExcept(p1), "all players done 1");
        p1.done = false;
        Tester.check(server.allPlayersDoneExcept(p1), "all players done 2");
        SPlayer p2 = (SPlayer)server.all_players.elementAt(1);
        Tester.check(!server.allPlayersDoneExcept(p2), "all players done 3");
    }

    private static boolean test_turn(Object s) {
        SPlayer sp = (SPlayer)s;

        return sp.turn(new Turn(sp,server.d)); 
    }

}
