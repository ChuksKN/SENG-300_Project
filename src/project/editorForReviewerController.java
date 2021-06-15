package project;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
* Controller class for the editor GUI.
* Allows the editor to:
* see who the reviewers of each journal are.
* see the reviewers' feedback of the journal(s) that was assigned to them. 
* send a PDf file containing the feedback of the 3 reviewers of the journal to the author who submitted the journal.
* decide what the final evaluation result for the selected journal would be. 
*/
public class editorForReviewerController implements Initializable
{
	private String currentId = ""; 
	private String dropboxName = "";
	private String dropboxID = "";
	private String currentFilePath1 = null; 
	private String currentFilePath2 = null;
	private String currentFilePath3 = null;
	private String authorsEmail;
	private int button1Clicked = 0;
	private int button2Clicked = 0;
	private int button3Clicked = 0;
	private List<String> lstFile;
	private ObservableList<String> decisionList = FXCollections.observableArrayList(null, "Accepted", "Rejected", "Major Revision", "Minor Revision");
	
	@FXML
	private Button logOut;

	@FXML
	private Button notifications;

	@FXML
	private Button home;	
	
    @FXML
    private ListView<String> listViewID;

    @FXML
    private ComboBox<String> decisionByEditor;

    @FXML
    private TextField rev1Decision;

    @FXML
    private TextField rev2Decision;

    @FXML
    private TextField rev3Decision;
    
    @FXML
    private Button viewReviewer1sFeedback;

    @FXML
    private Button viewReviewer2sFeedback;

    @FXML
    private Button viewReviewer3sFeedback;
    
    @FXML
    private Button uploadButton;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private TextField rev1Info;

    @FXML
    private TextField rev2Info;

    @FXML
    private TextField rev3Info;
    
    @FXML
    private Label uploadCombinedFeedback;

    @FXML
    private Label chooseFinalEvalResultError;

    @FXML
    private Label uploadCombinedFeedbackError;
    
    @FXML
    /**
     * This function switches from the editorForReviewer page to the main Editor page 
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
     * This function switches from the editorForReviewer page to the start page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void logOut (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("startPageGUI.fxml"));
    	Main.stage.setTitle("Start Page");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
    
    @FXML
    /**
     * This function opens the system explorer where the editor can select the PDF file containing 
     * the combined feedback of the 3 reviewers assigned to the journal selected.
     * @param event - the button click event 
     */
    private void onUploadClick(ActionEvent event) {
    	FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", lstFile));
		File f = fc.showOpenDialog(null);
		if(f != null) {
			 uploadCombinedFeedback.setText(f.getAbsolutePath());
			 uploadCombinedFeedbackError.setText("");
		}
    }
    
    /**
     * This function populates the notifications.csv 
     * @param event - the button click event  
     */
	private void sendNotification (String authorEm, String notifType, String dateLog, String journalID)
	{
		
		Path path = Paths.get(System.getProperty("user.dir"),"author_notifications.csv");
		
		try 
		{
			FileWriter filewriter = new FileWriter(path.toString(),true);
			BufferedWriter bw = new BufferedWriter(filewriter); 
			PrintWriter pw = new PrintWriter(bw); 
			
			//The next line in the file we are "printing" 
			pw.println(authorEm+","+dateLog+","+notifType+","+journalID);
			pw.flush();
			pw.close();
		
		}
		catch(Exception e)
		{
		
			e.printStackTrace();
			
		} 
	}
	
