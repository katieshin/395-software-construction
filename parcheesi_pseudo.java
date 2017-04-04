//each player has 4 pawns, 2 dice rolls, and an array for which pawns finished
private class Player {

	//safe square posn: 1, 6, 11, 18, 23, 28, 35, 40, 45, 52, 57, 62, 69, 74-82
	//pawnPosn[x][y][z] : x indicates posn(square num), y indicates whether it's safe or not
	//						z indicates whether a blockade is formed
	private int[4][3] pawnPosn; //82 squares (including home and start point)	
	private boolean[4] pawnFinish;
	private int[2] diceRoll;
	private int doubleRoll; //number of double rolls
}

//set green starting point as square 0
public class Play {
	//private Player[4]; //green yellow blue red
	//assume there is only 1 player

	private void rollDie() {
		diceRoll[0] = (int)(Math.random()*6 + 1);
		diceRoll[1] = (int)(Math.random()*6 + 1);
	}

	public void addRollDie() {
		if (diceRoll[0] == 5 || diceRoll[1] == 5
				|| diceRoll[0] + diceRoll[1] == 5) { //either of the die is 5. or sum == 5
			for (int i = 0; i < 4; i++) {
				if (pawnPosn[i][0][0] == 0) {
					pawnPosn[i][0][0] = diceRoll[0] + diceRoll[1] - 5; //set pawn posn
				}
				else {
					//add dice rolls to random pawns
				}
			}
		}
		else { //neither of the die are 5
			//add dice rolls to random pawns
		}
	}

	public void doubleDieRoll() {
		doubleRoll = 0;

		if (doubleRoll < 3) {
			if (diceRoll[0] == diceRoll[1]) {
				if (pawnPosn[0][0][0] != 0 &&
					pawnPosn[1][0][0] != 0 &&
					pawnPosn[2][0][0] != 0 &&
					pawnPosn[3][0][0] != 0) {
					//add diceRoll[0] twice and 7-diceRoll[0] twice
				}
				doubleRoll++;
			} 
		}
		else if (doubleRoll == 3) {
			int farthest;
			for (int i = 1; i < 4; i++) {
				if (pawnPosn[i][0][0] > pawnPosn[0][0][0]) {
					farthest = i;
				}
				else {
					farthest = 0;
				}
			}
			//if roll 3 doubles in a row then set the farthest pawn back to starting pt
			pawnPosn[farthest][0][0] = 0;
		}
		doubleRoll = 0;
	}

	//2 pawns of the same color in one square prevents other pawns from passing (even same color)
	public void blockades() {
		int posn0 = pawnPosn[0][0][0];
		int posn1 = pawnPosn[1][0][0];
		int posn2 = pawnPosn[2][0][0];
		int posn3 = pawnPosn[3][0][0];

		//there must be a better way of doing this
		if (posn0 == posn1)
			pawnPosn[0][0][1] = pawnPosn[1][0][1] = 1;
		else if (posn0 == posn2)
			pawnPosn[0][0][1] = pawnPosn[2][0][1] = 1;
		else if (posn0 == posn3)
			pawnPosn[0][0][1] = pawnPosn[3][0][1] = 1;
		else if (posn1 == posn2)
			pawnPosn[1][0][1] = pawnPosn[2][0][1] = 1;
		else if (posn1 == posn3)
			pawnPosn[1][0][1] = pawnPosn[3][0][1] = 1;
		else if (posn2 == posn3)
			pawnPosn[2][0][1] = pawnPosn[3][0][1] = 1;
	}

	//pawn can bop a different color pawn if it lands on the same square.
	//the player who bopped another player can move +20
	public void bop() {

	}

	//purple squares
	//other color pawns can pass but not stop at a purple square if there is already a diff color pawn
	public void safety() {
		for (int i = 0; i < 4; i++) {
			if (pawnPosn[0][0][0] == /*1, 6, 11, 18, 23, 28, 35, 40, 45, 52, 57, 62, 69, 74 */) {
				pawnPosn[0][1][0] = 1; //posn is safe
			}
		}
	}

	public void getToHome() {
		for (int i = 0; i < 4; i++) {
			if (pawnPosn[i][0][0] == /* 75 - 82 */) {
				rollDie();
				if ((diceRoll[0] + pawnPosn[i][0][0] != 82) ||
					(diceRoll[1] + pawnPosn[i][0][0] != 82)
					//add dice roll to another random pawn
				else {
					pawnFinish[i] = true;
					//add 10 to another random pawn
				}
			}
		}
	}
}