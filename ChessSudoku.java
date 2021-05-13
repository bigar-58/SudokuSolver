package finalproject;

import java.util.*;
import java.io.*;


public class ChessSudoku {
	/* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For
	 * a standard Sudoku puzzle, SIZE is 3 and N is 9.
	 */
	public int SIZE, N;

	/* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
	 * not yet been revealed are stored as 0.
	 */
	public int grid[][];

	/* Booleans indicating whether of not one or more of the chess rules should be
	 * applied to this Sudoku.
	 */
	public boolean knightRule;
	public boolean kingRule;
	public boolean queenRule;


	// Field that stores the same Sudoku puzzle solved in all possible ways
	public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();

	private boolean checkRow(int row, int column, int val) {
		//iterate through the elements in the current row
		for (int index = 0; index < this.N; index++) {
			//if the number appears elsewhere in the current row than it can't be a valid entry
			if (this.grid[row][index] == val && index != column) {
				return false;
			}
		}

		//the entry has passed the row test for sudoku
		return true;
	}

	private boolean checkCol(int row, int column, int val) {
		//iterate through the elements of the current column
		for (int index = 0; index < this.N; index++) {
			//if the number appears in the column more than once than it's not a valid entry
			if (this.grid[index][column] == val && index != row) {
				return false;
			}
		}

		//if the function gets to this point then it has passed the column test for sudoku
		return true;
	}

	private boolean checkBox(int row, int column, int val) {
		int curr_row = row - (row % this.SIZE); 			// first row of the current grid
		int curr_column = column - (column % this.SIZE); 	// first column of the current grid


		//iterate through each row and column in the current subgrid
		for (int cr = curr_row; cr < curr_row + this.SIZE; cr++) {

			for (int cc = curr_column; cc < curr_column + this.SIZE; cc++) {

				//if the value is equal to the value at the current position it should return false
				if (this.grid[cr][cc] == val && cr != row && cc != column) {
					return false;
				}
			}
		}

		return true;
	}


	private boolean checkKnight(int row, int column, int val) {


		//check for all of the possible knight moves that can be made by the current entry value

		if (row - 2 >= 0 && column - 1 >= 0 && val == this.grid[row - 2][column - 1]) {
			return false; // if it is equal the entry is invalid
		}
		if (row - 2 >= 0 && column + 1 < this.N && val == this.grid[row - 2][column + 1]) {
			return false; // if it is equal the entry is invalid
		}

		if (row - 1 >= 0 && column - 2 >= 0 && val == this.grid[row - 1][column - 2]) {
			return false; // if it is equal the entry is invalid
		}

		if (row - 1 >= 0 && column + 2 < this.N && val == this.grid[row - 1][column + 2]) {
			return false; // if it is equal the entry is invalid
		}

		if (row + 1 < this.N && column - 2 >= 0 && val == this.grid[row + 1][column - 2]) {
			return false; // if it is equal the entry is invalid
		}

		if (row + 1 < this.N && column + 2 < this.N && val == this.grid[row + 1][column + 2]) {
			return false; // if it is equal the entry is invalid
		}

		if (row + 2 < this.N && column - 1 >= 0 && val == this.grid[row + 2][column - 1]) {
			return false; // if it is equal the entry is invalid
		}

		if (row + 2 < this.N && column + 1 < this.N && val == this.grid[row + 2][column + 1]) {
			return false; // if it is equal the entry is invalid
		}
		else
			return true;


	}


	private boolean checkKing(int row, int column, int val) {
		//can maybe skip checking columns as its done earlier anyway

		//check the corner to the upper left of the entry
		if (row - 1 >= 0 && column - 1 >= 0 && val == this.grid[row - 1][column - 1]) {
			return false; // if it is equal the entry is invalid
		}

		//check the upper right corner of the entry
		if (row - 1 >= 0 && column + 1 < this.N && val == this.grid[row - 1][column + 1]) {
			return false;
		}

		//check the lower left corner of the entry
		if (row + 1 < this.N && column - 1 >= 0 && val == this.grid[row + 1][column - 1]) {
			return false;
		}

		//check the lower right corner of the entry
		if (row + 1 < this.N && column + 1 < this.N && val == this.grid[row + 1][column + 1]) {
			return false;
		}

		//if the entry passes all of those tests it has passed the kings rule for chess sudoku
		return true;


	}

