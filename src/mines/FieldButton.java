package mines;

import javafx.scene.control.Button;

public class FieldButton extends Button{
	private int x, y;

	//Constructor
	public FieldButton(int x, int y) {
		this.x = x;
		this.y = y;
	}

	//Getter
	public int getX() {
		return x;
	}

	//Getter
	public int getY() {
		return y;
	}
}