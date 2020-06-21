package View;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class propertiesController implements Initializable{
    public javafx.scene.control.TextField numOfT;
    public javafx.scene.control.Button okB;
    public javafx.scene.control.ComboBox Generator;
    public javafx.scene.control.ComboBox Search;

    public void OK(){

        try {
            FileInputStream in = new FileInputStream("resources/config.properties");
            Properties p = new Properties();
            p.load(in);
            in.close();
            if(!isValidInt(numOfT.getText()) || numOfT.getText().trim().isEmpty()){
                MyViewController.showAlert("Enter an integer in num of threads field");
                return;
            }
            if(Integer.valueOf(numOfT.getText()) < 1){
                MyViewController.showAlert("Num of threads needs to be bigger than zero");
                return;
            }


            FileOutputStream out = new FileOutputStream("resources/config.properties");
            p.setProperty("Server.threadPoolSize", numOfT.getText());
            p.setProperty("SearchingAlgorithm", Search.getSelectionModel().getSelectedItem().toString());
            p.setProperty("MazeGenerator", Generator.getSelectionModel().getSelectedItem().toString());
            p.store(out, null);
            out.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return;
        }


        MyViewController.showAlert("The Properties has changed successfully!");
        Stage s = (Stage)okB.getScene().getWindow();
        s.close();

    }
    public  static boolean isValidInt(String s){
        try{
            int x = Integer.parseInt(s);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Generator.getItems().addAll(
                "MyMazeGenerator",
                "SimpleMazeGenerator"
        );
        Search.getItems().addAll(
                "BestFirstSearch",
                "BreadthFirstSearch",
                "DepthFirstSearch"
        );
        try {
            FileInputStream in = new FileInputStream("resources/config.properties");
            Properties p = new Properties();
            p.load(in);
            in.close();
            Search.setValue(p.getProperty("SearchingAlgorithm"));
            Generator.setValue((p.getProperty("MazeGenerator")));
            numOfT.setText(p.getProperty("Server.threadPoolSize"));
        }
        catch(IOException e)
        {

        }
    }
}