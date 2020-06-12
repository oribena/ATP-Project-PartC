package View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutController implements Initializable {
    public Button close;
    public Label Itext;

    public void close(){
        Stage s = (Stage)close.getScene().getWindow();
        s.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Itext.setWrapText(true);
        Itext.setText("Hello,\n" +
                "This is a game about a Goblin trying to find the gold coin that can grab silver coins on his way.\n" +
                "Algorithms used: BestFirstSearch, BreadthFirstSearch, DepthFirstSearch\n\n" +
                "Developers:\n" + "Noaa Kless - 204938351 \n" + "& \n" + "Ori Ben-Artzy - 206252496\n");

    }
}