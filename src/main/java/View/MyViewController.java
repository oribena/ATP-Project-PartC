package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import com.sun.prism.impl.VertexBuffer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.omg.CORBA.portable.ValueBase;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MyViewController implements IView, Observer {

    private MyViewModel viewModel;
    public mazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.Button Start;
    public javafx.scene.control.Button CMaze;
    public javafx.scene.control.Button Solve;
    public javafx.scene.control.TextField mazeFileName;
    public javafx.scene.control.Button Save;
    public javafx.scene.control.Label labelM;
    public javafx.scene.control.MenuItem saveMenu;


    public void pushButton(ActionEvent actionEvent) {
        System.out.println("test");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("hiiii");
        alert.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            displayMaze(viewModel.getMaze());
            btn_generateMaze.setDisable(false);
            int currentCol = viewModel.getCharacterPositionColumn();
            int currentRow = viewModel.getCharacterPositionRow();
            Maze maze1 = viewModel.getMaze();
            boolean solved = viewModel.solvingHappened();
            mazeDisplayer.setDone(solved);
            mazeDisplayer.setMaze(maze1);
            if (solved == true)
            {
                mazeDisplayer.writeSolution(viewModel.getSolution());
            }
        }
    }

    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
    }


    public static void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }
    ///////////////////////////

    public void setViewModel(MyViewModel viewModel) {
        mazeDisplayer.initial();
        this.viewModel = viewModel;
        bindProperties(viewModel);
    }

    private void bindProperties(MyViewModel viewModel) {
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRow);
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn);
    }

    //region String Property for Binding
    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();
    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }
    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }
    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }
    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }
    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                Scale fixedScale = new Scale();
                double old = oldSceneWidth.doubleValue();
                double fixed = newSceneWidth.doubleValue();
                fixedScale.setPivotX(mazeDisplayer.getLayoutX() * (fixed/old));
                fixedScale.setX(mazeDisplayer.getScaleX() * (fixed/old));
                mazeDisplayer.getTransforms().add(fixedScale);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                Scale fixedScale = new Scale();
                double old = oldSceneHeight.doubleValue();
                double fixed = newSceneHeight.doubleValue();
                fixedScale.setPivotY(mazeDisplayer.getLayoutY() * (fixed/old));
                fixedScale.setY(mazeDisplayer.getScaleY() * (fixed/old));
                mazeDisplayer.getTransforms().add(fixedScale);            }
        });
    }
//////////////////////

    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
        if(mazeDisplayer.isDone()) {
            txtfld_rowsNum.setDisable(false);
            txtfld_columnsNum.setDisable(false);
            Solve.setDisable(true);
            CMaze.setDisable(false);
            Start.setDisable(true);
            showAlert("congratulation!, you won");

        }
    }


    public void generateMaze() {
        int heigth = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        if ( heigth <= 0 || width <= 0 )
            showAlert("wrong INPUT");
        btn_generateMaze.setDisable(true);
        btn_solveMaze.setDisable(false);
        viewModel.generateMaze(width, heigth);
    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.SolveMaze();
    }

    public void About(ActionEvent actionEvent){
        try{
            System.out.println("test");
            Stage stage=new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader=new FXMLLoader();
            Parent root=fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene=new Scene(root,600,600);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){

        }
    }
    public void Help(ActionEvent actionEvent){
        try{

            Stage stage=new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader=new FXMLLoader();
            Parent root=fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene=new Scene(root,600,600);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){

        }
    }


    public void Properties() {
        try {
            Stage stage = new Stage();
            stage.setTitle("properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = (Parent)fxmlLoader.load(this.getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 600.0D, 600.0D);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception var5) {
        }

    }

    public void Exit(ActionEvent actionEvent) {
        viewModel.exitGame();
    }


}
