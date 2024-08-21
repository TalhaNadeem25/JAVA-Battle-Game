package BattleJava;
import java.util.Scanner;
import java.util.Random; 

/**
* Board description:
* This class contains the necessary information and methods for a single player to participate in a multi-player battleship game.
* @author Blaise Kruppa
* @author Talha Nadeem
* @author Jesus Medina
* @author Abdul Muqeet Taahaa
* @version v 1.0 04/21/2024
*/
public class Board {
	//indicates the size of a player's board
	//size x size
	int size;
	//Stores the amount of ships a player has left on their board.
	int ships;
	//Stores the location of all ships a player possesses.
	char[][] hidden;
	//Stores the hit/miss grid that can be displayed to other players.
	char[][] display;
	
	/**
	* A method that fires a shot at a player's board.
	* @param location
	* The firing location in the form "A1"
	* @return
	* The response to this move.
	* can be "miss", "hit" etc.
	*/
	public String FIRE(String location) {
		if (location.length() != 2) {
			return "invalid location";
		}
		int row;
		switch (location.charAt(0)) {
			case 'A': row = 0; break;
			case 'B': row = 1; break;
			case 'C': row = 2; break;
			case 'D': row = 3; break;
			case 'E': row = 4; break;
			case 'F': row = 5; break;
			case 'G': row = 6; break;
			case 'H': row = 7; break;
			default: return "Invalid location";
		}
		int column = Character.getNumericValue(location.charAt(1)) - 1;
		if (column > 7 || column < 0) {
			return "Invalid location, try again";
		}
		if (this.display[row][column] != '.') {
			return "Already fired, try again";
		}
		else if (this.hidden[row][column] == 'S') {
			this.display[row][column] = 'X';
			if (this.isSink(row, column)) return "Hit and Sink";
			return "Hit";
		}
		this.display[row][column] = 'O';
		return "Miss";
	}
	
