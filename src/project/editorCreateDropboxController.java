package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

/**
* Controller class for the editor GUI.
* Allows the editor to:
* create a drop box where journals can be submitted. 
* create a drop box with or without a deadline
* close a drop box. 
*/
public class editorCreateDropboxController implements Initializable
{
	
		@FXML
		private Button closeDropbox;
	
		@FXML
	    private Button home;
	    
	    @FXML
	    private Button logOut;
	   
	    @FXML
	    private Button notifications;
	    
	    @FXML
	    private Button submit;
	   
	    @FXML
	    private DatePicker deadlinePicker;
	    
	    @FXML
	    private TextField dropboxName;
	    
	    @FXML
	    private LocalDate deadline;
	    
	    @FXML
	    private ListView<String> dropboxList;
	    
	    @FXML
	    private String name;
	    
	    @FXML
	    /**
	     * This function loads the FXML file for the editorMainGUI, returning the user to the home screen
	     * @param event - The button press triggered from when a user wants to return to the home screen 
	     * @throws Exception
	     */
		private void editorMainGUI(ActionEvent event) throws Exception
	    {		   
	    	Parent root = FXMLLoader.load(getClass().getResource("editorMainGUI.fxml"));
	    	Main.stage.setTitle("Homepage - Editor");
	    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
	    }
	    
