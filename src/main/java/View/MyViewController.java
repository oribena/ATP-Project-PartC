package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.event.EventHandler;
import static javafx.geometry.Pos.CENTER;

public class MyViewController implements IView,Observer , Initializable {

    protected MyViewModel viewModel = MyViewModel.getInstance();

    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Label lbl_player_row;
    @FXML
    public Label lbl_player_column;
    @FXML
    public Button buttonGenerateMaze;
    @FXML
    public Button buttonSolveMaze;
    @FXML
    public Button mute;
    @FXML
    public Button buttonHideMaze;

    public boolean isMusic =true;

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
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
        if (viewModel.isWonGame() == true)
        {
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel)  { //if (o == viewModel)
            //setViewModel(viewModel);
            if (arg == "generate") {
                isMusic = true;
                mazeDisplayer.setMaze(viewModel.getMaze());
                mazeDisplayer.set_goal_position(viewModel.getGoalPosRow(), viewModel.getGoalPosCol());
                mazeDisplayer.set_player_position(viewModel.getCurrPosRow(), viewModel.getCurrPosCol());
                mazeDisplayer.drawMaze(mazeDisplayer.getMaze());
                buttonGenerateMaze.setDisable(false);
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
//                    showAlert("YOU WON!");
                    //Music
                    viewModel.pauseMusic();
                    try{
                        viewModel.playMusic((new Media(getClass().getResource("/Music/claps.mp3").toURI().toString())),400.0);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Stage stage = new Stage();
                    stage.setTitle("YOU WON!!!");
                    VBox layout = new VBox();
                    HBox H = new HBox(5);
                    H.setAlignment(CENTER);
                    layout.setAlignment(CENTER);
                    Button close = new Button();
                    close.setText("play again!");
                    H.getChildren().add(close);
                    layout.spacingProperty().setValue(10);
                    Image im = new Image("/Images/wonGif.gif");
                    ImageView image = new ImageView(im);
                    layout.getChildren().add(image);
                    layout.getChildren().add(H);
                    Scene scene = new Scene(layout, 520, 350);
                    scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                    stage.show();
                    //close button
                    close.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            stage.close();
                        }
                    });
                }

                else
                    mazeDisplayer.set_player_position(viewModel.getCurrPosRow(), viewModel.getCurrPosCol());
            }
            else if (arg == "solve") {
                mazeDisplayer.writeSolution(viewModel.getSolution());
            }
        }
    }

    public static void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void generateMaze()
    {
        viewModel.addObserver(this);
        String strRows = textField_mazeRows.getText();
        String strCols = textField_mazeColumns.getText();
        if (isValidNumber(strRows) && isValidNumber(strCols)) {
            int rows = Integer.valueOf(strRows);
            int cols = Integer.valueOf(strCols);
            viewModel.generateMaze(rows, cols);
            buttonSolveMaze.setDisable(false);
            buttonHideMaze.setDisable(true);

            mute.setDisable(false);

        }
        else
            showAlert("wrong INPUT");
    }

    public boolean isValidNumber(String str)
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
//        viewModel.SolveMaze();
    }

    public void hideMaze(ActionEvent actionEvent) {
        mazeDisplayer.drawMaze(mazeDisplayer.getMaze());
        buttonHideMaze.setDisable(true);

    }