    @FXML
    /**
     * This function takes the journal ID of the journal selected, the email of the author of the journal,
     * the file path of the combined feedback that the editor uploaded, the evaluation result decided by the editor,
     * the drop box that the journal was submitted to, and the drop box's ID and writes it all to final_feedback.csv.  
     * @param event - the button click event 
     * @throws Exception
     */
    private void onSubmitClick(ActionEvent event) throws IOException {
    	String combinedReviewFilePath = uploadCombinedFeedback.getText();
    	String selectedFinalDecision = decisionByEditor.getSelectionModel().getSelectedItem();
    	chooseFinalEvalResultError.setText("");
    	
       	DateTimeFormatter dateID = DateTimeFormatter.ofPattern("dd-MM-yyy_HH:mm");  
    	LocalDateTime notiLog = LocalDateTime.now();
    	String notifType = "editorToAuthor";
    	
    	if(combinedReviewFilePath != "" && selectedFinalDecision != null) {
    		File source = new File(combinedReviewFilePath);
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("(yyyy-MM-dd_HH-mm-ss)");  
        	LocalDateTime now = LocalDateTime.now();  
        	
        	File dest = new File(".\\CombinedReviewerFeedback\\CombinedReviewerFeedback_"+dtf.format(now)+".pdf");
        	Files.copy(source.toPath(), dest.toPath());
        	String destFeedbackPath = dest.toString();
        	
        	String filepath = "final_feedback.csv";
        	
        	try {
        		
        		sendNotification(authorsEmail,notifType,dateID.format(notiLog),currentId);
        		
    			FileWriter fwRev = new FileWriter(filepath, true);
    			BufferedWriter bwRev = new BufferedWriter(fwRev);
    			PrintWriter pwRev = new PrintWriter(bwRev);
    				
    			pwRev.println(currentId+","+authorsEmail+","+destFeedbackPath+","+selectedFinalDecision+","+dropboxName.replaceAll("\\r|\\n", "")+","+dropboxID.replaceAll("\\r|\\n", "")); //!
    			pwRev.flush();
    			pwRev.close();
    			
    			JOptionPane.showMessageDialog(null, "File Sent:\n"+destFeedbackPath);
    			editJournalCSV();
    			decisionByEditor.setDisable(true);
    			uploadButton.setDisable(true);
    			submitButton.setDisable(true);
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(null, "File Not Sent");
    			
    		}
			
    	}else {
    		if(combinedReviewFilePath == "" && selectedFinalDecision == null) {
    			chooseFinalEvalResultError.setText("You Must Choose an Evaluation Result!");
        		uploadCombinedFeedbackError.setText("You Must Upload the Combined Feedback!");
			}else if(combinedReviewFilePath != "" && selectedFinalDecision == null) {
				chooseFinalEvalResultError.setText("You Must Choose an Evaluation Result!");
			}else if(combinedReviewFilePath == "" && selectedFinalDecision != null){
				uploadCombinedFeedbackError.setText("You Must Upload the Combined Feedback!");
			}
    	}
    }
    
    /**
     * This function reads the journal IDs and the 3 reviewers assigned to those journals in assigned_journals.csv 
     * and writes them to the text area seen on the page.
     */
    private void readAssignedJournalsCSV() {
    	Path path = Paths.get(System.getProperty("user.dir"),"assigned_Journals.csv");

		String id; 
		String prefRev1;
		String prefRev1Email;
		String prefRev2;
		String prefRev2Email;
		String prefRev3;
		String prefRev3Email;
		
		ArrayList<String> reviewedJournals = new ArrayList<String>(); 
		
		try 
		{
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			
			while(scanner.hasNext())
			{	
				id = scanner.next();
				scanner.next(); 
				prefRev1 = scanner.next();
				prefRev1Email =scanner.next();
				prefRev2 = scanner.next(); 
				prefRev2Email =scanner.next();
				prefRev3 = scanner.next(); 
				prefRev3Email =scanner.next();
				String reviewInfo = id+": "+prefRev1+", "+prefRev1Email+"; "+prefRev2+", "+prefRev2Email+";  "+prefRev3+", "+prefRev3Email.replaceAll("\\r|\\n", "")+";   ";
				reviewedJournals.add(reviewInfo);
			}
			scanner.close();
		
			listViewID.getItems().addAll(reviewedJournals);
			listViewID.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			listViewID.getSelectionModel().select(0);
			listViewID.setEditable(false);
			viewReviewer1sFeedback.setDisable(true);
			viewReviewer2sFeedback.setDisable(true);
			viewReviewer3sFeedback.setDisable(true);
			decisionByEditor.setDisable(true);
			uploadButton.setDisable(true);
			submitButton.setDisable(true);
			rev1Info.setEditable(false);
			rev2Info.setEditable(false);
			rev3Info.setEditable(false);
			rev1Decision.setEditable(false);
			rev2Decision.setEditable(false);
			rev3Decision.setEditable(false);		
		}
		catch(Exception e)
		{
			String warning = "Unable to find reviewer_decision.csv";
			listViewID.getItems().add(warning);
			e.printStackTrace();
		}
    }
    
