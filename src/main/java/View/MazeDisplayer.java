package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
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
    private boolean solved;
    //private Solution currSol;
    private ArrayList<AState> path;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTarget = new SimpleStringProperty();
    StringProperty imageSolution = new SimpleStringProperty();
    StringProperty imagePath = new SimpleStringProperty();
    StringProperty imageYellow = new SimpleStringProperty();
    StringProperty imageRed= new SimpleStringProperty();
    StringProperty imageBlue = new SimpleStringProperty();

    public void setImageYellow(String imageYellow) {
        this.imageYellow.set(imageYellow);
    }

    public void setImageRed(String imageRed) {
        this.imageRed.set(imageRed);
    }

    public void setImageBlue(String imageBlue) {
        this.imageBlue.set(imageBlue);
    }


    public String getImageYellow() {
        return imageYellow.get();
    }

    public StringProperty imageYellowProperty() {
        return imageYellow;
    }

    public String getImageRed() {
        return imageRed.get();
    }

    public StringProperty imageRedProperty() {
        return imageRed;
    }

    public String getImageBlue() {
        return imageBlue.get();
    }

    public StringProperty imageBlueProperty() {
        return imageBlue;
    }



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
        solved=false;
        draw();
    }

    public void draw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setStroke(Color.BROWN);
            graphicsContext.setLineWidth(4);
            graphicsContext.stroke();
            double w, h;
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
                System.out.println("Can't find file");
            }
            //drawing the walls
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (maze[i][j] == 1)
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null) {
                            graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                        } else {
                            graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                        }
                    }
                }
            }
            //draw the solution
            if (solved) {
                Image imageRed = null;
                Image imageYellow = null;
                Image imageBlue = null;

                try {
                    imageRed = new Image(new FileInputStream(getImageRed()));
                    imageYellow = new Image(new FileInputStream(getImageYellow()));
                    imageBlue = new Image(new FileInputStream(getImageBlue()));
                } catch (FileNotFoundException e) {
                    System.out.println("Can't find image");
                }
                for (int i = 1; i < path.size() - 1; i += 3) {
                    graphicsContext.drawImage(imageRed, ((MazeState) path.get(i)).getPos().getColumnIndex() * cellWidth, ((MazeState) path.get(i)).getPos().getRowIndex() * cellHeight, cellWidth, cellHeight);
                    if (i + 1 < path.size() - 1)
                        graphicsContext.drawImage(imageBlue, ((MazeState) path.get(i + 1)).getPos().getColumnIndex() * cellWidth, ((MazeState) path.get(i + 1)).getPos().getRowIndex() * cellHeight, cellWidth, cellHeight);
                    if (i + 2 < path.size() - 1)
                        graphicsContext.drawImage(imageYellow, ((MazeState) path.get(i + 2)).getPos().getColumnIndex() * cellWidth, ((MazeState) path.get(i + 2)).getPos().getRowIndex() * cellHeight, cellWidth, cellHeight);
                }
            }
            //draw the Player
            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("Can't find image");
            }
            graphicsContext.drawImage(playerImage, w_player, h_player, cellWidth, cellHeight);
            //draw the goal
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

    public void writeSolution(ArrayList<AState> solution) {
        solved=true;
        path = solution;
        draw();
    }
}
