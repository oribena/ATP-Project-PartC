package sample;

import Model.IModel;
import Model.MyModel;
import View.MyViewController;
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
import javafx.stage.Stage;

import javax.imageio.IIOParam;
import java.net.URISyntaxException;
import java.util.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends Application {

    public static void exitGame(){
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //FXMLLoader fxmlLoader = new FXMLLoader();
        //Parent root = fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
        MyModel model = MyModel.getInstance();
        MyViewModel viewModel = MyViewModel.getInstance();
        model.addObserver(viewModel);
        Parent root = FXMLLoader.load(getClass().getResource("../View/MyView.fxml"));
        primaryStage.setTitle("F.R.I.E.N.D.S Maze");
        primaryStage.setScene(new Scene(root, 768, 614));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
