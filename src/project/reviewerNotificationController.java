package project;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
* Controller class for the notification page of the reviewer.
*/
public class reviewerNotificationController implements Initializable
{
	
	@FXML
	private Button logOut;

	@FXML
	private Button notifications;

	@FXML
	private Button home;	
	
	@FXML
	private ListView<String> listViewID;
	
    @FXML
    /**
     * This function switches from the reviewerNotification page to the main reviewer page 
     * @param event - the button click event 
     * @throws Exception
     */
	private void reviewerMainGUI(ActionEvent event) throws Exception{		   
    	Parent root = FXMLLoader.load(getClass().getResource("reviewerMainGUI.fxml"));
    	Main.stage.setTitle("Homepage - Reviewer");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
	}
    
    @FXML
    /**
     * This function switches from the reviewerNotification page to the start page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void logOut(ActionEvent event) throws Exception{
    	Parent root = FXMLLoader.load(getClass().getResource("startPageGUI.fxml"));
    	Main.stage.setTitle("Start Page");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
    
    @FXML
    /**
     * This function takes the reviewer to the notification page.
     * @param event - the button click event 
     * @throws Exception
     */
    private void goToNotifications (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("reviewerNotificationGUI.fxml"));
    	Main.stage.setTitle("Notifications");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
    
    /**
     * This function writes the notification stating that 
     * the reviewer has been assigned a journal to review.  
     */
    private void readNotificationCSV()
    {
    	Path path = Paths.get(System.getProperty("user.dir"),"reviewer_notifications.csv");
    	Path path2 = Paths.get(System.getProperty("user.dir"),"current_user.csv");
    	
    	String journalID, rev1, rev2, rev3, dateLog;
		String curEmail = null;
    	
    	ArrayList<String> arrNotif = new ArrayList<String>();
    	
    	try 
		{
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			
			Scanner scanner2 = new Scanner(new File(path2.toString()));
			scanner2.useDelimiter("[,\n]");
			
			while(scanner2.hasNext()) {
				curEmail = scanner2.next();
				scanner2.next();
				scanner2.next();
			}
			scanner2.close();
			
			while(scanner.hasNext())
			{	
				String miscNotif = "";

				scanner.next();
				rev1 = scanner.next(); 
				scanner.next(); 
				rev2 = scanner.next();
				scanner.next();
				rev3 = scanner.next();
				dateLog = scanner.next();
				scanner.next();
				journalID = scanner.next();

				if (rev1.equals(curEmail) || rev2.equals(curEmail) || rev3.equals(curEmail)) {
					miscNotif = "You have been assigned to review a journal entry!\n"+"Journal ID: "+journalID+"\tDate: "+dateLog;
					arrNotif.add(miscNotif);
				}
			}
			scanner.close();
			listViewID.getItems().addAll(arrNotif);

		}
		catch(Exception e)
		{
			String warning = "There seems to be a problem";
			listViewID.getItems().add(warning);
			e.printStackTrace();
		}
    
    }
    
    /**
   	 * This function initializes the JavaFx controllers on the reviewerNotification page.
   	 */
    public void initialize(URL url, ResourceBundle rb)
    {
    	readNotificationCSV();
    	
    	home.setOnAction(arg0 ->
    	{
    		try
    		{
    			reviewerMainGUI(arg0);
    		}
    		catch (Exception e)
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
    	
    	notifications.setOnAction(arg0 ->
		{
			try
			{
				goToNotifications(arg0);
			}
			catch(Exception e)
    		{
    			e.printStackTrace();
    		}
		});
       
    }
}