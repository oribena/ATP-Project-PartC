package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class mazeDisplayer extends Canvas {

    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    //private int characterPositionRowSTART = 1;
    //private int characterPositionColumnSTART = 1;
    public boolean done;


    public void setDone(boolean done) {
        this.done = done;
    }
    public boolean isDone(){
        if(done){
            return true;
        }
        else {
            return false;
        }
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        redraw();
    }

    public void writeSolution(Solution solution){
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / /*maze.length*/maze.getMat().length;
        double cellWidth = canvasWidth / /*maze[0].length*/ maze.getMat()[0].length;
        ArrayList<AState> path = solution.getSolutionPath();
        GraphicsContext gc = getGraphicsContext2D();
        String imageSolution = this.ImageSolution.getValue();
        if (imageSolution == null)
            gc.setFill(Color.BLACK);
        for (int i=1 ; i < path.size() - 1 ; i++){
            gc.fillRect(((MazeState) path.get(i)).getPos().getColumnIndex()*cellWidth, ((MazeState) path.get(i)).getPos().getRowIndex()*cellHeight  , cellWidth,cellHeight);
        }
        //(mazeState)list
    }

    public void initial()
    {
        GraphicsContext graphicsContext=getGraphicsContext2D();
        graphicsContext.clearRect(0,0,getWidth(),getHeight());
        try {
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            graphicsContext.drawImage(characterImage,0,0,getWidth(),getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        //characterPositionRowSTART = row;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }



    public void redraw() {
        if (ImageFileNameCharacter.getValue() == null )
            return;
        if (maze != null) {
            //maze.getOurMaze()[characterPositionRow][characterPositionColumn] = 2;
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / /*maze.length*/maze.getMat().length;
            double cellWidth = canvasWidth / /*maze[0].length*/ maze.getMat()[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image endPosImage = new Image(new FileInputStream(ImageFileEnd.get()));
                Image pathImage = new Image(new FileInputStream(ImagePath.get()));
                Image solutionImage = new Image(new FileInputStream(ImageSolution.get()));



                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.getMat().length; i++) {
                    for (int j = 0; j < maze.getMat()[0].length; j++) {
                        if (maze.getMat()[i][j] == 1) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            //gc.drawImage(wallImage, j* cellHeight ,  i * cellWidth, cellHeight, cellWidth );
                            gc.drawImage(wallImage, j* cellWidth ,  i * cellHeight, cellWidth, cellHeight );

                        }
                        else
                        if(maze.getMat()[i][j] == 2 ){
                            //gc.drawImage(solutionImage, j* cellHeight ,  i * cellWidth, cellHeight, cellWidth );
                            gc.drawImage(solutionImage, j* cellWidth ,  i * cellHeight, cellWidth, cellHeight );
                        }
                        else{
                            //gc.drawImage(pathImage, j* cellHeight ,  i * cellWidth, cellHeight, cellWidth );
                            gc.drawImage(pathImage, j* cellWidth ,  i * cellHeight, cellWidth, cellHeight );
                        }
                    }
                }

//                gc.drawImage(characterImage,characterPositionColumn * cellHeight, characterPositionRow * cellWidth,cellHeight, cellWidth );
//                gc.drawImage(endPosImage,maze.getEndPos().getCol() * cellHeight ,maze.getEndPos().getRow() * cellWidth,cellHeight, cellWidth );
//                gc.drawImage(solutionImage, characterPositionColumnSTART* cellWidth ,  characterPositionRowSTART * cellHeight, cellWidth, cellHeight );
                gc.drawImage(characterImage,characterPositionColumn * cellWidth, characterPositionRow * cellHeight,cellWidth, cellHeight );
                gc.drawImage(endPosImage,maze.getGoalPosition().getColumnIndex() * cellWidth ,maze.getGoalPosition().getRowIndex()* cellHeight,cellWidth, cellHeight );

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileEnd = new SimpleStringProperty();
    private StringProperty ImagePath = new SimpleStringProperty();
    private StringProperty ImageSolution = new SimpleStringProperty();

    public String getImageSolution() {
        return ImageSolution.get();
    }

    public void setImageSolution(String imageSolution) {
        this.ImageSolution.set(imageSolution);
    }


    public String getImagePath() {
        return ImagePath.get();
    }

    public void setImagePath(String imagePath) {
        this.ImagePath.set(imagePath);
    }


    public String getImageFileEnd() {
        return ImageFileEnd.get();
    }

    public void setImageFileEnd(String imageFileEnd) {
        this.ImageFileEnd.set(imageFileEnd);
    }
    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }
    //endregion

}

    //characterPositionColumnSTART = column;