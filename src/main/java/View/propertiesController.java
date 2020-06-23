package View;

import Server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class propertiesController {
    @FXML
    private Button cancel;
    @FXML
    private Button apply;
    @FXML
    private MenuButton mazeAlgorithm;
    @FXML
    private MenuButton solvingAlgorithm;
    @FXML
    private MenuItem MyMazeGenerator;
    @FXML
    private MenuItem SimpleMazeGenerator;
    @FXML
    private MenuItem EmptyMazeGenerator;
    @FXML
    private MenuItem Best;
    @FXML
    private MenuItem BFS;
    @FXML
    private MenuItem DFS;



    public void onAction(ActionEvent actionEvent) {
        if(actionEvent.getSource() == Best || actionEvent.getSource() == BFS || actionEvent.getSource() == DFS ){
            solvingAlgorithm.setText(((MenuItem)actionEvent.getSource()).getText());
        }
        if(actionEvent.getSource() == MyMazeGenerator || actionEvent.getSource() == SimpleMazeGenerator || actionEvent.getSource() == EmptyMazeGenerator){
            mazeAlgorithm.setText(((MenuItem)actionEvent.getSource()).getText());
        }
        if(actionEvent.getSource() == cancel){
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        }
        if(actionEvent.getSource() == apply){
            Server.setConfigurations("MazeGenerator",mazeAlgorithm.getText());
            Server.setConfigurations("SearchingAlgorithm", solvingAlgorithm.getText());
            ((Stage)apply.getScene().getWindow()).close();

            //Alert
            MyViewController.showAlert("The Properties has changed successfully!");
            Stage s = (Stage)apply.getScene().getWindow();
            s.close();
        }

    }

}