    @FXML
    /**
     * This function reads the journal that was clicked on the text box to get the journal Id and 
     * the 3 reviewers assigned to that journal which are used when the readReviewerDecision() 
     * and readJournalSubmitted() methods are called. 
     * @param Mouse event - the mouse click (selecting a journal in the text box)
     * @throws Exception
     */
	private void onListViewMouseClick (MouseEvent event) throws Exception
	{
    	String reviewer1;
    	String reviewer2;
    	String reviewer3;
    	String selectedReview = listViewID.getSelectionModel().getSelectedItem();
		int index1 = selectedReview.indexOf(":");
		int index2 = selectedReview.indexOf("; ");
		int index3 = selectedReview.indexOf(";  ");
		int index4 = selectedReview.indexOf(";   ");
		currentId = selectedReview.substring(0,index1);
		reviewer1 = selectedReview.substring(index1+2, index2);
		reviewer2 = selectedReview.substring(index2+2, index3);
		reviewer3 = selectedReview.substring(index3+3, index4);
		
		System.out.println(selectedReview);
		System.out.println("ID: "+currentId);
		System.out.println("email 1: "+reviewer1);
		System.out.println("email 2: "+reviewer2);
		System.out.println("email 3: "+reviewer3);
		readReviewerDecisionCSV(currentId, reviewer1, reviewer2, reviewer3);
		readJournalsSubmittedCSV(currentId);
		uploadCombinedFeedback.setText("");
		uploadCombinedFeedbackError.setText("");
		chooseFinalEvalResultError.setText("");
	}
    
    /**
     * This function reads the reviewer_decision.csv to get the information of the assigned reviewers 
     * @param journalId the journal ID of the selected journal
     * @param aReviewer1 the reviewer that's been assigned to the selected journal
     * @param aReviewer2 the reviewer that's been assigned to the selected journal
     * @param aReviewer3 the reviewer that's been assigned to the selected journal
     */
    private void readReviewerDecisionCSV(String journalId, String aReviewer1, String aReviewer2, String aReviewer3) {
    	Path path = Paths.get(System.getProperty("user.dir"),"reviewer_decision.csv");

		String id;
		String email;
		String filePath;
		String decision;
		
		try 
		{
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			rev1Info.setText("Reviewer Info Unavailable");
			rev1Decision.setText("Reviewer Info Unavailable");
			rev2Info.setText("Reviewer Info Unavailable");
			rev2Decision.setText("Reviewer Info Unavailable");
			rev3Info.setText("Reviewer Info Unavailable");
			rev3Decision.setText("Reviewer Info Unavailable");
			viewReviewer1sFeedback.setDisable(true);
			viewReviewer2sFeedback.setDisable(true);
			viewReviewer3sFeedback.setDisable(true);
			
			while(scanner.hasNext())
			{	
				id = scanner.next();
				email = scanner.next();
				filePath = scanner.next(); 
				decision = scanner.next();
				//System.out.println(journalId + " Vs. "+ id);
				
				if(journalId.equals(id)) {
					String reviewer1Email;
					String reviewer2Email;
					String reviewer3Email;
					int index1 = aReviewer1.indexOf(", ");
					reviewer1Email = aReviewer1.substring(index1+2);
					int index2 = aReviewer2.indexOf(", ");
					reviewer2Email = aReviewer2.substring(index2+2);
					int index3 = aReviewer3.indexOf(", ");
					reviewer3Email = aReviewer3.substring(index3+2);
			
					if(reviewer1Email.equals(email)) {
						rev1Info.setText(aReviewer1);
						rev1Decision.setText(decision);
						currentFilePath1 = filePath;
						viewReviewer1sFeedback.setDisable(false);
						System.out.println("rev1info: "+rev1Info.getText());
						System.out.println("rev1Decision: "+rev1Decision.getText());
					}else if(reviewer2Email.equals(email)) {
						rev2Info.setText(aReviewer2);
						rev2Decision.setText(decision);
						currentFilePath2 = filePath;
						viewReviewer2sFeedback.setDisable(false);
						System.out.println("rev2info: "+rev2Info.getText());
						System.out.println("rev2Decision: "+rev2Decision.getText());
					}else if(reviewer3Email.equals(email)) {
						rev3Info.setText(aReviewer3);
						rev3Decision.setText(decision);
						currentFilePath3 = filePath;
						viewReviewer3sFeedback.setDisable(false);
						System.out.println("rev3info: "+rev3Info.getText());
						System.out.println("rev3Decision: "+rev3Decision.getText());
					}
					if(rev1Info.getText().equals(aReviewer1) && rev2Info.getText().equals(aReviewer2) && rev3Info.getText().equals(aReviewer3)) {
						decisionByEditor.setDisable(false);
						uploadButton.setDisable(false);
						submitButton.setDisable(false); 
					}else {
						if(!rev1Info.getText().equals(aReviewer1)) {
							rev1Info.setText("Reviewer Info Unavailable");
							rev1Decision.setText("Reviewer Info Unavailable");
							viewReviewer1sFeedback.setDisable(true);
							decisionByEditor.setDisable(true);
							uploadButton.setDisable(true);
							submitButton.setDisable(true); 
						}
						if(!rev2Info.getText().equals(aReviewer2)) {
							rev2Info.setText("Reviewer Info Unavailable");
							rev2Decision.setText("Reviewer Info Unavailable");
							viewReviewer2sFeedback.setDisable(true);
							decisionByEditor.setDisable(true);
							uploadButton.setDisable(true);
							submitButton.setDisable(true); 
						}
						if(!rev3Info.getText().equals(aReviewer3)) {
							rev3Info.setText("Reviewer Info Unavailable");
							rev3Decision.setText("Reviewer Info Unavailable");
							viewReviewer3sFeedback.setDisable(true);
							decisionByEditor.setDisable(true);
							uploadButton.setDisable(true);
							submitButton.setDisable(true); 
						}
					}
				}
			}
			scanner.close();
		
		}
		catch(Exception e)
		{
			String warning = "Unable to find reviewer_decision.csv";
			listViewID.getItems().add(warning);
			e.printStackTrace();
		}
    }
    
