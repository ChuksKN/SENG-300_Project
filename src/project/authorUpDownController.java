package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
* Controller class for the author GUI.
* Allows the author to:
* upload a PDF file containing his/her journal which will be sent to the reviewer that's assigned to that journal
* select up to 3 reviewers that the author would prefer review their journal.
* download the feedback of the journal and see if their journal will be published or not.
*/
public class authorUpDownController implements Initializable
{
	private List<String> lstFile;
	private static Scanner scanner;
	private String finalDecision;

	@FXML
	private Button uploadJournal;

	@FXML
	private Button submitJournal;

	@FXML 
	private Button downloadFeedback;

	@FXML
	private Label evaluationResult;

	@FXML
	private Label decisionMade;

	@FXML
	private Button logOut;

	@FXML
	private Button notifications;

	@FXML
	private Button home;	    

	@FXML
	private ChoiceBox<String> listOfReviewers1;

	@FXML
	private ChoiceBox<String> listOfReviewers2;

	@FXML
	private ChoiceBox<String> listOfReviewers3;

	@FXML
	private Label uploadError;

	@FXML
	private Label getJournal;


	@FXML
	/**
     * This function switches from the authorUpDown page to the main Author page 
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
     * This function switches from the authorUpDown page to the start page 
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
     * This function opens the system explorer where the author can select the PDF file containing his/her journal
     * @param event - the button click event 
     */
	private void onUploadClick(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", lstFile));
		File f = fc.showOpenDialog(null);
		if(f != null) {
			getJournal.setText(f.getAbsolutePath());
			getJournal.setAlignment(Pos.CENTER);
			getJournal.setStyle("-fx-background-color: #14a75c");
			uploadError.setText("");
			uploadError.setAlignment(Pos.CENTER);
    		uploadError.setStyle("-fx-background-color: #3d4956");
		}
	}	

