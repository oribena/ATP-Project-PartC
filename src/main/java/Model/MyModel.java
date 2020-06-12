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

import javax.swing.*;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
public class MyModel implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Server generateTheMaze;
    private Server solveTheMaze ;
    public boolean solved;
    java.lang.String s = new File("resources/Music/EyeOfTheTiger.mp3").toURI().toString();
    private MediaPlayer MediaPlayer = new MediaPlayer(new Media(s));

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
            //setChanged();
            //notifyObservers();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }

    public void exitGame(){
//        generateTheMaze.stop();
//        solveTheMaze.stop();
//        Main.exitGame();
    }
}