    /**
     * This function reads the journals_submitted_to_editor.csv to get the drop box name and the drop box ID 
     * that the selected journal was submitted to.
     * @param aID the journal ID of the selected journal
     */
    private void readJournalsSubmittedCSV(String aID) {
    	Path path = Paths.get(System.getProperty("user.dir"),"journals_submitted_to_editor.csv");
    	String id;
    	String email;
    	String dropboxname;
    	String dropboxid;
    	Boolean finishedReviewed;
    	try 
		{
			Scanner s = new Scanner(new File(path.toString()));
			s.useDelimiter("[,\n]");
			
			while(s.hasNext())
			{	
				id = s.next();
				email = s.next();
				s.next();
				s.next();
				s.next();
				s.next();
				s.next();
				finishedReviewed = Boolean.valueOf(s.next());
				dropboxname = s.next();
				dropboxid = s.next();
				
				if(aID.equals(id)) {
					authorsEmail = email;
					dropboxName = dropboxname;
					dropboxID = dropboxid;
					System.out.println("If: "+currentId);
					System.out.println("Author's Email: "+authorsEmail);
					
					if(finishedReviewed == true) {
						decisionByEditor.setDisable(true);
						uploadButton.setDisable(true);
						submitButton.setDisable(true);
					}
				}
			}
			s.close();
			
		}catch(Exception e)
    	{
			e.printStackTrace();
		}
    }
    
