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
* Controller class for the editor GUI.
* Allows the editor to:
* go to the editorCreateDropbox page.
* go to the editorForAuthor page.
* go to the editorForReviewer page.
*/
public class editorMainController implements Initializable
{
    @FXML
    private Button editorForAuthorButton;
    
    @FXML
    private Button editorForReviewerButton;
    
    @FXML
    private Button editorCreateDropboxButton;
    
    @FXML
    private Button home;
    
    @FXML
    private Button notifications;
    
    @FXML
    private Button logOut;
    
    @FXML
    /**
     * This function take the user to the editor main page
     * @param event - the button click event  
     * @throws Exception
     */
    private void editorMainGUI (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("editorMainGUI.fxml"));
    	Main.stage.setTitle("Homepage - Editor");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
			
    @FXML
    /**
     * This function switches from the editorMain page to the start page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void logOut(ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("startPageGUI.fxml"));
    	Main.stage.setTitle("Start Page");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }	   
    
    @FXML
    /**
     * This function switches from the main Editor page to the editorForAuthorGUI page 
     * @param event - the button click event  
     * @throws Exception
     */
    private void editorForAuthorGUI (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("editorForAuthorGUI.fxml"));
    	Main.stage.setTitle("Author Submissions");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }

    @FXML
    /**
     * This function switches from the main Editor page to the editorForReviewerGUI page 
     * @param event - the button click event  
     * @throws Exception
     */
    private void editorForReviewerGUI (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("editorForReviewerGUI.fxml"));
    	Main.stage.setTitle("Reviewer Feedback");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
    
    @FXML
    /**
     * This function switches from the main Editor page to the editorCreateDropbox page 
     * @param event - the button click event  
     * @throws Exception
     */
    private void createDropBoxGUI (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("editorCreateDropboxGUI.fxml"));
    	Main.stage.setTitle("Create a new Dropbox");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
    
    /**
     * This function adds the event handlers to the buttons 
     */
    public void initialize(URL url, ResourceBundle rb)
    {
    	editorForAuthorButton.setOnAction(arg0 ->
    	{
    		try
			{
    			editorForAuthorGUI(arg0);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
    	
    	editorForReviewerButton.setOnAction(arg0 ->
       {
    	   	try
    	   	{
    	   		editorForReviewerGUI(arg0);
    	   	}
    	   	catch (Exception e)
    	   	{
    	   		e.printStackTrace();
			}
       });
    	
    	editorCreateDropboxButton.setOnAction(arg0 ->
        {
     	   	try
     	   	{
     	   		createDropBoxGUI(arg0);
     	   	}
     	   	catch (Exception e)
     	   	{
     	   		e.printStackTrace();
 			}
        });
    	
    	home.setOnAction(arg0 ->
		{
			try
			{
				editorMainGUI(arg0);
			}
			catch(Exception e)
    		{
    			e.printStackTrace();
    		}
		});
		
		logOut.setOnAction(arg0 ->
		{
			try
			{
				logOut(arg0);
			}
			catch(Exception e)
    		{
    			e.printStackTrace();
    		}
		});
    }    
}