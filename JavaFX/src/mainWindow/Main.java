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

        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Sõiduplaan");
        tab1.setContent(root);
        tabPane.getTabs().add(tab1);

        Parent favorites = FXMLLoader.load(getClass().getResource("secondWindow.fxml"));
        Tab tab2 = new Tab("Ajalugu");
        tab2.setContent(favorites);
        tabPane.getTabs().add(tab2);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        primaryStage.setScene(new Scene(tabPane));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
