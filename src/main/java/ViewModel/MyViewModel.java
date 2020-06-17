package ViewModel;

import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer{
    private MediaPlayer mdp_music;
    private MediaPlayer mdp_media;
    private static MyViewModel myViewModel;
    private MyModel model;
    private int[][] mazeArray;
    private int goalPosRow;
    private int goalPosCol;
    private int currPosRow;
    private int currPosCol;
    private boolean wonGame;

    private MyViewModel() {
        model = MyModel.getInstance();
    }

    public static MyViewModel getInstance() {
        if (myViewModel == null) {
            myViewModel = new MyViewModel();
        }
        return myViewModel;
    }

    public int[][] getMaze() { return mazeArray; }

    public int getCurrPosRow() { return currPosRow; }

    public int getCurrPosCol() { return currPosCol; }

    public int getGoalPosRow() { return goalPosRow; }

    public int getGoalPosCol() { return goalPosCol; }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyModel) {
            if (arg == "generate") {
                mazeArray = model.getMaze();
                currPosRow = model.getCharacterPositionRow();
                currPosCol = model.getCharacterPositionCol();
                goalPosRow = model.getGoalPosRow();
                goalPosCol = model.getGoalPosCol();
                setChanged();
                notifyObservers("generate");
            }
            else if (arg == "move") {
                currPosRow = model.getCharacterPositionRow();
                currPosCol = model.getCharacterPositionCol();
                wonGame = model.isWonGame();
                setChanged();
                notifyObservers("move");
            }
            else if (arg == "solve") {
                setChanged();
                notifyObservers("solve");
            }
        }
    }


    public Solution getSolution() { return model.getSolution();}

    public boolean isWonGame() { return wonGame; }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }


    public void moveCharacter(KeyEvent movement){
        model.moveCharacter(movement.getCode());
    }

    public void load() {
        model.load();
    }
    public void save(){
        model.save();
    }

    public void exitGame(){
        model.exitGame();
    }


    public void SolveMaze(){
        model.solveMaze();
        //setChanged();
        //notifyObservers();
    }


}
