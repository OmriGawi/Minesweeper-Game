package mines;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private Button resetBTN;
    
    @FXML
    private TextField heightFLD;

    @FXML
    private TextField widthFLD;
    
    @FXML
    private TextField minesFLD;

    //Getter
    public Button getResetBTN() {
    	return resetBTN;
    }
    
    //Getter
    public TextField getHeightFLD() {
    	return heightFLD;
    }
    
    //Getter
    public TextField getWidthFLD() {
    	return widthFLD;
    }

    //Getter
    public TextField getMinesFLD() {
    	return minesFLD;
    }


}
