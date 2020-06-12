package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer{

    private IModel model;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            setChanged();
            notifyObservers();
        }
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public Solution getSolution() {
        return model.getSolution();
    }

    public boolean solvingHappened() {
        return model.solvingHappened();
    }

    public void exitGame(){
        model.exitGame();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public void SolveMaze(){
        model.solving();
        setChanged();
        notifyObservers();
    }
}