	private boolean checkQueen(int row, int column, int val) {

		if (val == this.N) {
			for (int i = 1; i < this.N; i++) {
				//check the corner to the upper left of the entry
				if (row - i >= 0 && column - i >= 0 && val == this.grid[row - i][column - i]) {
					return false; // if it is equal the entry is invalid
				}

				//check the upper right corner of the entry
				if (row - i >= 0 && column + i < this.N && val == this.grid[row - i][column + i]) {
					return false;
				}

				//check the lower left corner of the entry
				if (row + i < this.N && column - i >= 0 && val == this.grid[row + i][column - i]) {
					return false;
				}

				//check the lower right corner of the entry
				if (row + i < this.N && column + i < this.N && val == this.grid[row + i][column + i]) {
					return false;
				}
			}
			return true;
		}

		return false;

	}


	private boolean isValidEntry(int row, int column, int val) {

		//check if the current value passes the basic sudoku rules
		//check the conditions individually to minimize the amount of for loops necessary
		boolean passRowTest = this.checkRow(row, column, val);
		if (!passRowTest){
			return false;
		}

		boolean passColTest = this.checkCol(row, column, val);

		if (!passColTest){
			return false;
		}

		boolean passBoxTest = this.checkBox(row, column, val);

		if (!passBoxTest){
			return false;
		}



		//if the value passes all of the basic tests it should check if any of the chess rules are active
		if (passRowTest && passColTest && passBoxTest) {

			//if the king rule is applied, it should check if the the value is valid
			if (this.kingRule && !this.checkKing(row, column, val)) {
				return false;
			}


			//if the knight rule is applied to the puzzle the knight rule should be checked for the current value
			if (this.knightRule && !this.checkKnight(row, column, val)) {
				return false;
			}

			//if the queen rule is applied to the puzzle the queen rule should be check for the current value
			if (val == this.N && this.queenRule && !this.checkQueen(row, column, val)) {
				return false;
			}

			return true;
		}

		return false;
	}

	private int[] findAllEntries(int row, int column){
		int[] validEntries = new int[this.N];
		int numEntries = 0;

		for(int i=1; i< this.N; i++){
			if(this.isValidEntry(row, column, i)){
				validEntries[numEntries] = i;
			}
		}

		return validEntries;
	}





	private boolean solveSudoku(boolean allSolutions){

		//iterate through the grid to search for 0s
		for (int row = 0; row < this.N; row++) {

			for (int col = 0; col < this.N; col++) {

				if (this.grid[row][col] == 0) {

						//iterate through the possible values
						for (int val = 1; val <= this.N; val++) {


							//check if the value is valid for the puzzle
							if (this.isValidEntry(row, col, val)) {
								this.grid[row][col] = val; // if it is, apply it to this grid


							} else {
								continue;
							}

							//recursively call solveSudoku on this grid with new entry and check if it can be solved
							if (this.solveSudoku(allSolutions) == true) {
								solutions.add(this);
								return true;
							} else {
								this.grid[row][col] = 0; // if it can't replace the cell with a 0 and try the next value
								continue;
							}

					}
					return false; // if the board is able to get to this point it cannot be solved
				}
			}
		}

		return true; // if the board passes every cell in the grid it has yielded a valid solution
	}

	/* The solve() method should remove all the unknown characters ('x') in the grid
	 * and replace them with the numbers in the correct range that satisfy the constraints
	 * of the Sudoku puzzle. If true is provided as input, the method should find finds ALL 
	 * possible solutions and store them in the field named solutions. */
	public void solve(boolean allSolutions) {

		// call solveSudoku on this so that the board can be solved recursively
		//and so that it can check if a certain board is solvable
		this.solveSudoku(allSolutions);

	}

	



	/* Default constructor.  This will initialize all positions to the default 0
	 * value.  Use the read() function to load the Sudoku puzzle from a file or
	 * the standard input. */
	public ChessSudoku( int size ) {
		SIZE = size;
		N = size*size;

		grid = new int[N][N];
		for( int i = 0; i < N; i++ ) 
			for( int j = 0; j < N; j++ ) 
				grid[i][j] = 0;
	}