	//A method that fills a players board with the '.' char to increase readability.
	private void initializeBoard() {
        display = new char[size][size];
        hidden = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                display[i][j] = '.';
                hidden[i][j] = '.';
            }
        }
    }
	
	//A method that randomly places ships of length 3 on the players board.
    public void placeShipsRandomly() {
        Random random = new Random();
        int shipsToPlace = ships;

        while (shipsToPlace > 0) {
            int shipSize = 3; // Assuming ship size is 3 for example
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            boolean isHorizontal = random.nextBoolean();

            // Check if the ship can be placed in the randomly chosen position
            if (canPlaceShip(row, col, shipSize, isHorizontal)) {
                // Place the ship
                if (isHorizontal) {
                    for (int c = col; c < col + shipSize; c++) {
                        hidden[row][c] = 'S';
                    }
                } else {
                    for (int r = row; r < row + shipSize; r++) {
                        hidden[r][col] = 'S';
                    }
                }
                shipsToPlace--;
            }
        }
    }

    /**
    * A method that determines if a location is valid for a ship to be placed.
    * @param startRow
    * The row index for the first ship space.
    * @param startCol
    * The column index for the first ship space.
    * @param shipSize
    * The length of the ship.
    * @param isHorozontal
    * @return
    * A boolean that indicates whether the position is valid.
    */
    private boolean canPlaceShip(int startRow, int startCol, int shipSize, boolean isHorizontal) {
        if (isHorizontal) {
            if (startCol + shipSize > size) {
                return false; // Ship exceeds board boundary
            }
            for (int c = startCol; c < startCol + shipSize; c++) {
                if (hidden[startRow][c] == 'S') {
                    return false; // Overlaps with another ship
                }
                if (!this.checkAdjacent(startRow, c)) {
                	return false;
                }
            }
        } else {
            if (startRow + shipSize > size) {
                return false; // Ship exceeds board boundary
            }
            for (int r = startRow; r < startRow + shipSize; r++) {
                if (hidden[r][startCol] == 'S') {
                    return false; // Overlaps with another ship
                }
                if (!this.checkAdjacent(r, startCol)) {
                	return false;
                }

            }
        }
        return true;
    }
    
    /**
    * A method that determines if adjacent tiles contain ships
    * @param row
    * The row index for the tile being checked.
    * @param column
    * The column index for the tile being checked.
    * @return
    * A boolean that is true if no adjacent tiles contain ships.
    */
    public boolean checkAdjacent(int row, int column) {
    	if (row >= 1) {
    		if (hidden[row-1][column] == 'S') return false;
    	}
    	if (row < (size - 1)) {
    		if (hidden[row+1][column] == 'S') return false;
    	}
    	if (column >= 1) {
    		if (hidden[row][column-1] == 'S') return false;
    	}
    	if (column < (size - 1)) {
    		if (hidden[row][column+1] == 'S') return false;
    	}
    	return true;
    }
    
    //A method that displays a player's public/response board.
    public void displayBoard() {
        System.out.print(" ");
        for (int col = 0; col < size; col++) {
            System.out.printf("%2d", (col + 1));
        }
        System.out.println();
        for (int row = 0; row < size; row++) {
            char colChar = (char) ('A' + row);
            System.out.print(colChar + " ");
            for (int col = 0; col < size; col++) {
                System.out.print(display[row][col] + " ");
            }
            System.out.println();
        }
    }
    
     /**
     * A method that determines whether a hit sinks a ship.
     * @param row
     * The row index for the original hit.
     * @param y
     * The column index for the original hit
     * @return
     * A true-false bool indicating whether a ship was sunk.
     */
    public Boolean isSink(int row, int column) {
    	Boolean vertical = false;
    	if (row == 0) {if(hidden[row+1][column] == 'S') {vertical = true;}}
    	else if (row == (size-1)) {if (hidden[row-1][column] == 'S') {vertical = true;}}
    	else if (hidden[row-1][column] == 'S' || hidden[row+1][column] == 'S') {vertical = true;}
    	if(vertical) {
    		while(hidden[row][column] == 'S') {
    			if (row == 0) break;
    			row--;
    		}
    		if (hidden[row][column] != 'S') {row++;}
    		while(hidden[row][column] == 'S') {
    			if (display[row][column] != 'X') return false;
    			row++;
    			if (row >= size) break;
    		}
    		ships--;
    		return true;
    	}
    	while(hidden[row][column] == 'S') {
   			if (column == 0) break;
   			column--;
   		}
		if (hidden[row][column] != 'S') {column++;}
    	while(hidden[row][column] == 'S') {
    		if (display[row][column] != 'X') return false;
    		column++;
    		if (column >= size) break;
    	}
    	ships--;
    	return true;
    }
    
    
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
		int boardSize = 0;
		// Determining preliminary information: board size, ship count, players
		while(boardSize == 0) {
			System.out.printf("Select board size using key-number\n(1) Small Board 6x6 3 Ships\n(2) Medium Board 8x8 4 Ships\n(3) Classic Board 10x10 5 Ships\n");
        	int choice = sc.nextInt();
        	switch(choice) {
        	case 1: boardSize = 6; break;
        	case 2: boardSize = 8; break;
        	case 3: boardSize = 10; break;
        	default: System.out.println("Invalid key, choose again");
        	}
		}
		String response = "123";
		System.out.println("Select number of players (2-4): ");
		int numOfPlayers = sc.nextInt();
		while (numOfPlayers > 4 || numOfPlayers < 2) {
			System.out.println("Invalid number of players");
			System.out.println("Select number of players (2-4): ");
			numOfPlayers = sc.nextInt();
		}
		Boolean turnSelector = (numOfPlayers > 2)? true:false;

		//Creating a Board object for each player and initializing their boards
		Board[] player = new Board[numOfPlayers];
		for (int i = 0; i < numOfPlayers; i++) {
			player[i] = new Board();
			player[i].size = boardSize;
			player[i].initializeBoard();
			player[i].ships = boardSize / 2;
		}
		//Placing ships
		for (Board turns : player) {
            turns.placeShipsRandomly();
        }
		// Game Starts with Player1
		// Repeat until game end
		GAMESTATE: while(true) {
			int opponent;
			TURN: for (int i = 0; i < numOfPlayers; i++) {
				if (player[i].ships == 0) {System.out.println("Player " + (i+1) + " has no remaining ships so their turn will be skipped.");break TURN;}
				System.out.println("Player " + (i+1) + "'s turn");
				//Selecting the opponent (3 or more players only)
				if (turnSelector) {
					System.out.println("Enter opponent: ");
					opponent = sc.nextInt();
					opponent--;
					while (opponent == i || opponent >= numOfPlayers || opponent < 0 || player[opponent].ships == 0) {
						System.out.println("Invalid opponent");
						System.out.println("Enter opponent: ");
						opponent = sc.nextInt();
						opponent--;}
					System.out.println("Player " + (opponent+1) + "'s board:");
				}
				else {opponent = (i==0)? 1:0;}
				player[opponent].displayBoard();
				//Making move
				do {if (response.equals("Hit") || response.equals("Hit and Sink")) player[opponent].displayBoard();
					System.out.print("Enter move: ");
					String move = sc.next();
					response = player[opponent].FIRE(move);
					System.out.println(response);
					// GameState check
					short count = 0;
					for(int j = 0; j < numOfPlayers; j++) {
						if(player[j].ships > 0) count++;}
					if(count <= 1) {
						break GAMESTATE;}
				}
				//Give another turn if the move was a hit
				while(!response.equals("Miss"));
			}
		}
		sc.close();
		int winner = 0;
		for(int i = 0; i < numOfPlayers; i++) {
			if (player[i].size != 0) {winner = i; break;}
		}
		System.out.println("Congratulations!! Player " + (winner+1) + " wins!!!");
	}
}