package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Class containing the main method of our project
 */
public class Main extends Application
{
	public static Stage stage;
	static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	
	
	@Override
	/**
	 * Main stage of our project
	 */
    public void start (Stage primaryStage) throws Exception
	{
        Parent root = FXMLLoader.load(getClass().getResource("startPageGUI.fxml"));
		stage = primaryStage;
        stage.setTitle("Start Page");
        stage.setScene(new Scene(root, screenSize.getWidth(), screenSize.getHeight()));
        stage.setMaximized(true);
        stage.show();
    }
	
	/**
	 * Main method of our project
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}