	/* readInteger is a helper function for the reading of the input file.  It reads
	 * words until it finds one that represents an integer. For convenience, it will also
	 * recognize the string "x" as equivalent to "0". */
	static int readInteger( InputStream in ) throws Exception {
		int result = 0;
		boolean success = false;

		while( !success ) {
			String word = readWord( in );

			try {
				result = Integer.parseInt( word );
				success = true;
			} catch( Exception e ) {
				// Convert 'x' words into 0's
				if( word.compareTo("x") == 0 ) {
					result = 0;
					success = true;
				}
				// Ignore all other words that are not integers
			}
		}

		return result;
	}


	/* readWord is a helper function that reads a word separated by white space. */
	static String readWord( InputStream in ) throws Exception {
		StringBuffer result = new StringBuffer();
		int currentChar = in.read();
		String whiteSpace = " \t\r\n";
		// Ignore any leading white space
		while( whiteSpace.indexOf(currentChar) > -1 ) {
			currentChar = in.read();
		}

		// Read all characters until you reach white space
		while( whiteSpace.indexOf(currentChar) == -1 ) {
			result.append( (char) currentChar );
			currentChar = in.read();
		}
		return result.toString();
	}


	/* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
	 * grid is filled in one row at at time, from left to right.  All non-valid
	 * characters are ignored by this function and may be used in the Sudoku file
	 * to increase its legibility. */
	public void read( InputStream in ) throws Exception {
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				grid[i][j] = readInteger( in );
			}
		}
	}


	/* Helper function for the printing of Sudoku puzzle.  This function will print
	 * out text, preceded by enough ' ' characters to make sure that the printint out
	 * takes at least width characters.  */
	void printFixedWidth( String text, int width ) {
		for( int i = 0; i < width - text.length(); i++ )
			System.out.print( " " );
		System.out.print( text );
	}


	/* The print() function outputs the Sudoku grid to the standard output, using
	 * a bit of extra formatting to make the result clearly readable. */
	public void print() {
		// Compute the number of digits necessary to print out each number in the Sudoku puzzle
		int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

		// Create a dashed line to separate the boxes 
		int lineLength = (digits + 1) * N + 2 * SIZE - 3;
		StringBuffer line = new StringBuffer();
		for( int lineInit = 0; lineInit < lineLength; lineInit++ )
			line.append('-');

		// Go through the grid, printing out its values separated by spaces
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				printFixedWidth( String.valueOf( grid[i][j] ), digits );
				// Print the vertical lines between boxes 
				if( (j < N-1) && ((j+1) % SIZE == 0) )
					System.out.print( " |" );
				System.out.print( " " );
			}
			System.out.println();

			// Print the horizontal line between boxes
			if( (i < N-1) && ((i+1) % SIZE == 0) )
				System.out.println( line.toString() );
		}
	}


	/* The main function reads in a Sudoku puzzle from the standard input, 
	 * unless a file name is provided as a run-time argument, in which case the
	 * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
	 * outputs the completed puzzle to the standard output. */
	public static void main( String args[] ) throws Exception {
		InputStream in = new FileInputStream("medium3x3_eightSolutions.txt");

		// The first number in all Sudoku files must represent the size of the puzzle.  See
		// the example files for the file format.
		int puzzleSize = readInteger( in );
		if( puzzleSize > 100 || puzzleSize < 1 ) {
			System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
			System.exit(-1);
		}

		ChessSudoku s = new ChessSudoku( puzzleSize );
		
		// You can modify these to add rules to your sudoku
		s.knightRule = false;
		s.kingRule = false;
		s.queenRule = false;
		
		// read the rest of the Sudoku puzzle
		s.read( in );

		System.out.println("Before the solve:");
		s.print();
		System.out.println();


		// Solve the puzzle by finding one solution.
		s.solve(true);

		for(ChessSudoku cs: s.solutions){
			cs.print();
		}
		System.out.println("After the solve:");
		s.print();
	}
}

