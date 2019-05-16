package mainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Sõiduplaan");

        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(440);
        primaryStage.setMinWidth(680);
        primaryStage.setMaxHeight(440);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
