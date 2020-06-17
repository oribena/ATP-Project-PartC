package View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class helpController implements Initializable {
    public Button close;
    public Label Itext;

    public void close() {
        Stage s = (Stage)this.close.getScene().getWindow();
        s.close();
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.Itext.setWrapText(true);
        this.Itext.setText("Guide,\nPress: \nNUMPAD 2 - To go UP \nNUMPAD 8 - To go DOWN \nNUMPAD 6 - To go RIGHT \nNUMPAD 4 - To go LEFT \nNUMPAD 3 - To go UP & RIGHT \nNUMPAD 1 - To go UP & LEFT \nNUMPAD 9 - To go DOWN & RIGHT \nNUMPAD 7 - To go DOWN & LEFT \n\nYou can save/load the mazes.\n And if you have a problem with the maze - I'LL BE THERE FOR YOU!!\n You can see the solution by pressing the solve button.");
    }
}
