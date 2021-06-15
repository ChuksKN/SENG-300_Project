package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
* Controller class for the reviewer GUI.
* Allows the reviewer to:
* download the journal for them to read and review.
* upload a PDF file containing the feedback of the journal they reviewed which will be sent to the editor. 
* decide on an evaluation result which will determine if the journal will be published or not.
*/
public class reviewerUpDownController {
	
	
	private ObservableList<String> decisionList = FXCollections.observableArrayList(null, "Accepted", "Rejected", "Major Revision", "Minor Revision");
	private List<String> lstFile;
	private String csvFile = "current_user.csv";
    private BufferedReader br = null;
    private String line = "";
    private String cvsSplitBy = ",";

    @FXML
    private Button downloadJournal;

    @FXML
    private Button uploadFeedback;

    @FXML
    private ChoiceBox<String> decisionChoiceBox;

    @FXML
    private Button submitDecision;

    @FXML
    private Button signOut;
  
    @FXML
    private Button home;
  
    @FXML
    private Button notifications;

    @FXML
    private Button logOut;
    
    @FXML
    private Label uploadError;

    @FXML
    private Label choiceBoxError;
    
    @FXML
    private Label feedbackLabel;
    
    @FXML
    /**
     * This function switches from the reviewerUpDown page to the main reviewer page 
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
     * This function switches from the reviewerUpDown page to the start page 
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
     * This function switches from the author's main page to the notification page 
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
     * This function opens the system explorer where the reviewer can select the PDF file containing his/her feedback of the journal
     * @param event - the button click event 
     */
    private void onUploadClick(ActionEvent event) {
    	FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", lstFile));
    	File f = fc.showOpenDialog(null);
    	if(f != null) {
    		feedbackLabel.setText(f.getAbsolutePath());
    		feedbackLabel.setAlignment(Pos.CENTER);
    		feedbackLabel.setStyle("-fx-background-color: #14a75c");
    		
    		uploadError.setText("");
    		uploadError.setAlignment(Pos.CENTER);
    		uploadError.setStyle("-fx-background-color: #3d4956");
    	}
    }
    
    @FXML
    /**
     * This function takes the journal ID of the journal uploaded, the email of the reviewer, the file path of the reviewer's feedback,
     * and the reviewers decision regarding the journal(Accepter, Rejected, Major/Minor Revision).
     * @param event - the button click event 
     * @throws IOException
     */
    private void onSubmitClick(ActionEvent event) throws IOException{
    	String feedbackInput = feedbackLabel.getText();
    	String selectedDecision = decisionChoiceBox.getSelectionModel().getSelectedItem();
    	choiceBoxError.setText("");
    	choiceBoxError.setAlignment(Pos.CENTER);
    	choiceBoxError.setStyle("-fx-background-color: #3d4956");
 
    	if(feedbackInput != "" && selectedDecision != null) {
    		File source = new File(feedbackInput);
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("(yyyy-MM-dd_HH-mm-ss)");  
	    	LocalDateTime now = LocalDateTime.now();  
	    	
	    	File dest = new File(".\\ReviewerFeedback\\ReviewerFeedback_"+dtf.format(now)+".pdf");
	    	Files.copy(source.toPath(), dest.toPath());
	    	String destFeedbackPath = dest.toString();
	    	
	    	String filepath = "reviewer_decision.csv";
	    	String journalID = getJournalID();
	    	
	    	try {
				FileWriter fwRev = new FileWriter(filepath, true);
				BufferedWriter bwRev = new BufferedWriter(fwRev);
				PrintWriter pwRev = new PrintWriter(bwRev);
				String email = "";
	
		        br = new BufferedReader(new FileReader(csvFile));
		        while ((line = br.readLine()) != null) {
		            String[] reviewerInfo = line.split(cvsSplitBy);
		            email = reviewerInfo[0];
		        }
					
				pwRev.println(journalID+","+email+","+destFeedbackPath+","+selectedDecision); //!
				pwRev.flush();
				pwRev.close();	
				JOptionPane.showMessageDialog(null, "File Sent:\n"+destFeedbackPath);	
				
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "File Not Sent");
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
    	}else 
    	{
			if(feedbackInput == "" && selectedDecision == null) {
				uploadError.setText("You Must Upload your Feedback!");
				uploadError.setAlignment(Pos.CENTER);
				uploadError.setStyle("-fx-background-color: #ff652f");
				choiceBoxError.setText("You Must Choose an Evaluation Result!");
				choiceBoxError.setAlignment(Pos.CENTER);
				choiceBoxError.setStyle("-fx-background-color: #ff652f");
			}else if(feedbackInput != "" && selectedDecision == null) {
				choiceBoxError.setText("You Must Choose an Evaluation Result!");
				choiceBoxError.setAlignment(Pos.CENTER);
				choiceBoxError.setStyle("-fx-background-color: #ff652f");
			}else if(feedbackInput == "" && selectedDecision != null){
				uploadError.setText("You Must Upload your Feedback!");
				uploadError.setAlignment(Pos.CENTER);
				uploadError.setStyle("-fx-background-color: #ff652f");
			}
    	}
    } 
    
    @FXML
    /**
     * This function opens the journal that was assigned to the reviewer.
     * @param event -  button click event.
     */
    private void onDownloadClick(ActionEvent event){
 
    	try{
	    	String path = getAuthorsJournal(); // Test Path
	
	    	File file = new File(path);

	    	if(file.exists())
	    	{
	    		Process pro = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
	    		pro.waitFor(); 
	    	}
	    	else
	    	{
	    		System.out.println("In the else!");
	    	}
    	} 
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    /**
     * This function reads the journal_being_reviewed.csv and gets the journal ID.
     * @return journalID - the ID of the journal being reviewed by current reviewer.
     */
    private String getJournalID() {
    	Path path = Paths.get(System.getProperty("user.dir"),"journal_being_reviewed.csv");
        BufferedReader brID = null;
    	String journalID = "";
    	
    	try 
    	{
    		brID = new BufferedReader(new FileReader(path.toString()));
	        while ((line = brID.readLine()) != null) {
	            String[] journalBeingReviewedInfo = line.split(",");
	            journalID = journalBeingReviewedInfo[1];
	        }
    	} catch (IOException e) {
			e.printStackTrace();
        } finally { 
            if (brID != null) {
                try {
                    brID.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } 	
    	return journalID;
    }
    
    /**
     * This function reads the journal_being_reviewed.csv and gets the file path of the journal.
     * @return authorsJournal - the file path of the journal.
     */
    private String getAuthorsJournal() {
    	Path path = Paths.get(System.getProperty("user.dir"),"journal_being_reviewed.csv");
        BufferedReader brAuth = null;
    	String authorsJournal = "";
    	
    	try 
    	{
    		brAuth = new BufferedReader(new FileReader(path.toString()));
	        while ((line = brAuth.readLine()) != null) {
	            String[] journalBeingReviewedInfo = line.split(",");
	            authorsJournal = journalBeingReviewedInfo[0];
	        }
    	} catch (IOException e) {
			e.printStackTrace();
        } finally { 
            if (brAuth != null) {
                try {
                	brAuth.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    	return authorsJournal;	
    }
       
    @FXML
    /**
	 * This function initializes the JavaFx controllers on the reviewerUpDown page.
	 */
    private void initialize() {
     
    	decisionChoiceBox.setItems(decisionList);
    	lstFile = new ArrayList<>();
    	lstFile.add("*.pdf");
    	
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
