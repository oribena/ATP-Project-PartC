package ViewModel;

import Model.IModel;
import javafx.beans.value.ObservableValue;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel{

    private IModel model;
    private int characterPositionRowIndex=0;
    private int characterPositionColumnIndex=0;

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
}
