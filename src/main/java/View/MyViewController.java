package View;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class MyViewController implements IView{
    public void pushButton(ActionEvent actionEvent) {
        System.out.println("test");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("hiiii");
        alert.show();
    }
}
