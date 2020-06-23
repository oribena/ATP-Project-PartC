package ViewModel;

import Model.MyModel;
import algorithms.search.AState;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer{
    private MediaPlayer myMedia;
    private static MyViewModel myViewModel;
    private MyModel model;
    private int[][] mazeArray;
    private int goalPosRow;
    private int goalPosCol;
    private int currPosRow;
    private int currPosCol;
    private boolean wonGame;
    private ArrayList<AState> solution;

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
            if (arg == "generate" || arg == "load") {
                mazeArray = model.getMaze();
                currPosRow = model.getCharacterPositionRow();
                currPosCol = model.getCharacterPositionCol();
                goalPosRow = model.getGoalPosRow();
                goalPosCol = model.getGoalPosCol();
                if (arg == "generate"){
                    setChanged();
                    notifyObservers("generate");
                }
                if (arg == "load"){
                    setChanged();
                    notifyObservers("load");
                }

            }
            else if (arg == "move") {
                currPosRow = model.getCharacterPositionRow();
                currPosCol = model.getCharacterPositionCol();
                wonGame = model.isWonGame();
                setChanged();
                notifyObservers("move");
            }
            else if (arg == "solve") {
                solution = model.getSolution();
                setChanged();
                notifyObservers("solve");
            }
        }
    }


    public ArrayList<AState> getSolution() { return model.getSolution();}

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

    public void playMusic(Media media,double vol){
        myMedia =new MediaPlayer(media);
        myMedia.setVolume(vol);
        myMedia.play();
    }
    public void pauseMusic(){
        if(myMedia !=null) {
            myMedia.stop();
        }
    }

    public void mute(boolean mute){
        if (mute==true)
        {
        myMedia.setMute(mute);
        myMedia.setAutoPlay(!mute);
        }
        else
            myMedia.play();
    }
}
