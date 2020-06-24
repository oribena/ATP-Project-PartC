package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import static javafx.geometry.Pos.CENTER;

public class MyViewController implements IView,Observer , Initializable {

    protected MyViewModel viewModel = MyViewModel.getInstance();
    @FXML
    public javafx.scene.control.TextField textField_mazeRows;
    @FXML
    public javafx.scene.control.TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public javafx.scene.control.Label lbl_player_row;
    @FXML
    public javafx.scene.control.Label lbl_player_column;
    @FXML
    public javafx.scene.control.Button buttonGenerateMaze;
    @FXML
    public javafx.scene.control.Button buttonSolveMaze;
    @FXML
    public javafx.scene.control.Button mute;
    @FXML
    public javafx.scene.control.Button buttonHideMaze;
    @FXML
    public Pane pane;
    @FXML
    public BorderPane borderPane;

    public boolean isMusic =true;
    public boolean alreadyGenerate =false;

    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();

    public String get_update_player_position_row() {
        return update_player_position_row.get();
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public String get_update_player_position_col() {
        return update_player_position_col.get();
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);
        //adjust Display Size of the maze to the window
        adjustDisplaySize();
    }

    private void adjustDisplaySize() {
        borderPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            pane.setMinHeight(borderPane.getWidth()-107);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });
        borderPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            pane.setMinHeight(borderPane.getHeight()-25);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });
        pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            mazeDisplayer.setWidth(pane.getWidth()-107);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });
        pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            mazeDisplayer.setHeight(borderPane.getHeight()-25);
            if (viewModel.getMaze() != null)
                mazeDisplayer.draw();
        });
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel)  { //if (o instanceof MyViewModel)
            if (arg == "generate" || arg == "load") {
                if (arg == "load"){

                }

                isMusic = true;
                mazeDisplayer.setMaze(viewModel.getMaze());
                mazeDisplayer.set_goal_position(viewModel.getGoalPosRow(), viewModel.getGoalPosCol());
                mazeDisplayer.set_player_position(viewModel.getCurrPosRow(), viewModel.getCurrPosCol());
                mazeDisplayer.drawMaze(mazeDisplayer.getMaze());
                buttonGenerateMaze.setDisable(false);
                //zoom in and out
                this.zoom(mazeDisplayer);
                //Music
                viewModel.pauseMusic();
                try{
                    viewModel.playMusic((new Media(getClass().getResource("/Music/song.mp3").toURI().toString())),200.0);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            else if (arg == "move") {
                if (viewModel.isWonGame() == true) {
                    //Music
                    viewModel.pauseMusic();
                    try{
                        viewModel.playMusic((new Media(getClass().getResource("/Music/end.mp3").toURI().toString())),400.0);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    //Gif pop up
                    Stage stage = new Stage();
                    stage.setTitle("YOU WON!!!");
                    VBox layout = new VBox();
                    HBox H = new HBox(5);
                    H.setAlignment(CENTER);
                    layout.setAlignment(CENTER);
                    javafx.scene.control.Button close = new javafx.scene.control.Button();
                    close.setText("Close");
                    H.getChildren().add(close);
                    layout.spacingProperty().setValue(10);
                    Image im = new Image("/Images/wonGif.gif");
                    ImageView image = new ImageView(im);
                    layout.getChildren().add(image);
                    layout.getChildren().add(H);
                    Scene scene = new Scene(layout, 520, 350);
                    scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();
                    //Gif close button
                    close.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            viewModel.pauseMusic();
                            stage.close();

                        }
                    });
                }

                else
                    mazeDisplayer.set_player_position(viewModel.getCurrPosRow(), viewModel.getCurrPosCol());
            }
            else if (arg == "solve") {
                //draw the solution
                mazeDisplayer.writeSolution(viewModel.getSolution());
            }
        }
    }

    public void zoom(MazeDisplayer pane) {
        pane.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.05;
                        double deltaY = event.getDeltaY();

                        if (deltaY < 0) {
                            zoomFactor = 0.95;
                        }
                        pane.setScaleX(pane.getScaleX() * zoomFactor);
                        pane.setScaleY(pane.getScaleY() * zoomFactor);
                        event.consume();
                    }
                });


    }

    public static void showAlert(String alertMessage) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void generateMaze()
    {
        alreadyGenerate = true;
        viewModel.addObserver(this);
        String strRows = textField_mazeRows.getText();
        String strCols = textField_mazeColumns.getText();
        if (isValidNum(strRows) && isValidNum(strCols)) {
            //disable buttons
            buttonSolveMaze.setDisable(false);
            buttonHideMaze.setDisable(true);
            mute.setDisable(false);
            //generate maze
            int rows = Integer.valueOf(strRows);
            int cols = Integer.valueOf(strCols);
            viewModel.generateMaze(rows, cols);

        }
        else
            showAlert("Enter a Valid number");
    }

    public boolean isValidNum(String str)
    {
        String regex = "\\d+";
        if (str.matches(regex)) {
            int val = Integer.valueOf(str);
            if (val >= 2 && val <= 500)
                return true;
        }
        return false;
    }

    public void solveMaze(ActionEvent actionEvent) {
        mazeDisplayer.writeSolution(viewModel.getSolution());
        buttonHideMaze.setDisable(false);
        buttonSolveMaze.setDisable(true);
//        viewModel.SolveMaze();
    }

    public void hideMaze(ActionEvent actionEvent) {
        mazeDisplayer.drawMaze(mazeDisplayer.getMaze());
        buttonHideMaze.setDisable(true);
        buttonSolveMaze.setDisable(false);
    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("F.R.I.E.N.D.S Maze");
            VBox layout = new VBox();
            layout.setAlignment(CENTER);
            javafx.scene.control.Button close = new javafx.scene.control.Button();
            close.setText("Back");
            javafx.scene.control.Label label = new javafx.scene.control.Label("Hey guys,\n" +
                    "Help Ross reach the central perk cafe safetly!! \n" +
                    "Algorithms used: BestFirstSearch, BreadthFirstSearch, DepthFirstSearch\n\n" +
                    "Developers:\n" + "Noaa Kless " + "and " + "Ori Ben-Artzy\n\n" + "HAVE FUN!");
            label.setAlignment(CENTER);
            label.setLineSpacing(2);
            layout.spacingProperty().setValue(30);
            layout.getChildren().addAll(label,close);
            close.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            Scene scene = new Scene(layout, 960, 614);
            scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
    }

    public void Help(ActionEvent actionEvent) {
        try {
            try{
                viewModel.playMusic((new Media(getClass().getResource("/Music/imFine.mp3").toURI().toString())),400.0);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setTitle("F.R.I.E.N.D.S Maze");
            VBox layout = new VBox();
            layout.setAlignment(CENTER);
            javafx.scene.control.Button close = new javafx.scene.control.Button();
            close.setText("Back");
            javafx.scene.control.Label label = new javafx.scene.control.Label("Guide,\nPress: \nNUMPAD 2 - To go UP \nNUMPAD 8 - To go DOWN \nNUMPAD 6 - To go RIGHT \nNUMPAD 4 - To go LEFT \nNUMPAD 3 - To go UP & RIGHT \nNUMPAD 1 - To go UP & LEFT \nNUMPAD 9 - To go DOWN & RIGHT \nNUMPAD 7 - To go DOWN & LEFT \n\nYou can save and load the mazes.\n If you have a problem with the maze - I'LL BE THERE FOR YOU!!\n You can see the solution by pressing the solve button.");
            label.setAlignment(CENTER);
            label.setLineSpacing(2);
            layout.spacingProperty().setValue(30);
            layout.getChildren().addAll(label,close);
            close.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            Scene scene = new Scene(layout, 960, 614);
            scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
    }

    public void Properties() {
        try {
            Stage stage = new Stage();
            stage.setTitle("F.R.I.E.N.D.S Maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = (Parent)fxmlLoader.load(this.getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 960.0D, 614.0D);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
            //stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception var5) {
        }

    }

    public void load() {
        if (alreadyGenerate == false) {
            showAlert("Please generate maze first");
        }
        else {
            viewModel.pauseMusic();
            buttonSolveMaze.setDisable(false);
            buttonHideMaze.setDisable(true);
            viewModel.load();
        }
    }

    public void save(){
        if (alreadyGenerate == false) {
            showAlert("Please generate maze first");
        }
        else {
            viewModel.save();
        }
    }

    public void Exit(ActionEvent actionEvent) {
        try{
            viewModel.playMusic((new Media(getClass().getResource("/Music/break.mp3").toURI().toString())),400.0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Are you sure you want to EXIT?");
        Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.get() == javafx.scene.control.ButtonType.OK) {
            viewModel.exitGame();
        } else {

            alert.close();
        }

    }

    //stop or start music button
    public void mute(ActionEvent actionEvent) {
        if (isMusic){
            viewModel.mute(true);
            isMusic = false;
        }
        else
        {
            viewModel.mute(false);
            isMusic=true;
            viewModel.pauseMusic();
            try{
                viewModel.playMusic((new Media(getClass().getResource("/Music/song.mp3").toURI().toString())),200.0);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


}