	@FXML
	/**
     * This function takes the journal ID of the journal uploaded, the email of the author, the file path of the journal 
     * that the author uploaded, the 3 preferred reviewers of the author, A boolean for if it's being reviewed or not,
     * another boolean for if it's done being reviewed or not, the drop box that the journal was submitted to, 
     * and the drop box's ID and writes it all to journals_submitted_to_editor.csv.  
     * @param event - the button click event 
     * @throws IOException
     */
	void onSubmitClick(ActionEvent event) throws IOException {

		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		String filepathCurUsers = "current_user.csv";
		String authorsEmail = "";
		try {

			br = new BufferedReader(new FileReader(filepathCurUsers));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] currentUserInfo = line.split(csvSplitBy);
				authorsEmail = currentUserInfo[0];
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//Journal ID,Author First,Author Last,File Path,Preferred Reviewer 1,Preferred Reviewer 2,Preferred Reviewer 3, Being Reviewed (Boolean), Finished Reviewed (Boolean)
		DateTimeFormatter dateID = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");  
		LocalDateTime now = LocalDateTime.now();
		String journalID = "J"+dateID.format(now);
		String journalFilePath = getJournal.getText();
		String selectedPrefReviewer1 = listOfReviewers1.getSelectionModel().getSelectedItem();
		String selectedPrefReviewer2 = listOfReviewers2.getSelectionModel().getSelectedItem();
		String selectedPrefReviewer3 = listOfReviewers3.getSelectionModel().getSelectedItem();
		Boolean beingReviewed = false;
		Boolean finishedReviewed = false;
		String dropboxInfo = getDropBoxInfo();


		if(journalFilePath != "") {
			File source = new File(journalFilePath);
			File dest = new File(".\\AuthorSubmission\\Author's Journal_"+dateID.format(now)+".pdf");
			Files.copy(source.toPath(), dest.toPath());
			String destJournalPath = dest.toString();
			String filepath = "journals_submitted_to_editor.csv";

			try {
				FileWriter fwRev = new FileWriter(filepath, true);
				BufferedWriter bwRev = new BufferedWriter(fwRev);
				PrintWriter pwRev = new PrintWriter(bwRev);
				if(selectedPrefReviewer1 == null) {
					selectedPrefReviewer1 = "No Preference";
				}
				if(selectedPrefReviewer2 == null) {
					selectedPrefReviewer2 = "No Preference";
				}
				if(selectedPrefReviewer3 == null) {
					selectedPrefReviewer3 = "No Preference";
				}
				pwRev.println(journalID+","+authorsEmail+","+destJournalPath+","+selectedPrefReviewer1.replaceAll("\\r|\\n", "")+","+selectedPrefReviewer2.replaceAll("\\r|\\n", "")+","+selectedPrefReviewer3.replaceAll("\\r|\\n", "")+","+beingReviewed.toString().replaceAll("\n", "")+","+finishedReviewed.toString().replaceAll("\n", "")+","+dropboxInfo.replaceAll("\n", "")); //!
				pwRev.flush();
				pwRev.close();

				JOptionPane.showMessageDialog(null, "File Sent to:\n "+destJournalPath);

			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "File Not Sent");
			}
		}else {
			uploadError.setText("You Must Upload Your Journal!");
			uploadError.setAlignment(Pos.CENTER);
			uploadError.setStyle("-fx-background-color: #ff652f");
			
		}
	}

	@FXML
	/**
	 * This function opens the combined feedback of the 3 reviewers of the journal.
	 * @param event: button click event.
	 */
	private void onDownloadClick(ActionEvent event) {

		try
		{
			String path = getFinalFeedBack(); // Test Path
			decisionMade.setText(finalDecision);

			File file = new File(path);

			if(file.exists())
			{
				Process pro = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
				pro.waitFor(); 

			}
			else
			{
				//Something pops up that says couldn’t be found
				System.out.println("No file available");
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
    
	/**
     * This function gets the drop box name and the drop box ID of the selected drop box in the authorMain page.
     * @return info the drop box name and drop box ID of the selected drop box.
     */
    private String getDropBoxInfo() {
		Path path = Paths.get(System.getProperty("user.dir"),"selected_dropbox_for_upload_journal.csv");
		BufferedReader br = null;
    	String info = "";
    	String line = "";
  
    	try 
    	{
    		br = new BufferedReader(new FileReader(path.toString()));
	        while ((line = br.readLine()) != null) {
	        	String[] dropboxInfo = line.split(",");
	        	String j = dropboxInfo[0];
	        	String k = dropboxInfo[1];
	        	info = j+","+k;
	        }
    	} catch (IOException e) {
			e.printStackTrace();
        } finally { 
            if (br != null) {
                try {
                	br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return info;
	}
	
    /**
     * This function determines which button(s) will be disabled, depending on where the author chooses to go from the main page.
     */
	public void whichDropBox() {
		authorMainController am = new authorMainController();
		int k = am.getChecker();
		if(k == 0) {
			downloadFeedback.setDisable(true);
			System.out.println("if: "+k);
			
		}else if(k ==1) {
			uploadJournal.setDisable(true);
			listOfReviewers1.setDisable(true);
			listOfReviewers2.setDisable(true);
			listOfReviewers3.setDisable(true);
			submitJournal.setDisable(true);
			System.out.println("E: "+k);
		
		}
	}
	
	/**
	 * This function reads the CSV file that contains information about the reviewers that are available to review the journals.
	 * Using the CSV file, this function populates the DropDowns with the FirstName, the LastName, and affiliation of the Reviewers. 
	 */
	private void readReviewerCSV()
	{
		Path path = Paths.get(System.getProperty("user.dir"),"reviewer_info.csv");
		
		String firstname; 
		String lastname; 
		String affiliation;
		ArrayList<String> reviewers = new ArrayList<String>();
		
		try 
		{
			scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			while(scanner.hasNext())
			{
				StringBuilder str = new StringBuilder();
				scanner.next(); //reviewerID
				firstname = scanner.next();
				lastname = scanner.next(); 
				scanner.next(); //email
				affiliation = scanner.next(); 
				str.append(firstname+" "+lastname+" - "+affiliation.replaceAll("\\r|\\n", ""));
				reviewers.add(str.toString());
			}
			listOfReviewers1.getItems().add(0, null);
			listOfReviewers2.getItems().add(0, null);
			listOfReviewers3.getItems().add(0, null);
			listOfReviewers1.getItems().addAll(reviewers);
			listOfReviewers2.getItems().addAll(reviewers);
			listOfReviewers3.getItems().addAll(reviewers); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	/**
     * This function gets the file path of the reviewers' combined feedback and the final evaluation result 
     * that was decided by the editor. 
     * @return combinedFeedbackPath: the file path of the reviewers' combined feedback. 
     */
	private String getFinalFeedBack() {
		Path path = Paths.get(System.getProperty("user.dir"),"selected_dropbox_for_download_feedback.csv");
		
		BufferedReader br = null;
    	String finalEvaluationResult = "";
    	String line = "";
  
    	try 
    	{
    		br = new BufferedReader(new FileReader(path.toString()));
	        while ((line = br.readLine()) != null) {
	        	String[] feedbackInfo = line.split(",");
	        	finalEvaluationResult = feedbackInfo[1];
	            finalDecision = feedbackInfo[0];
	        }
    	} catch (IOException e) {
			e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                	br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return finalEvaluationResult;
	}

	/**
	 * This function initializes the JavaFx controllers on the authorUpDown page.
	 */
	public void initialize(URL url, ResourceBundle rb) 
	{
		whichDropBox();
		readReviewerCSV(); 
		lstFile = new ArrayList<>();
		lstFile.add("*.pdf");

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