package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

/**
* Controller class for the reviewer GUI.
* Allows the reviewer to select a journal that has been assigned to them to review.
*/
public class reviewerMainController
{
	private String currentUserEmail;
	private String id;
	private String file;
	private String reviewer1email;
	private String reviewer2email;
	private String reviewer3email;
	
	ArrayList<String> journalsToReview = new ArrayList<String>();
	
    @FXML
    private ChoiceBox<String> chooseJournalToReview;

    @FXML
    private Button home;

    @FXML
    private Button notifications;

    @FXML
    private Button logOut;

    @FXML
    private Button submitJournalToReview;
    
    @FXML
    private Label chooseJournalError;
    
    @FXML
    /**
     * This takes the user to the reviewer main page.
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
     * This function switches from the reviewer main page to the reviewerUpDown page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void reviewerUpDownGUI(ActionEvent event) throws Exception{		   
    	Parent root = FXMLLoader.load(getClass().getResource("reviewerUpDownGUI.fxml"));
    	Main.stage.setTitle("Upload/Downlaod");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
	}
    
    @FXML
    /**
     * This function switches from the reviewer main page to the start page 
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
     * This function switches from the reviewer's main page to the notification page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void goToNotifications (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("reviewerNotificationGUI.fxml"));
    	Main.stage.setTitle("Notifications");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
    
    @FXML
	/**
	 * This function populates the drop down menu with the journal(s) that has been assigned to the reviewer. 
	 * Once a journal has been selected, the journal's file path and ID is written in to journal_being_reviewed.csv.
	 * @param event - button click event
	 * @throws Exception
	 */
	private void loadJournals(ActionEvent event) throws Exception
	{
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
			
		Path assignedJournalPath = Paths.get(System.getProperty("user.dir"),"assigned_journals.csv");

		try  
		{
			Scanner scanner = new Scanner(new File(assignedJournalPath.toString()));
			scanner.useDelimiter("[,\n]");
			
			while(scanner.hasNext())
				{
					StringBuilder str = new StringBuilder();
					id = scanner.next(); //id
					file = scanner.next(); //file
					scanner.next(); //reviewer1
					reviewer1email = scanner.next(); //reviewer1 email
					scanner.next(); //reviewer2 email
					reviewer2email = scanner.next(); //reviewer2 email
					scanner.next(); //reviewer3 email
					reviewer3email = scanner.next(); //reviewer3 email
					System.out.println("R1's email: "+reviewer1email);
					System.out.println("R2's email: "+reviewer2email);
					System.out.println("R3's email: "+reviewer3email);
					
					if(currentUserEmail.trim().contentEquals(reviewer1email.trim()) || currentUserEmail.trim().contentEquals(reviewer2email.trim()) || currentUserEmail.trim().contentEquals(reviewer3email.trim()))
					{
						str.append(file+","+id);
						journalsToReview.add(str.toString());
						System.out.println("Added 1 Journal.");
					}
					else
					{
						System.out.println("No journal added.");
					}
				}
				scanner.close();
				
				chooseJournalToReview.getItems().addAll(journalsToReview);	
		}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
	}
    
    @FXML
    /**
	 * This function initializes the JavaFx controllers on the reviewerMain page.
	 */
    private void initialize() {
    	
    	try
		{
			loadJournals(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    	
        submitJournalToReview.setOnAction(arg0 ->
    	{
    		String chosenJournal = chooseJournalToReview.getSelectionModel().getSelectedItem();
        	chooseJournalError.setText("");
        	String filepath = "journal_being_reviewed.csv";

    		try{
    			if(chosenJournal != null) {
    				FileWriter fwRev = new FileWriter(filepath, false);
    	    		BufferedWriter bwRev = new BufferedWriter(fwRev);
    	    		PrintWriter pwRev = new PrintWriter(bwRev);
    	    		pwRev.println(chosenJournal);
    	    		pwRev.flush();
    	    		pwRev.close();
    	    		
    				reviewerUpDownGUI(arg0);
    	    	}else {
    	    		chooseJournalError.setText("Choose A Journal To Review!");
    	    		chooseJournalError.setAlignment(Pos.CENTER);
    	    		chooseJournalError.setStyle("-fx-background-color: #ff652f");
    	    	}
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
