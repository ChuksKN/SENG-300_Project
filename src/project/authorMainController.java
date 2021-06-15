package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
import javafx.scene.control.ComboBox;

/**
* Controller class for the author GUI.
* Allows the author to: 
* Select a drop box where they would submit their journal and proceed to the authorUpDown page.
* Select a drop box to see the feedback of the journal that they submitted.
*/
public class authorMainController implements Initializable
{
	private static int checker;
	private String currentUserEmail;
	private ArrayList<String> decisionAndFeedback = new ArrayList<String>();
   
	@FXML
    private Button logOut;
   
    @FXML
    private Button notifications;
    
    @FXML
    private Button home;	    
    
    @FXML
    private Button uploadJournalNext;
    
    @FXML
    private Button viewFeedbackNext;
    
    @FXML
    private ComboBox<String> dropboxForUploadJournal;
    
    @FXML
    private ComboBox<String> dropboxForDownloadFeedback;

    @FXML
	/**
     * This function takes the user to the author's main page.
     * @param event - the button click event 
     * @throws Exception
     */
	private void authorMainGUI(ActionEvent event) throws Exception
    {		   
    	Parent root = FXMLLoader.load(getClass().getResource("authorMainGUI.fxml"));
    	Main.stage.setTitle("Homepage - Author");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
	}
    
    @FXML
    /**
     * This function switches from the author's main page to the notification page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void goToNotifications (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("authorNotificationGUI.fxml"));
    	Main.stage.setTitle("Notifications");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
        
    @FXML
	/**
     * This function switches from the author's main page to the start page 
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
     * This function switches from the author's main page to the authorUpDown page 
     * @param event - the button click event 
     * @throws Exception
     */
   private void authorUpDownGUI(ActionEvent event) throws Exception
   {		   
	   Parent root = FXMLLoader.load(getClass().getResource("authorUpDownGUI.fxml"));
	   Main.stage.setTitle("Upload/Downlaod");
	   Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
   }
   
   /**
    * Setter method for the integer checker.
    * @param aChecker
    */
   public static void setChecker(int aChecker) {
	   checker = aChecker;
   }
   
   /**
    * Getter method for the integer checker.
    * @return authorMainController.checker
    */
   public int getChecker() {
	   return authorMainController.checker;
   }
   
   /**
    * This function reads from the dropboxes.csv and 
    * writes those information to the drop down menu associated with the "Upload Journal" button 
    */
   public void getDropboxes() {
    	Path dropboxPath = Paths.get(System.getProperty("user.dir"),"dropboxes.csv");
    	String dropboxId;
    	String dropboxName;
    	String dueDate;
    	
    	ArrayList<String> dropboxList = new ArrayList<String>(); 
    	try {
    		Scanner s = new Scanner(new File(dropboxPath.toString()));
    		s.useDelimiter("[,\n]");
    		while(s.hasNext()) {
    			dropboxId = s.next();
    			dropboxName = s.next();
    			s.next();
    			dueDate = s.next();
    			
				if(dueDate.replaceAll("\\r|\\n", "").equals("9999-99-99"))
				{
					dueDate = "no deadline";
					String dropboxInfo = dropboxName+","+dropboxId+","+dueDate;
					dropboxList.add(dropboxInfo);	   
				}
				else {
					String dropboxInfo = dropboxName+","+dropboxId+","+dueDate.replaceAll("\\r|\\n", "");
					dropboxList.add(dropboxInfo);	   
				}	 			
    		}
    		s.close();
    		dropboxForUploadJournal.getItems().addAll(dropboxList);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
   }
   	
   /**
    * This function populates the drop down menu associated with the "View Feedback" button 
    * with the drop boxes of the journals that has been reviewed and given a feedback.
    */
   public void getDropboxesWithFeedback() {
	   Path currentUserPath = Paths.get(System.getProperty("user.dir"),"current_user.csv");
		
		try 
		{
			Scanner currentUserscanner = new Scanner(new File(currentUserPath.toString()));
			currentUserscanner.useDelimiter("[,\n]");
					
			while(currentUserscanner.hasNext())
			{
				currentUserEmail = currentUserscanner.next(); //email
				currentUserscanner.next(); //id
				currentUserscanner.next(); //date_time
				
				System.out.println("Current user's email: "+currentUserEmail);
			}
			
			currentUserscanner.close();
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
	   	Path dropboxPath = Paths.get(System.getProperty("user.dir"),"final_feedback.csv");
	   	String journalId;
	   	String usersEmail;
	   	String dropboxName;
    	String dropboxId;
    	String currentFeedbackFilePath;
    	String finalDecision;
   
    	
    	ArrayList<String> dropboxList = new ArrayList<String>(); 
    	
    	try {
    		Scanner s = new Scanner(new File(dropboxPath.toString()));
    		s.useDelimiter("[,\n]");
    		
    		while(s.hasNext()) {
    			journalId = s.next();
    			usersEmail = s.next();
    			currentFeedbackFilePath = s.next();
    			finalDecision = s.next();
    			dropboxName = s.next();
    			dropboxId = s.next();
    			
    			if(currentUserEmail.equals(usersEmail)) {
    				String dropboxInfo = dropboxName+","+dropboxId.replaceAll("\\r|\\n", "")+","+journalId;//+","+
    				String dropboxInfor2 = finalDecision+","+currentFeedbackFilePath;
    				decisionAndFeedback.add(dropboxInfor2);
	    			dropboxList.add(dropboxInfo);	    			
    			}
    		}
    		s.close();
    		dropboxForDownloadFeedback.getItems().addAll(dropboxList);
    
    	}catch(Exception e){
    		e.printStackTrace();
    	}
   }
   
   /**
	* This function initializes the JavaFx controllers on the authorMain page.
	*/
   public void initialize(URL url, ResourceBundle rb) 
   {
   		getDropboxes();
   		getDropboxesWithFeedback();
   		
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
    	
    	home.setOnAction(arg0 ->
    	{
    		try
    		{
    			authorMainGUI(arg0);
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
    	
    	viewFeedbackNext.setOnAction(arg0 ->
    	{
    		String chosenDropbox = dropboxForDownloadFeedback.getSelectionModel().getSelectedItem();
    		String filepath = "selected_dropbox_for_download_feedback.csv";
    		int index = dropboxForDownloadFeedback.getSelectionModel().getSelectedIndex();
    		
    		try
    		{
    			if(chosenDropbox != null) {
    				FileWriter fwRev = new FileWriter(filepath, false);
    	    		BufferedWriter bwRev = new BufferedWriter(fwRev);
    	    		PrintWriter pwRev = new PrintWriter(bwRev);
    	    		pwRev.println(decisionAndFeedback.get(index));
    	    		pwRev.flush();
    	    		pwRev.close();
    			setChecker(1);
    			authorUpDownGUI(arg0);
    			}
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	});
    	
    	uploadJournalNext.setOnAction(arg0 ->
    	{
    		String chosenDropbox = dropboxForUploadJournal.getSelectionModel().getSelectedItem();
    		String filepath = "selected_dropbox_for_upload_journal.csv";
    		try
    		{
    			if(chosenDropbox != null) {
    				FileWriter fwRev = new FileWriter(filepath, false);
    	    		BufferedWriter bwRev = new BufferedWriter(fwRev);
    	    		PrintWriter pwRev = new PrintWriter(bwRev);
    	    		pwRev.println(chosenDropbox);
    	    		pwRev.flush();
    	    		pwRev.close();
    			setChecker(0);
    			authorUpDownGUI(arg0);
    			}
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	});
    }	   
}