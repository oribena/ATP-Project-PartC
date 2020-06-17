package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

public interface IModel {

    void generateMaze(int row, int col);
    void solveMaze();
    void stopServers();
    void exitGame();
    void moveCharacter(KeyCode movement);
}