    @FXML
    /**
     * This function opens the selected reviewer's feedback
     * @param event button click event.
     * @throws Exception
     */
    private void openReviewerFeedback (ActionEvent event) throws Exception
    {
		try {
			String localPath = "";
			
			if(button1Clicked == 1 && button2Clicked == 0 && button3Clicked == 0) {
				localPath = currentFilePath1.substring(19);
			}else if(button1Clicked == 0 && button2Clicked == 1 && button3Clicked == 0) {
				localPath = currentFilePath2.substring(19);
			}else if(button1Clicked == 0 && button2Clicked == 0 && button3Clicked == 1) {
				localPath = currentFilePath3.substring(19);
			}
			Path path = Paths.get(System.getProperty("user.dir"),"ReviewerFeedback",localPath);
			
			System.out.println(path.toString());
			
			File file = new File(path.toString());
			if(file.exists()){
				if(file.toString( ).endsWith(".pdf")) {
	               
					System.out.println("The pdf file is Open");  
					Process Pro = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+file.getPath());
					Pro.waitFor();
				}
	            else{
	                Desktop dt = Desktop.getDesktop();
	                dt.open(file);
	            }
			}
	    	else
	    	{
	    		//Something pops up that says couldn't be found 
	    		//Could do this as a window or a 
	    		System.out.println("Error Opening File/File Doesn't exits");
	    		System.out.println("currentFilePath: "+localPath);
	    	}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
    
    /**
	 * This function is used for editing the journals_submitted_to_editor.csv when a feedback is submitted by the reviewers 
	 * The main point of this function is to change the "false" to "true" in the finished reviewed position, 
	 * so that once the editor has submitted the feedback he/she cannot submit another feedback about the same journal.
	 */
    private void editJournalCSV() 
	{
		Path path = Paths.get(System.getProperty("user.dir"),"journals_submitted_to_editor.csv");
		File newFile = new File(Paths.get(System.getProperty("user.dir"),"temp2.txt").toString());
		File oldFile = new File(path.toString());
		
		String id; String author; String filePath; String r1; String r2; String r3; String beingRev; String doneRev; String dropboxName; String dropboxId; 
		 
		try 
		{
			FileWriter fw = new FileWriter(Paths.get(System.getProperty("user.dir"),"temp2.txt").toString(),true);
			BufferedWriter bw = new BufferedWriter(fw); 
			PrintWriter pw = new PrintWriter(bw);
			
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			
			while(scanner.hasNext())
			{
				id = scanner.next();
				author = scanner.next();
				filePath = scanner.next();
				r1 = scanner.next();
				r2 = scanner.next();
				r3 = scanner.next();
				beingRev = scanner.next();
				doneRev = scanner.next();
				dropboxName = scanner.next();
				dropboxId = scanner.next();
				
				if(id.equals(currentId))
				{
					pw.println(id+","+author+","+filePath+","+r1+","+r2+","+r3+",true,true,"+dropboxName+","+dropboxId.replaceAll("\\r|\\n", ""));
				}
				else 
				{
					pw.println(id+","+author+","+filePath+","+r1+","+r2+","+r3+","+beingRev+","+doneRev+","+dropboxName+","+dropboxId.replaceAll("\\r|\\n", ""));	
				}
			}
			scanner.close();
			pw.flush();
			pw.close();
			oldFile.delete();
			File dump = new File(path.toString());
			newFile.renameTo(dump);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}  
    
    /**
	 * This function initializes the JavaFx controllers on the editorForReviewer page
	 */
    public void initialize(URL url, ResourceBundle rb)
    {
    	readAssignedJournalsCSV();
    	lstFile = new ArrayList<>();
		lstFile.add("*.pdf");
		decisionByEditor.setItems(decisionList);
    	
    	viewReviewer1sFeedback.setOnAction(arg0 ->
        {
     	   	try
     	   	{	
     	   		button1Clicked = 1;
     	   		button2Clicked = 0;
     	   		button3Clicked = 0;
     	   		openReviewerFeedback(arg0);
     	   	}
     	   	catch (Exception e)
     	   	{
     	   		e.printStackTrace();
 			}
        });
    	
    	viewReviewer2sFeedback.setOnAction(arg0 ->
        {
     	   	try
     	   	{
	     	   	button1Clicked = 0;
	 	   		button2Clicked = 1;
	 	   		button3Clicked = 0;
     	   		openReviewerFeedback(arg0);
     	   	}
     	   	catch (Exception e)
     	   	{
     	   		e.printStackTrace();
 			}
        });
    	
    	viewReviewer3sFeedback.setOnAction(arg0 ->
        {
     	   	try
     	   	{
	     	   	button1Clicked = 0;
	 	   		button2Clicked = 0;
	 	   		button3Clicked = 1;
     	   		openReviewerFeedback(arg0);
     	   	}
     	   	catch (Exception e)
     	   	{
     	   		e.printStackTrace();
 			}
        });
    	
    	listViewID.setOnMouseClicked(arg0 ->
        {
     	   	try
     	   	{
     	   		onListViewMouseClick(arg0);
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
    	   	catch
    	   	(Exception e)
    	   	{
			e.printStackTrace();
			}
    	});
    }
}