	    @FXML
	    /**
	     * This function loads the FXML file for the startPageGUI, returning the user to main sign in page
	     * @param event - The button press triggered from when a user wants to log out 
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
	     * This function "creates a dropBox" using the values that are in the textField and the datePicker
	     * @param event - the button press from when a user wants to create a dropBox 
	     * @throws Exception
	     */
	    private void createDropbox(ActionEvent event) throws Exception
	    {
	    	DateTimeFormatter dtfyyyy = DateTimeFormatter.ofPattern("yyyy");
	    	DateTimeFormatter dtfMM = DateTimeFormatter.ofPattern("MM");
	    	DateTimeFormatter dtfdd = DateTimeFormatter.ofPattern("dd");
			LocalDateTime currentDate = LocalDateTime.now();
			
	    	//check if there are null values 
	    	if(!dropboxName.getText().isBlank())
	    	{
		    	//check if the dropbox name currently exists 
		    	if(nameExists(dropboxName.getText()))
		    	{
		    		JOptionPane.showMessageDialog(null, "That name already exists - choose new name");
		    		//create a prompt that informs user that they should 
		    	}
		    	else
		    	{	
		    		try
		    		{
		    			int currentyyyy = Integer.parseInt(dtfyyyy.format(currentDate));
		    			int pickedyyyy = Integer.parseInt(deadlinePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy")));
		    			int currentMM = Integer.parseInt(dtfMM.format(currentDate));
		    			int pickedMM = Integer.parseInt(deadlinePicker.getValue().format(DateTimeFormatter.ofPattern("MM")));
		    			int currentdd = Integer.parseInt(dtfdd.format(currentDate));
		    			int pickeddd = Integer.parseInt(deadlinePicker.getValue().format(DateTimeFormatter.ofPattern("dd")));

		    			if(pickedyyyy >= currentyyyy)
		    			{
	    					if (pickedyyyy > currentyyyy)
	    					{
			    				writeToCSV(dropboxName.getText(), deadlinePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	    					}		    				
		    				else if(pickedyyyy == currentyyyy)
		    				{
		    					if(pickedMM > currentMM)
		    					{
	    							writeToCSV(dropboxName.getText(), deadlinePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    					}
	    						else if(pickedMM == currentMM && pickeddd > currentdd)
		    					{
		    						writeToCSV(dropboxName.getText(), deadlinePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    					}
		    					else
		    					{
				    				JOptionPane.showMessageDialog(null, "You have chosen a date that is in the past or the current date. Please pick a date in the future!");
		    					}
		    				}
	    					else
	    					{
	    						JOptionPane.showMessageDialog(null, "You have chosen a date that is in the past or the current date. Please pick a date in the future!");
	    					}		    			}
		    			else
		    			{
		    				JOptionPane.showMessageDialog(null, "You have chosen a date that is in the past or the current date. Please pick a date in the future!");
		    			}

		    		}
		    		catch(NullPointerException e)
		    		{
		    			System.out.println("THERE IS NO DEADLINE");
		    			writeToCSV(dropboxName.getText(),"9999-99-99");
		    			//e.printStackTrace();
		    		}
		    	}
	    	}
	    }	
	   
	    /**
	     * This function takes the name and the deadline of the dropBox to be created and 
	     * writes the information to the dropboxes.csv on the next empty line in the format ID,name,true,date
	     * @param name - The name of the dropBox to be created 
	     * @param date - The deadline of the dropBox to be created 
	     */
	    private void writeToCSV(String name, String date)
	    {
	    	
	    	System.out.println("write NEWLINE to dropboxes.csv");
			
			Path path = Paths.get(System.getProperty("user.dir"),"dropboxes.csv");
			
			try 
			{
				FileWriter filewriter = new FileWriter(path.toString(),true);
				BufferedWriter bw = new BufferedWriter(filewriter); 
				PrintWriter pw = new PrintWriter(bw); 
				
				DateTimeFormatter dateID = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				LocalDateTime now = LocalDateTime.now();
				String id = "D"+ dateID.format(now);
				String toprint = id+","+name+",true,"+date;
				
				//The next line in the file we are "printing" 
				pw.println(toprint.replaceAll("\\r|\\n", ""));
				pw.flush();
				pw.close();
			
			}
			catch(Exception e)
			{
				e.printStackTrace();	
			} 
	    	

	    }
	    
	    /**
	     * This function closes a dropBox by deleting it from the dropboxes.csv file based on the ID parameter
	     * @param idToDelete - the ID of the dropBox to be deleted
	     */
	    private void closeDropboxCSV(String idToDelete)
	    {
	    	System.out.println("ATTEMPTING TO CLOSE THE DROPBOX");
	    	Path path = Paths.get(System.getProperty("user.dir"),"dropboxes.csv");
			
			File newFile = new File(Paths.get(System.getProperty("user.dir"),"temp1.txt").toString());
			File oldFile = new File(path.toString());
			
			String id; String dropboxName; String date; 
			
			try 
			{
				FileWriter fw = new FileWriter(Paths.get(System.getProperty("user.dir"),"temp1.txt").toString(),true);
				BufferedWriter bw = new BufferedWriter(fw); 
				PrintWriter pw = new PrintWriter(bw);
				
				Scanner scanner = new Scanner(new File(path.toString()));
				scanner.useDelimiter("[,\n]");
				
				while(scanner.hasNext())
				{
					id = scanner.next();
					dropboxName = scanner.next();
					scanner.next();
					date = scanner.next();
				
					String printstuff = id+","+dropboxName+",true,"+date;
					
					if(!id.equals(idToDelete))
					{
						pw.println(printstuff.replaceAll("\\r|\\n", ""));
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
	     * This function takes takes a name of a dropBox, checks the "database" and returns true or false if the name 
	     * currently exists in the database
	     * @param name - the name that we are searching to make sure doesn't exist already
	     * @return Depending on if a dropBox with that name exists, the function returns true (yes) or false (no)
	     */
	    private boolean nameExists(String name)
	    {
	    	Path path = Paths.get(System.getProperty("user.dir"),"dropboxes.csv");

	    	String dropboxName; 
			
			try 
			{
				Scanner scanner = new Scanner(new File(path.toString()));
				scanner.useDelimiter("[,\n]");
				
				while(scanner.hasNext())
				{	
					scanner.next();
					dropboxName = scanner.next();
					scanner.next();
					scanner.next();
					
					if(dropboxName.equals(name))
					{
						scanner.close();
						return true;
					}
				}
				scanner.close();	
			}
			catch(Exception e)
			{
				String warning = "Unable to find dropboxes.csv";
				dropboxList.getItems().add(warning);
				e.printStackTrace();
			}
			
			return false;
	    }
	    
	    /**
	     * This function returns true or false based on whether or not a dropBox should be closed based on it's date
	     * @param date - the string representation of the deadline date of a dropBox
	     * @return Based on whether the today is after the deadline, the function returns true (yes) or false (no)
	     * @throws ParseException
	     */
	    private boolean closeDropbox(String date) throws ParseException
	    {
	    	DateTimeFormatter dateID = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime now = LocalDateTime.now();
			String today = dateID.format(now);
	    	
	    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	    	Date d1 = fmt.parse(today);
	    	Date d2 = fmt.parse(date);
	    	
	        if(d1.compareTo(d2) > 0) {
	           return true; 
	        } else if(d1.compareTo(d2) <= 0) {
	          // System.out.println("Date 1 occurs before Date 2 or Both dates are equal");
	        }
	        return false;   
	    }
	    
	    /**
	     * This function reads the dropBox CSV and populates the ListView with the elements from the dropbox
	     */
	    private void readDropboxCSV()
	    {
	    	Path path = Paths.get(System.getProperty("user.dir"),"dropboxes.csv");
			//System.out.println(path.toString());
			
			String id;
	    	String dropboxName; 
			Boolean open; 
			String dropboxDate;
			
			ArrayList<String> dropboxes = new ArrayList<String>(); 
			
			try 
			{
				Scanner scanner = new Scanner(new File(path.toString()));
				scanner.useDelimiter("[,\n]");
				
				while(scanner.hasNext())
				{	
					id = scanner.next();
					dropboxName = scanner.next();
					open = Boolean.valueOf(scanner.next());
					dropboxDate = scanner.next();
				
					if(closeDropbox(dropboxDate))
					{
						System.out.println("SUPPOSED TO CLOSE THE DROPBOX");
						closeDropboxCSV(id);
					}
					else if(open == true)
					{
						System.out.println("-"+dropboxDate.replaceAll("\\r|\\n", "")+"-");
						if(dropboxDate.replaceAll("\\r|\\n", "").equals("9999-99-99"))
						{
							dropboxDate = "no deadline";
						}
						String prop = dropboxName+": "+dropboxDate;
						dropboxes.add(prop.replaceAll("\\r|\\n", ""));
					}
					
				}
				
				dropboxList.getItems().addAll(dropboxes);
				dropboxList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				dropboxList.getSelectionModel().select(0);
				dropboxList.setEditable(false);
				scanner.close();
				
			}
			catch(Exception e)
			{
				String warning = "Unable to find dropboxes.csv";
				dropboxList.getItems().add(warning);
				e.printStackTrace();
			}
	    	
	    }
	   
	    /**
	     * This function gets the ID of a dropBox in the dropboxes.csv folder based on the name of the dropbox
	     * @param dropboxName - the name of the dropBox 
	     * @return the associated ID that goes with the name or "" if no names exist
	     */
	    private String getDropboxID(String dropboxName)
	    {
			String dropboxID;
			Path path = Paths.get(System.getProperty("user.dir"),"dropboxes.csv");
			
			System.out.println("dropboxName:"+dropboxName);
			String name;
			
			try 
			{
				Scanner scanner = new Scanner(new File(path.toString()));
				scanner.useDelimiter("[,\n]");
				
				while(scanner.hasNext())
				{	
					dropboxID = scanner.next();
					System.out.print("dropboxID:"+dropboxID);
					name = scanner.next();
					System.out.println("\tdropboxName:"+name);
					scanner.next();
					scanner.next();
					
					if(dropboxName.equals(name))
					{
						scanner.close();
						return dropboxID;
					}
				}
				scanner.close();
			}
			catch(Exception e)
			{
				String warning = "Unable to find dropboxes.csv";
				dropboxList.getItems().add(warning);
				e.printStackTrace();
			}	
			return "";
	    }
	    
	    /**
	     * This function closes a dropBox that is in the listView
	     * @param event - the click on the "close dropBox" button
	     */
		private void closeDropbox2(ActionEvent event) {
	
			int finalIndex = dropboxList.getSelectionModel().getSelectedItem().indexOf(':');
			String dropboxName = dropboxList.getSelectionModel().getSelectedItem().substring(0,finalIndex);
			if(nameExists(dropboxName)) {
				String id = getDropboxID(dropboxName); 
				System.out.println("The ID: "+id);
				closeDropboxCSV(id);
			}
			else 
			{
				System.out.println("Error deleting dropbox");
			}
			dropboxList.getItems().clear();
			readDropboxCSV();
		}
		
		/**
	     * this function initializes all the JavaFX elements and their eventHandlers
	     */
	    public void initialize(URL url, ResourceBundle rb)
	    {
	    	readDropboxCSV();
	    
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
				catch(Exception e)
	    		{
	    			e.printStackTrace();
	    		}
			});
    
	    	submit.setOnAction(arg0 ->
			{
				try
				{
					createDropbox(arg0);
					dropboxList.getItems().clear();
					readDropboxCSV();
					deadlinePicker.setValue(null);
				}
				catch(Exception e)
	    		{
	    			e.printStackTrace();
	    		}
			});
	    	
	    	closeDropbox.setOnAction(arg0 ->
			{
				try
				{
					closeDropbox2(arg0);

				}
				catch(Exception e)
	    		{
	    			e.printStackTrace();
	    		}
			});
    	
    }
}