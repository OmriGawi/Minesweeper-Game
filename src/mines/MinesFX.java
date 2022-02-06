package mines;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MinesFX extends Application {
	private Mines field;
	private int height, width, numMines;
	private HBox hbox;
	private Controller controller;
	private Button resetBTN;
	private FieldButton[][] fieldButtons;
	private Stage primaryStage;
	private GridPane gridPane;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("GameScreen.fxml"));
			hbox = loader.load();
			controller = loader.getController();
			this.primaryStage = primaryStage;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// Get Reset Button
		getResetButton();
		// Set stage image as cover.
		setPrimaryStageImage();
		// Set new Icon image for primaryStage
		setPrimaryStageIcon();
		// Set primary stage title
		setPrimaryStageTitle();

		Scene scene = new Scene(hbox);
		primaryStage.setScene(scene);

		// Start Game
		initGame();
		primaryStage.show();

		// Action for Reset button -- Start new game
		resetBTN.setOnAction(new ResetAction());
	}

	
	// Set primary stage title
	private void setPrimaryStageTitle() {
		primaryStage.setTitle("Minesweeper Game");
	}
	// Get reset button
	private void getResetButton() {
		resetBTN = controller.getResetBTN();
	}
	
	// Set icon for primary stage
	private void setPrimaryStageIcon() {
		primaryStage.getIcons().add(new Image("mines/Icon.png"));
	}
	// Set background image for primary stage
	private void setPrimaryStageImage() {
		Image img = new Image("mines/BGImage.jpg");
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
		BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, backgroundSize);
		Background bGround = new Background(bImg);
		hbox.setBackground(bGround);
	}

	// This function will create new gridPane and start the game
	private void initGame() {
		gridPane = new GridPane();

		// Create Mines object and allocate number of buttons.
		getValuesFromUser();
		field = new Mines(height, width, numMines);
		fieldButtons = new FieldButton[height][width];
		setSizeFieldButtons();

		// Create number of buttons depends on height and width from user
		createButtons();
		hbox.getChildren().add(gridPane);
	}

	// Initialize width, height and mines entered from user in GUI
	private void getValuesFromUser() {
		height = Integer.valueOf(controller.getHeightFLD().getText());
		width = Integer.valueOf(controller.getWidthFLD().getText());
		numMines = Integer.valueOf(controller.getMinesFLD().getText());
	}

	// Add row and column constraints for gridPane
	private void setSizeFieldButtons() {
		List<ColumnConstraints> column = new ArrayList<>();
		List<RowConstraints> row = new ArrayList<>();

		for (int i = 0; i < height; i++)
			row.add(new RowConstraints(40));
		for (int i = 0; i < width; i++)
			column.add(new ColumnConstraints(40));

		gridPane.getColumnConstraints().addAll(column);
		gridPane.getRowConstraints().addAll(row);
		gridPane.setPadding(new Insets(15, 15, 15, 15));
	}

	// Create all buttons for board
	// setOnMouseClicked eventHandler
	private void createButtons() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				fieldButtons[i][j] = new FieldButton(i, j);
				fieldButtons[i][j].setText(field.get(i, j));
				fieldButtons[i][j]
						.setStyle("    -fx-background-color:\r\n" + "            linear-gradient(#f2f2f2, #d6d6d6),\r\n"
								+ "            linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%),\r\n"
								+ "            linear-gradient(#dddddd 0%, #f6f6f6 50%);\r\n"
								+ "    -fx-background-radius: 8,7,6;\r\n" + "    -fx-background-insets: 0,1,2;\r\n"
								+ "    -fx-text-fill: black;\r\n" + "    -fx-font-size: 14px;\r\n"
								+ "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 )");

				// Buttons Mouse action event handler
				fieldButtons[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						MouseButton clickType = event.getButton();
						int x = ((FieldButton) event.getSource()).getX();
						int y = ((FieldButton) event.getSource()).getY();
						
						// Add sound to button
						Media sound = new Media(getClass().getClassLoader().getResource("mines/clickSound.wav").toExternalForm());
						MediaPlayer mediaPlayer = new MediaPlayer(sound);
						mediaPlayer.play();
						
						// Left click
						if (clickType == MouseButton.PRIMARY) {

							boolean isNotMine = field.open(x, y);
							if (!isNotMine) {
								field.setShowAll(true);
								updateAllFieldButtons();
								popWindow(true, false);
							}
							else if (field.isDone())
								popWindow(false, true);
							updateAllFieldButtons();
						}

						// Right click
						if (clickType == MouseButton.SECONDARY) {
							field.toggleFlag(x, y);
							updateFlagButton(x, y);
						}
					}
				});

				fieldButtons[i][j].setMaxWidth(Double.MAX_VALUE);
				fieldButtons[i][j].setMaxHeight(Double.MAX_VALUE);
				gridPane.add(fieldButtons[i][j], j, i);
			}
	}

	// Reset Button action.
	// will start new game and initialize primaryStage size depends on parameters.
	class ResetAction implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			hbox.getChildren().remove(gridPane);
			getValuesFromUser();
			if (width > 6)
				primaryStage.setWidth(400 + (width - 6) * 40);
			else
				primaryStage.setWidth(400);
			if (height > 5)
				primaryStage.setHeight(275 + (height - 5) * 43);
			else
				primaryStage.setHeight(275);
			initGame();
		}
	}

	// Set or remove flag from button
	private void updateFlagButton(int x, int y) {
		fieldButtons[x][y].setText(field.get(x, y));
	}

	// Update all buttons text shown in GUI
	private void updateAllFieldButtons() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				fieldButtons[i][j].setText(field.get(i, j));
			}
	}

	// Creates a pop up window that will show up if the user won or lost the game.
	private void popWindow(boolean gameIsOver, boolean gameIsDone) {
		HBox hbox = new HBox();
		Image img;
		Stage popUpStage = new Stage();
		Scene scene;

		// Load picture and set scene size
		if (gameIsOver) {
			img = new Image("mines/GameIsOver.jpg");
			scene = new Scene(hbox, 150, 150);
		} else {
			img = new Image("mines/YouWon.jpg");
			scene = new Scene(hbox, 225, 150);
		}

		// Set background image
		BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bGround = new Background(bImg);
		hbox.setBackground(bGround);

		popUpStage.setScene(scene);
		popUpStage.show();
	}
}
