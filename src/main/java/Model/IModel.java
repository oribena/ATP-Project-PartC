package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

public interface IModel {

    void generateMaze(int width, int height);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    void solving();
    Maze getMaze();
    public Solution getSolution();
    boolean solvingHappened();
    void moveCharacter(KeyCode movement);

    public void exitGame();
}
