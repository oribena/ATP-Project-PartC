package sample;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.Optional;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.geometry.Pos.CENTER;

public class Main extends Application {

    private  MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MyModel model = MyModel.getInstance();
        MyViewModel viewModel = MyViewModel.getInstance();
        model.addObserver(viewModel);
        Parent root = FXMLLoader.load(getClass().getResource("../View/MyView.fxml"));
        primaryStage.setTitle("F.R.I.E.N.D.S Maze");
        primaryStage.setScene(new Scene(root, 960, 614));
        //exit game
        setStageCloseEvent(primaryStage, model);
        primaryStage.show();

        ////WELCOME SCENE
        try {
            Stage stage = new Stage();
            setStageCloseEvent(stage, model);
            stage.setTitle("F.R.I.E.N.D.S Maze");
            VBox layout = new VBox();
            layout.setAlignment(CENTER);
            Button close = new Button();
            close.setText("PLAY");
            Label label = new Label("\n\n\n\n\n\n ");
            label.setAlignment(CENTER);
            label.setLineSpacing(2);
            layout.spacingProperty().setValue(30);


            layout.getChildren().addAll(label, close);
            close.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            Scene scene = new Scene(layout, 960, 614);
            scene.getStylesheets().add(getClass().getResource("/View/WelcomeStyle.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
    }

    ///close the game
    private void setStageCloseEvent(Stage primaryStage, MyModel model) {
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to EXIT?");
            playAudio("resources/Music/break.mp3");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                model.close();
                primaryStage.close();
                MyViewModel.getInstance().exitGame();
            } else {
                event.consume();
            }

        });
    }

    public static void exitGame(){
        Platform.exit();
        System.exit(0);
    }
    protected void playAudio(String audio) {
        String musicFile = audio;
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
