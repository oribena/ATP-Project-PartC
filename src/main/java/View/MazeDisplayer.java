package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int row_player;
    private int col_player;
    private int goalRow;
    private int goalCol;

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTarget = new SimpleStringProperty();
    StringProperty imageSolution = new SimpleStringProperty();
    StringProperty imagePath = new SimpleStringProperty();

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public String getImagePath() {
        return imagePath.get();
    }



    public void setImageSolution(String imageSolution) {
        this.imageSolution.set(imageSolution);
    }
    public String getImageSolution() {
        return imageSolution.get();
    }

    public String getImageFileNameTarget() { return imageFileNameTarget.get();}

    public void setImageFileNameTarget(String imageFileNameTarget) { this.imageFileNameTarget.set(imageFileNameTarget); }

    public String getImageFileNameWall() { return imageFileNameWall.get(); }

    public void setImageFileNameWall(String imageFileNameWall) { this.imageFileNameWall.set(imageFileNameWall); }

    public String getImageFileNamePlayer() { return imageFileNamePlayer.get(); }

    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }

    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col) {
        this.row_player = row;
        this.col_player = col;
        draw();
    }

    public int getGoalRow() { return goalRow; }

    public int getGoalCol() { return goalCol; }

    public void set_goal_position(int row, int col) {
        this.goalRow = row;
        this.goalCol = col;
    }

    public int[][] getMaze() {
        return maze;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public void drawMaze(int [][] maze)
    {
        this.maze = maze;
        draw();
    }

    public void draw()
    {
        if(maze != null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0, canvasWidth, canvasHeight);
            graphicsContext.strokeRect(0,0, canvasWidth, canvasHeight);
            graphicsContext.setStroke(Color.BROWN);
            graphicsContext.setLineWidth(4);
            graphicsContext.stroke();
            double w, h;
            //Draw Maze
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
                System.out.println("Can't find file");
            }
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(maze[i][j] == 1) //Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null) {
                            graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                        }
                        else {
                            graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                        }
                    }
                }
            }
            //Player
            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("Can't find image");
            }
            graphicsContext.drawImage(playerImage, w_player, h_player, cellWidth, cellHeight);
            //Target
            double h_target = getGoalRow() * cellHeight;
            double w_target = getGoalCol() * cellWidth;
            Image targetImage = null;
            try {
                targetImage = new Image(new FileInputStream(getImageFileNameTarget()));
            } catch (FileNotFoundException e) {
                System.out.println("Can't find image");
            }
            graphicsContext.drawImage(targetImage, w_target, h_target, cellWidth, cellHeight);
        }
    }


    public void writeSolution(Solution solution){
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / /*maze.length*/maze.length;
        double cellWidth = canvasWidth / /*maze[0].length*/ maze[0].length;
        ArrayList<AState> path = solution.getSolutionPath();
        GraphicsContext gc = getGraphicsContext2D();
        Image imageForSolution = null;
        try {
            imageForSolution = new Image(new FileInputStream(getImageSolution()));
        } catch (FileNotFoundException e) {
            System.out.println("Can't find image");
        }
        //String imageSolution = this.imageSolution.getValue();
        if (imageForSolution == null)
            gc.setFill(Color.RED);
        for (int i=1 ; i < path.size() - 1 ; i++){
            gc.drawImage(imageForSolution, ((MazeState) path.get(i)).getPos().getColumnIndex()*cellWidth, ((MazeState) path.get(i)).getPos().getRowIndex()*cellHeight, cellWidth, cellHeight );

            //gc.fillRect(((MazeState) path.get(i)).getPos().getColumnIndex()*cellWidth, ((MazeState) path.get(i)).getPos().getRowIndex()*cellHeight  , cellWidth,cellHeight);
        }
        //(mazeState)list
    }

    //////////////////////////////// delete
    public void redraw() {
        if (imageFileNamePlayer.getValue() == null )
            return;
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / /*maze.length*/maze.length;
            double cellWidth = canvasWidth / /*maze[0].length*/ maze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(imageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(imageFileNamePlayer.get()));
                Image endPosImage = new Image(new FileInputStream(imageFileNameTarget.get()));
                Image pathImage = new Image(new FileInputStream(imageSolution.get()));
                Image solutionImage = new Image(new FileInputStream(imageSolution.get()));


                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[0].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.drawImage(wallImage, j* cellWidth ,  i * cellHeight, cellWidth, cellHeight );

                        }
                        else
                        if(maze[i][j] == 2 ){
                            gc.drawImage(solutionImage, j* cellWidth ,  i * cellHeight, cellWidth, cellHeight );
                        }
                        else{
                            gc.drawImage(pathImage, j* cellWidth ,  i * cellHeight, cellWidth, cellHeight );
                        }
                    }
                }
                gc.drawImage(characterImage,col_player * cellWidth, row_player * cellHeight,cellWidth, cellHeight );
                gc.drawImage(endPosImage,goalCol * cellWidth ,goalRow * cellHeight,cellWidth, cellHeight );

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }
}