//    public void About(ActionEvent actionEvent){
//        try{
//            Stage stage=new Stage();
//            stage.setTitle("About");
//            FXMLLoader fxmlLoader=new FXMLLoader();
//            Parent root=fxmlLoader.load(getClass().getResource("About.fxml").openStream());
//            Scene scene=new Scene(root,600,600);
//            stage.setScene(scene);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//        }catch (Exception e){
//
//        }
//    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("F.R.I.E.N.D.S Maze");
            VBox layout = new VBox();
            layout.setAlignment(CENTER);
            Button close = new Button();
            close.setText("Back");
            Label label = new Label("Hello,\n" +
                    "Help friends reach the central perk cafe safetly!! \n" +
                    "Algorithms used: BestFirstSearch, BreadthFirstSearch, DepthFirstSearch\n\n" +
                    "Developers:\n" + "Noaa Kless\n" + "& \n" + "Ori Ben-Artzy\n" + "HAVE FUN!");
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
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }

    public void Help(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("F.R.I.E.N.D.S Maze");
            VBox layout = new VBox();
            layout.setAlignment(CENTER);
            Button close = new Button();
            close.setText("Back");
            Label label = new Label("Guide,\nPress: \nNUMPAD 2 - To go UP \nNUMPAD 8 - To go DOWN \nNUMPAD 6 - To go RIGHT \nNUMPAD 4 - To go LEFT \nNUMPAD 3 - To go UP & RIGHT \nNUMPAD 1 - To go UP & LEFT \nNUMPAD 9 - To go DOWN & RIGHT \nNUMPAD 7 - To go DOWN & LEFT \n\nYou can save/load the mazes.\n And if you have a problem with the maze - I'LL BE THERE FOR YOU!!\n You can see the solution by pressing the solve button.");
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
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }

//    public void Help(ActionEvent actionEvent){
//        try{
//
//            Stage stage=new Stage();
//            stage.setTitle("Help");
//            FXMLLoader fxmlLoader=new FXMLLoader();
//            Parent root=fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
//            Scene scene=new Scene(root,600,600);
//            stage.setScene(scene);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//        }catch (Exception e){
//
//        }
//    }

//    public void Properties(ActionEvent actionEvent) {
//        try {
//            Stage stage = new Stage();
//            stage.setTitle("Properties Configuration");
//            VBox layout = new VBox();
//            HBox H = new HBox(3);
//
//            Label genLabel = new Label("Choose Generator:");
//            ComboBox<String> genCmb = new ComboBox<>();
//            genCmb.setPromptText("Generating Algorithm");
//            genCmb.getItems().addAll("SimpleMazeGenerator", "MyMazeGenerator");
//            H.getChildren().addAll(genLabel,genCmb);
//            layout.getChildren().add(H);
//            H.setAlignment(CENTER);
//
//            HBox H1 = new HBox(3);
//            Label solLabel = new Label("Choose Solver:");
//            ComboBox<String> solCmb = new ComboBox<>();
//            solCmb.setPromptText("Solving Algorithm");
//            solCmb.getItems().addAll("DepthFirstSearch", "BreadthFirstSearch","BestFirstSearch");
//            H1.getChildren().addAll(solLabel,solCmb);
//            layout.getChildren().add(H1);
//            H1.setAlignment(CENTER);
//
//            layout.setAlignment(CENTER);
//
//            Button close = new Button();
//            close.setText("Set Properties");
//            layout.spacingProperty().setValue(30);
//            layout.getChildren().addAll(close);
//            close.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    FileOutputStream out = null;
//                    try {
//                        out = new FileOutputStream("./resources/config.properties");
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    String str = "SearchingAlgorithm = "+solCmb.getValue() +"\nMazeGenerator = "+genCmb.getValue() ;
//
//                    byte[] bytesss = str.getBytes();
//                    try {
//                        out.write(bytesss);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    stage.close();
//                }
//            });
//            Scene scene = new Scene(layout, 768, 614);
//            scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
//            stage.setScene(scene);
//            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
//            stage.show();
//
//
//        } catch (Exception e) {
//        }
//    }


    public void Properties() {
        try {
            Stage stage = new Stage();
            stage.setTitle("F.R.I.E.N.D.S Maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = (Parent)fxmlLoader.load(this.getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 960.0D, 614.0D);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/View/LoadScene.css").toExternalForm());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception var5) {
        }

    }

    public void load() {
        viewModel.load();
    }
    public void save(){
        viewModel.save();
    }

    public void Exit(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to EXIT?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
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
