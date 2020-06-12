package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import algorithms.mazeGenerators.Maze;

import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
//import View.Main;
import View.MyViewController;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;
import com.sun.org.apache.xpath.internal.operations.String;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import Server.Server;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import sample.Main;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Server generateTheMaze;
    private Server solveTheMaze ;
    public boolean solved;
    java.lang.String s = new File("resources/Music/EyeOfTheTiger.mp3").toURI().toString();
    private MediaPlayer MediaPlayer = new MediaPlayer(new Media(s));

    public void startServers() {
        generateTheMaze.start();
        solveTheMaze.start();
    }

    public void stopServers() {
        generateTheMaze.stop();
        solveTheMaze.stop();
    }

    private Maze maze;
    private Solution mazeSolution;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;


    @Override
    public void generateMaze(int width, int height) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {

                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{height , width};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(mazeDimensions[0] * mazeDimensions[1]) + 12];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        characterPositionRow = maze.getStartPosition().getRowIndex();
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                        maze.getMat()[characterPositionRow][characterPositionColumn] =2;
                        //notify observers. will need to right to them every time
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            solved = false;
            //Music();
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }


    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public Maze getMaze() {
        return this.maze;
    }

    @Override
    public Solution getSolution() {
        return mazeSolution;
    }

    public boolean solvingHappened(){
        return solved;
    }

    @Override
    public void solving() {
        try {
            int [][]copy2=new int[maze.getMat().length][maze.getMat()[0].length];
            for (int i = 0 ; i < maze.getMat().length ; i++)
            {

                for (int j = 0 ; j< maze.getMat()[0].length ; j++)
                {
                    if ( maze.getMat()[i][j] != 1 && maze.getMat()[i][j] != 0){
                        copy2[i][j]=0;
                    }
                    else {
                        copy2[i][j] = maze.getMat()[i][j];
                    }

                }
            }
            Maze mazeTemp=new Maze(copy2,maze.getStartPosition(),maze.getGoalPosition());
            maze.print();
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(mazeTemp);
                        toServer.flush();
                        mazeSolution = (Solution)fromServer.readObject();

                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            solved = true;
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }

    public void winHandle()
    {
        if(characterPositionColumn == maze.getGoalPosition().getColumnIndex() && characterPositionRow == maze.getGoalPosition().getRowIndex())
        {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("YOU'VE WON!");
            alert.show();
        }
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        solved = false;
        int row = getCharacterPositionRow();
        int col = getCharacterPositionColumn();
        int sizeRow = getMaze().getMat().length;
        int sizeCol = getMaze().getMat()[0].length;
        switch (movement) {
            case UP:
                if( row>0 && getMaze().getMat()[row - 1][col] != 1) {
                    characterPositionRow--;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case DOWN:
                if( row<sizeRow-1 && getMaze().getMat()[row + 1][col] != 1) {
                    characterPositionRow++;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case RIGHT:
                if(col < sizeCol-1 && getMaze().getMat()[row][col + 1] != 1){
                    characterPositionColumn++;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case LEFT:
                if(col>0 && getMaze().getMat()[row][col - 1] != 1){
                    characterPositionColumn--;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD8:
                if( row>0 && getMaze().getMat()[row - 1][col] != 1) {
                    characterPositionRow--;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD2:
                if( row<sizeRow-1 && getMaze().getMat()[row + 1][col] != 1) {
                    characterPositionRow++;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD6:
                if(col < sizeCol-1 && getMaze().getMat()[row][col + 1] != 1){
                    characterPositionColumn++;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD4:
                if(col>0 && getMaze().getMat()[row][col - 1] != 1){
                    characterPositionColumn--;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD3:
                if( row< sizeRow-1 && col < sizeCol-1&& getMaze().getMat()[row + 1][col + 1] != 1) {
                    characterPositionRow++;
                    characterPositionColumn++;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD1:
                if( row<sizeRow-1&& col>0 && getMaze().getMat()[row + 1][col-1] != 1) {
                    characterPositionRow++;
                    characterPositionColumn--;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD7:
                if(col >0&& row>0 && getMaze().getMat()[row-1][col - 1] != 1){
                    characterPositionColumn--;
                    characterPositionRow--;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
            case NUMPAD9:
                if(row>0 &&   col<sizeCol-1 && getMaze().getMat()[row-1][col + 1] != 1){
                    characterPositionColumn++;
                    characterPositionRow--;
                    getMaze().getMat()[characterPositionRow][characterPositionColumn] = 2;
                    winHandle();
                }
                break;
        }
        setChanged();
        notifyObservers();
    }

    public void exitGame(){
        generateTheMaze.stop();
        solveTheMaze.stop();
        Main.exitGame();
    }
}
