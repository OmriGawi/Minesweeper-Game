package mines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mines {

	private int height, width;
	private Field board[][];
	private int numMines;
	private boolean showAll = false;

	// Constructor
	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		this.numMines = numMines;
		board = new Field[height][width];
		initializeBoard();
		setRandomMines();
	}

	// Initialize board with Field variables.
	private void initializeBoard() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				board[i][j] = new Field();
	}

	// randomly places NumMines mines on the game board.
	private void setRandomMines() {
		Random rand = new Random();
		int numOfMines = numMines;

		while (numOfMines != 0) {
			// Choose a random 2D coordinate within the board
			int i = rand.nextInt(height);
			int j = rand.nextInt(width);

			if (!board[i][j].isMine) {
				board[i][j].isMine = true;
				updateNumMines(i, j);
				numOfMines--;
			}
		}
	}

	// For a given coordinate, will update all neighbors's minesAround by 1.
	private void updateNumMines(int i, int j) {
		List<Location> list = new ArrayList<>();
		list = new Location(i, j).neighbors();
		// For each neighbor increase mines around.
		for (Location l : list)
			board[l.i][l.j].minesAround++;
	}

	// add a new mine in a given place.
	public boolean addMine(int i, int j) {
		if (board[i][j].isMine)
			return false;
		else
			board[i][j].isMine = true;
		updateNumMines(i, j);
		return true;
	}

	public boolean open(int i, int j) {
		// case there is a mine
		if (board[i][j].isMine) {
			return false;
		}
		// case already open
		if (board[i][j].isOpen)
			return true;
		else
			board[i][j].isOpen = true;
		// case no mines around
		if (board[i][j].minesAround == 0) {
			List<Location> list = new ArrayList<>();
			list = new Location(i, j).neighbors();
			for (Location l : list) {
				if (!board[l.i][l.j].isOpen)
					open(l.i, l.j);
			}
		}
		return true;
	}

	// place a flag in a given place.
	// if there is already a flag there, it will remove it.
	public void toggleFlag(int x, int y) {
		if (board[x][y].isFlag)
			board[x][y].isFlag = false;
		else
			board[x][y].isFlag = true;
	}

	// Return true if all locations that are not mines are opened.
	public boolean isDone() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (!board[i][j].isOpen && !board[i][j].isMine)
					return false;
			}
		}
		return true;
	}

	// Returns a representation of in a string by position.
	// If showAll is true will return all locations as if they are open.
	public String get(int i, int j) {
		if (showAll) {
			boolean temp = board[i][j].isOpen;
			board[i][j].isOpen = true;
			String str = board[i][j].toString();
			board[i][j].isOpen = temp;
			return str;
		}
		return board[i][j].toString();
	}

	// Setter
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	// Returns a description of the board as a string by using get() function for
	// each location.
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				b.append(get(i, j));
			b.append("\n");
		}
		return b.toString();
	}

	// This class represent a 2D coordinate in the board.
	private class Field {
		private boolean isMine, isFlag, isOpen;
		@SuppressWarnings("unused")
		private int minesAround = 0;

		@Override
		public String toString() {
			// Close
			if (!isOpen) {
				if (isFlag)
					return "F";
				else
					return ".";
			}
			// Open
			if (isMine)
				return "X";
			if (minesAround != 0)
				return "" + minesAround;
			return " ";
		}

	}

	// This class represent a single location in a 2D Matrix.
	private class Location {
		private int i, j;

		public Location(int i, int j) {
			this.i = i;
			this.j = j;
		}

		// Return a List of Location for all neighbors in the board for correct
		// Location.
		public List<Location> neighbors() {
			List<Location> nr = new ArrayList<>();

			// Check 8 directions
			// Up
			if (i > 0)
				nr.add(new Location(i - 1, j));
			// Down
			if (i < height - 1)
				nr.add(new Location(i + 1, j));
			// Right
			if (j < width - 1)
				nr.add(new Location(i, j + 1));
			// Left
			if (j > 0)
				nr.add(new Location(i, j - 1));
			// Top left corner
			if (i > 0 && j > 0)
				nr.add(new Location(i - 1, j - 1));
			// Top right corner
			if (i > 0 && j < width - 1)
				nr.add(new Location(i - 1, j + 1));
			// Bottom left corner
			if (i < height - 1 && j > 0)
				nr.add(new Location(i + 1, j - 1));
			// Bottom right corner
			if (i < height - 1 && j < width - 1)
				nr.add(new Location(i + 1, j + 1));

			return nr;
		}
	}

}
