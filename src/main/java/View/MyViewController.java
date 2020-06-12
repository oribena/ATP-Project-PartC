package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MyViewController implements IView{

    private MyViewModel viewModel;
    //public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.Button Start;
    public javafx.scene.control.Button CMaze;
    public javafx.scene.control.Button Solve;
    public javafx.scene.control.TextField mazeFileName;
    public javafx.scene.control.Button Save;
    public javafx.scene.control.Label labelM;
    public javafx.scene.control.MenuItem saveMenu;


    public void pushButton(ActionEvent actionEvent) {
        System.out.println("test");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("hiiii");
        alert.show();
    }

    public static void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void generateMaze() {
        int heigth = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        if ( heigth <= 0 || width <= 0 )
            showAlert("wrong INPUT");
        btn_generateMaze.setDisable(true);
        btn_solveMaze.setDisable(false);
        //viewModel.generateMaze(width, heigth);
    }







    public void About(ActionEvent actionEvent){
        try{
            System.out.println("test");
            Stage stage=new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader=new FXMLLoader();
            Parent root=fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene=new Scene(root,600,600);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){

        }
    }
    public void Help(ActionEvent actionEvent){
        try{

            Stage stage=new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader=new FXMLLoader();
            Parent root=fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene=new Scene(root,600,600);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){

        }
    }


    public void Properties() {
        try {
            Stage stage = new Stage();
            stage.setTitle("properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = (Parent)fxmlLoader.load(this.getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 600.0D, 600.0D);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception var5) {
        }

    }

    public void Exit(ActionEvent actionEvent) {
        viewModel.exitGame();
    }

}
