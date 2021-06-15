package project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
* Controller class for the start page GUI.
* Allows the user to choose whether to create an account by signing up or to login in as an editor, author, or reviewer.
*/
public class startPageController implements Initializable
{
    @FXML
    private Button signupButton;
    
    @FXML
    private Button loginButton;
    
    @FXML
    /**
     * This function switches from the start page to the sign up page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void addUserGUI (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("addUserGUI.fxml"));
    	Main.stage.setTitle("Sign Up");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));	
    }

    @FXML
    /**
     * This function switches from the start page to the log in page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void loginUserGUI (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("LoginUserGUI.fxml"));
    	Main.stage.setTitle("Login");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }

    /**
   	 * This function initializes the JavaFx controllers on the start page.
   	 */
    public void initialize(URL url, ResourceBundle rb)
    {
		signupButton.setOnAction(arg0 ->
		{
			try
			{
				addUserGUI(arg0);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
    	
		loginButton.setOnAction(arg0 ->
		{
    	   	try
    	   	{
    	   		loginUserGUI(arg0);
    	   	}
    	   	catch (Exception e)
    	   	{
    	   		e.printStackTrace();
			}
		});
    }
}
