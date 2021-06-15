package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import java.awt.Desktop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
* Controller class for the editor GUI.
* Allows the editor to:
* see the journals that were submitted by authors.
* see the reviewers that the author chose as the reviewers of their journal.
* select the reviewers that would review each journal.
*/
public class editorForAuthorController implements Initializable
{
	 //This holds the id of the selected journal
	private String currentId; 
	
	//This holds the file path of the selected journal 
	private String currentFilePath = null; 
	
	@FXML
	private ListView<String> listViewID;
	
	@FXML
    private Button logOut;
    
    @FXML
    private Button home;
   
    @FXML
    private Button notifications;

	@FXML
	private Button buttonToSend; 
	
	@FXML
	private Button buttonOpenJournal; 
	
	@FXML
	private TextField textBoxPref1; 
	
	@FXML
	private TextField textBoxPref2;
	
	@FXML
	private TextField textBoxPref3;
	
	@FXML
	private ComboBox<String> dropDown1; 
	
	@FXML
	private ComboBox<String> dropDown2;
	
	@FXML
	private ComboBox<String> dropDown3;
	
	private ArrayList<String> journID = new ArrayList<String>(); 
	
	@FXML
    /**
     * This function switches the editor from the editorForAuthor page to the main Editor page 
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
     * This function switches the editor from the editorForAuthor page to the log in page
     * @param event - the button click, when they log out 
     * @throws Exception
     */
    private void logOut(ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("startPageGUI.fxml"));
    	Main.stage.setTitle("Start Page");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }	   
	
	/**
	 * This function reads the CSV file that contains information about the journal that has been uploaded
	 * Using the information from the CSV file, this function: 
	 * 1. Populates the textArea with the journal submissions that aren't finished being reviewed
	 * 2. Sets the preferred reviewer for the journal that appears in the list (not done being reviewed)
	 * 3. Sets the currentFilePath to be the path to the journal that appears first in the list (the path that matches the preferred reviewers)
	 * 4. Disables the submit to reviewers button and the select preferred reviewers DropDowns if the journal has already been submitted to reviewers
	 */
	private void readJournalCSV()
	{
		Path path = Paths.get(System.getProperty("user.dir"),"journals_submitted_to_editor.csv");
		
		String id; 
		String author;
		String filePath;
		String prefReviewer1; 
		String prefReviewer2; 
		String prefReviewer3;
		Boolean beingReviewed; 
		Boolean doneReview; 
		
		ArrayList<String> journals = new ArrayList<String>(); 
		
		try 
		{
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			
			while(scanner.hasNext())
			{	
				id = scanner.next();
				author = scanner.next(); 
				filePath = scanner.next(); 
				prefReviewer1 = scanner.next(); 
				prefReviewer2 = scanner.next(); 
				prefReviewer3 = scanner.next();
				beingReviewed = Boolean.valueOf(scanner.next());
				doneReview = Boolean.valueOf(scanner.next()); 
				scanner.next();
				scanner.next();
				
				String journalProp = id+". "+author+"   "+filePath;
				
				if(prefReviewer1.equals(" "))
				{
					prefReviewer1 = "Author didn't select Reviewer";
				}
				if(prefReviewer2.equals(" "))
				{
					prefReviewer2 = "Author didn't select Reviewer";
				}
				if(prefReviewer3.equals(" "))
				{
					prefReviewer3 = "Author didn't select Reviewer";
				}
				
				if(currentFilePath == null && doneReview == false)
				{
					currentId = id;
					currentFilePath = filePath; 
					textBoxPref1.setText(prefReviewer1);
					textBoxPref2.setText(prefReviewer2);
					textBoxPref3.setText(prefReviewer3);
					
					if(beingReviewed == true)
					{
						assignedReviewersDropDown();
						dropDown1.setDisable(true);
						dropDown2.setDisable(true);
						dropDown3.setDisable(true);
						buttonToSend.setDisable(true);
					}
					
				}
				journID.add(id);
				if(doneReview == false)
				{
					journals.add(journalProp);
				}

			}
			System.out.println(journID);
			
			scanner.close();
			listViewID.getItems().addAll(journals);
			listViewID.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			listViewID.getSelectionModel().select(0);
			listViewID.setEditable(false);
			textBoxPref1.setEditable(false); 
			textBoxPref2.setEditable(false);
			textBoxPref3.setEditable(false);
		}
		catch(Exception e)
		{
			String warning = "Unable to find journals_submitted_to_editor.csv";
			listViewID.getItems().add(warning);
			e.printStackTrace();
		}
	}
	
	@FXML
    /**
     * This function parses the selected journal to get the journal Id and the author name
     * Then it passes the journal Id and author name as a parameter to readJournalCSV()
     * @param Mouse event - the mouse click (selecting a journal in the listView)
     * @throws Exception
     */
	private void updateDisplays (MouseEvent event) throws Exception
	{
		String selectedJournal = listViewID.getSelectionModel().getSelectedItem();
		int index1 = selectedJournal.indexOf(". ");
		int index2 = selectedJournal.indexOf(".\\");
		String id = selectedJournal.substring(0, index1);
		String authorName = selectedJournal.substring((index1+2),(index2-3));
		readJournalCSV2(id, authorName); 
	}
	
	/**
	 * This function reads from the assigned_journals.csv and populates the dropdowns with the assigned reviewers for 
	 * each journal that has been submitted 
	 * The purpose of this function is so the editor can see which reviewers they have selected for the journals that
	 * have been submitted 
	 */
	private void assignedReviewersDropDown()
	{
		System.out.println("In Assigned Reviewers");
		Path path = Paths.get(System.getProperty("user.dir"),"assigned_journals.csv");	
		try 
		{
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			
			String journalId; String r1; String r2; String r3;
			
			while(scanner.hasNext())
			{	
				journalId = scanner.next();
				scanner.next();
				r1 = scanner.next();
				scanner.next();
				r2 = scanner.next();
				scanner.next();
				r3 = scanner.next();
				scanner.next();
				
				if(journalId.equals(currentId))
				{
					dropDown1.setValue(r1);
					dropDown2.setValue(r2);
					dropDown3.setValue(r3);
				}
			}
			scanner.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This function reads the CSV file that contains information about journal uploads, and searches for the journal parameter 
	 * Using the information from the Journal CSV file, this function: 
	 * 1. Sets the TextFields to contain the selected preferred reviewers of the selected submitted journal
	 * 2. Sets the currentFilePath to be the path to the selected submitted journal
	 * 3. Disables(or enables) the submit to reviewers button and the select preferred reviewers DropDowns if the journal has(hasn't) been submitted to reviewers
	 *@param journalId - the journal Id of the submitted journal whose information is going to be displayed 
	 *@param authorName - the author name of the the submitted journal whose information is going to be displayed 
	 */
	private void readJournalCSV2(String journalId, String authorName)
	{
		Path path = Paths.get(System.getProperty("user.dir"),"journals_submitted_to_editor.csv");
		
		String id; 
		String author;
		String filePath;
		String prefReviewer1; 
		String prefReviewer2; 
		String prefReviewer3;
		Boolean beingReviewed; 
	
		try 
		{
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			
			while(scanner.hasNext())
			{	
				id = scanner.next();
				author = scanner.next(); 
				filePath = scanner.next(); 
				prefReviewer1 = scanner.next(); 
				prefReviewer2 = scanner.next(); 
				prefReviewer3 = scanner.next();
				beingReviewed = Boolean.valueOf(scanner.next());
				scanner.next();
				scanner.next(); 
				scanner.next();
				
				if(prefReviewer1.equals(" "))
				{
					prefReviewer1 = "Author didn't select Reviewer";
				}
				if(prefReviewer2.equals(" "))
				{
					prefReviewer2 = "Author didn't select Reviewer";
				}
				if(prefReviewer3.equals(" "))
				{
					prefReviewer3 = "Author didn't select Reviewer";
				}
				
				if(journalId.equals(id) && author.equals(authorName))
				{
					currentId = id;
					currentFilePath = filePath; 
					textBoxPref1.setText(prefReviewer1);
					textBoxPref2.setText(prefReviewer2);
					textBoxPref3.setText(prefReviewer3);
					
					if(beingReviewed == true)
					{
						assignedReviewersDropDown();
						dropDown1.setDisable(true);
						dropDown2.setDisable(true);
						dropDown3.setDisable(true);
						buttonToSend.setDisable(true);
					}
					else 
					{
						dropDown1.setDisable(false);
						dropDown2.setDisable(false);
						dropDown3.setDisable(false);
						buttonToSend.setDisable(false);
					}
					break;
				}
			}
			scanner.close();
		}
		catch(Exception e)
		{
			String warning = "Unable to find journals_submitted_to_editor.csv";
			listViewID.getItems().add(warning);
			e.printStackTrace();
		}
	}
	
	/**
	* This function reads the CSV file that contains information about the reviewers that are available to review 
	 * the available journals [First Name, Last Name, Email, Affiliation, ID]
	 * Using the CSV file, this function: 
	 * 1. Populates the DropDowns with the FirstName and the LastName of the Reviewers.
	 */
	private void readReviewerCSV()
	{
		Path path = Paths.get(System.getProperty("user.dir"),"reviewer_info.csv");

		System.out.println("Reviewer Info Path:"+path.toString());
			
		String firstname; 
		String lastname; 
		String affiliation;
		String email;
		
		ArrayList<String> reviewers = new ArrayList<String>(); 
		
		try 
		{
			Scanner scanner = new Scanner(new File(path.toString()));
			scanner.useDelimiter("[,\n]");
			
			
			while(scanner.hasNext())
			{
				StringBuilder str = new StringBuilder();
				scanner.next(); //id
				firstname = scanner.next();
				lastname = scanner.next(); 
				email = scanner.next(); //email
				affiliation = scanner.next(); 
				
				str.append(firstname+" "+lastname+" - "+affiliation.replaceAll("\\r|\\n", "")+","+email);
				reviewers.add(str.toString());
			}
			scanner.close();
			
			dropDown1.getItems().addAll(reviewers);
			dropDown2.getItems().addAll(reviewers);
			dropDown3.getItems().addAll(reviewers); 	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
    /**
     * This function opens the Journal that currently is selected 
     * @param event - the button click event  
     * @throws Exception
     */
	private void openJournal (ActionEvent event) throws Exception
    {
		try {
			
			String localPath = currentFilePath.substring(19);
			Path path = Paths.get(System.getProperty("user.dir"),"AuthorSubmission",localPath);
			
			System.out.println(path.toString());
			
			File file = new File(path.toString());
			if(file.exists()){
				if(file.toString( ).endsWith(".pdf")) {
	               
					System.out.println("here");
					System.out.println(file.getAbsolutePath());  
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
     * This function populates the reviewer_notifications.csv 
     * @param event - the button click event  
     */
	private void sendNotification (String recEmail1, String recEmail2, String recEmail3, String notifType, String dateLog, String journalID)
	{
		Path path = Paths.get(System.getProperty("user.dir"),"reviewer_notifications.csv");
		
		try 
		{
			FileWriter filewriter = new FileWriter(path.toString(),true);
			BufferedWriter bw = new BufferedWriter(filewriter); 
			PrintWriter pw = new PrintWriter(bw); 
			pw.println(recEmail1+","+recEmail2+","+recEmail3+","+dateLog+","+notifType+","+journalID);
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
     * This function opens the Journal that currently is selected 
     * @param event - the button click event  
     * @throws Exception
     */
	private void selectReviewers (MouseEvent event) throws Exception
	{
    	DateTimeFormatter dateID = DateTimeFormatter.ofPattern("dd-MM-yyy_HH:mm");  
    	LocalDateTime notiLog = LocalDateTime.now();
    	String notifType = "editorToReviewer";

		if((null == dropDown1.getValue()) || (null == dropDown2.getValue()) || (null == dropDown3.getValue()))
		{
			System.out.println("NULL Dropbox Section");
			return;
		} 
		else 
		{
			if(!duplicatesExist())
			{
				System.out.println("Duplicates DO NOT exist");
				
				String rev1 = dropDown1.getSelectionModel().getSelectedItem();
				String rev2 = dropDown2.getSelectionModel().getSelectedItem();
				String rev3 = dropDown3.getSelectionModel().getSelectedItem();
				reviewerCSVOperations(rev1,rev2,rev3);
				
				sendNotification(rev1,rev2,rev3,notifType,dateID.format(notiLog),journID.get(listViewID.getSelectionModel().getSelectedIndex()));
				
				//edit the CSV file that contains the submitted journals (journals_submitted_to_editor.csv)
				//Change the line with the currentId to end in False,True
				editJournalCSV();
				
				dropDown1.setDisable(true);
				dropDown2.setDisable(true);
				dropDown3.setDisable(true); 
				buttonToSend.setDisable(true);
			}
			else
			{
				System.out.println("Duplicates exist");
			}
		}
	}
	
    /**
     * This function looks at the ATTEMPTED selection by the user in the drowDowns
     * If the User has selected at least 2 of the same Reviewers between drowDown 1,2, or 3 
     * Then they will be prompted to reselect a 
     * @return true or false based on if duplicates exist
     */
	private boolean duplicatesExist()
	{
		Boolean exist = false;
		if((dropDown1.getSelectionModel().getSelectedItem().equals(dropDown2.getSelectionModel().getSelectedItem())) && (dropDown1.getSelectionModel().getSelectedItem().equals(dropDown3.getSelectionModel().getSelectedItem()))) 
		{
			dropDown2.getSelectionModel().clearSelection();
			dropDown3.getSelectionModel().clearSelection();
			exist = true;
			
		} else if(dropDown1.getSelectionModel().getSelectedItem().equals(dropDown2.getSelectionModel().getSelectedItem()))
		{
			dropDown2.getSelectionModel().clearSelection();
			exist = true;
		}
		else if(dropDown1.getSelectionModel().getSelectedItem().equals(dropDown3.getSelectionModel().getSelectedItem())) 
		{
			dropDown3.getSelectionModel().clearSelection();
			exist = true;
		}
		else if(dropDown2.getSelectionModel().getSelectedItem().equals(dropDown3.getSelectionModel().getSelectedItem()))
		{
			dropDown3.getSelectionModel().clearSelection();
			exist = true; 
		}
		return exist; 
	}
		
	/**
	 * This function writes to assigned_journals.csv when the 3 reviewers have been selected for a journal 
	 * @param reviewer1 - the first reviewer that has been assigned to the journal 
	 * @param reviewer2 - the second reviewer that has been assigned to the journal 
	 * @param reviewer3 - the third reviewer that has been assigned to the journal
	 */
	private void reviewerCSVOperations(String reviewer1, String reviewer2, String reviewer3)
	{
		System.out.println("reviewerCSV");
		
		Path path = Paths.get(System.getProperty("user.dir"),"assigned_journals.csv");
		
		try 
		{
			FileWriter filewriter = new FileWriter(path.toString(),true);
			BufferedWriter bw = new BufferedWriter(filewriter); 
			PrintWriter pw = new PrintWriter(bw); 
			pw.println(currentId+","+currentFilePath+","+reviewer1+","+reviewer2+","+reviewer3);
			pw.flush();
			pw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 		
	}
	
	/**
	 * This function is used for editing the journals_submitted_to_editor.csv 
	 * when a journal is submitted to the reviewers 
	 * The main point of this function is to change the "false" to "true" in the being reviewed position, 
	 * so that when the journal is selected, the reviewers that were selected are displayed and the editor 
	 * cannot resubmit the journal to the same or different reviewers again
	 */
	private void editJournalCSV() 
	{
		Path path = Paths.get(System.getProperty("user.dir"),"journals_submitted_to_editor.csv");
	
		File newFile = new File(Paths.get(System.getProperty("user.dir"),"temp.txt").toString());
		File oldFile = new File(path.toString());
		
		String id; String author; String filePath; String r1; String r2; String r3; String beingRev; String doneRev; String dropboxName; String dropboxId; 
		 
		try 
		{
			FileWriter fw = new FileWriter(Paths.get(System.getProperty("user.dir"),"temp.txt").toString(),true);
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
					pw.println(id+","+author+","+filePath+","+r1+","+r2+","+r3+",true,false,"+dropboxName.replaceAll("\\r|\\n", "")+","+dropboxId.replaceAll("\\r|\\n", ""));
				}
				else 
				{
					pw.println(id+","+author+","+filePath+","+r1+","+r2+","+r3+","+beingRev.replaceAll("\\r|\\n", "")+","+doneRev.replaceAll("\\r|\\n", "")+","+dropboxName.replaceAll("\\r|\\n", "")+","+dropboxId.replaceAll("\\r|\\n", ""));	
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
	 * This function initializes the JavaFx controllers on the editorForAuthor page and their event handlers
	 */
	public void initialize(URL url, ResourceBundle rb)
    {
    	readReviewerCSV(); 
		readJournalCSV(); 
    	
    	buttonOpenJournal.setOnAction(arg0 ->
        {
     	   	try
     	   	{
     	   		openJournal(arg0);
     	   	}
     	   	catch
     	   	(Exception e)
     	   	{
 			e.printStackTrace();
 			}
        });
    	
    	listViewID.setOnMouseClicked(arg0 ->
        {
     	   	try
     	   	{
     	   		updateDisplays(arg0);
     	   	}
     	   	catch
     	   	(Exception e)
     	   	{
 			e.printStackTrace();
 			}
        });
    	buttonToSend.setOnMouseClicked(arg0 ->
        {
     	   	try
     	   	{
     	   		selectReviewers(arg0);
     	   	}
     	   	catch
     	   	(Exception e)
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
