package project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
* Controller class for the Registration GUI.
* Allows the user to:
* to create an account consisting of their First and Last Names, Password, Email Address, Affiliation and Account type.
* The user information is stored in the userinfo.csv and users.csv files.
*/
public class addUserController implements Initializable {
	
	//This value is set to true if all the values that the user has selected are valid 
	//This value is false when the page initially loads, and any of the values the user inputs when trying to create an account aren't valid. 
	boolean acceptableReg = false;
	
    @FXML
    private Label nameLabel;
    
    @FXML
    private Label passLabel;
    
    @FXML
    private Label emailLabel;
    
    @FXML
    private Label affilLabel;
    
    @FXML
    private Label accTypeLabel;
    
    @FXML
    private TextField firstName;
    
    @FXML
    private TextField lastName;
    
    @FXML
    private PasswordField passW;
    
    @FXML
    private PasswordField confirmPassW;
    
    @FXML
    private TextField emailAdd;
    
    @FXML
    private TextField confirmEmailAdd;
    
    @FXML
    private TextField affil;
    
    @FXML
    private ChoiceBox<String> accType;

    @FXML
    private Button confirmForm;
    
    @FXML
    private Button submitForm;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    /**
     * This function switches from the registration page to the start page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void cancel (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("startPageGUI.fxml"));
    	Main.stage.setTitle("Start Page");
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    }
    
    @FXML
    /**
     * This function makes sure that the account being created hasn't already been created.
     * @param event - button click event
     */
    private void errChecking (ActionEvent event) {
    	
    	int Check_1 = 0;
    	int Check_2 = 0;
    	int Check_3 = 0;
    	int Check_4 = 0;
    	int Check_5 = 0;
    	
        String checkSelection = accType.getSelectionModel().getSelectedItem();
        String emailFormat = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    	
        //	Error Checking for First and Last Names
        if (firstName.getText().length() <= 0 || lastName.getText().length() <= 0) {
        	nameLabel.setText("That name is invalid!");
        	nameLabel.setAlignment(Pos.CENTER);
        	nameLabel.setStyle("-fx-background-color: #ff652f");
        	
        	Check_2 = 0;
        	
        } else {
        	nameLabel.setText("Satisfactory");
        	nameLabel.setAlignment(Pos.CENTER);
        	nameLabel.setStyle("-fx-background-color: #14a75c");
        	
        	Check_1 = 1;
        }
        
        // Error checking for Password
        if (!passW.getText().equals(confirmPassW.getText())) {
        	passLabel.setText("Your Passwords don't match!");
        	passLabel.setAlignment(Pos.CENTER);
        	passLabel.setStyle("-fx-background-color: #ff652f");
        	
        	passW.clear();
        	confirmPassW.clear();
        	
        	Check_2 = 0;
        	
        } else if (passW.getText().length() <= 0 || confirmPassW.getText().length() <= 0){
        	passLabel.setText("Enter and confirm your password!");
        	passLabel.setAlignment(Pos.CENTER);
        	passLabel.setStyle("-fx-background-color: #ff652f");
        	
        	Check_2 = 0;
        	
        } else {
        	passLabel.setText("Satisfactory");
        	passLabel.setStyle("-fx-background-color: #14a75c");
        	
        	Check_2 = 1;
        }
        
        // Error checking for Email Address
        if (!emailAdd.getText().equals(confirmEmailAdd.getText())) {
        	emailLabel.setText("Your Email doesn't match!");
        	emailLabel.setAlignment(Pos.CENTER);
        	emailLabel.setStyle("-fx-background-color: #ff652f");
        	
        	emailAdd.clear();
        	confirmEmailAdd.clear();
        	
        	Check_3 = 0;
        	
        } else if (emailAdd.getText().length() <= 0 || confirmEmailAdd.getText().length() <= 0){
        	emailLabel.setText("Enter and confirm your Email!");
        	emailLabel.setAlignment(Pos.CENTER);
        	emailLabel.setStyle("-fx-background-color: #ff652f");
        	
        	Check_3 = 0;
        	
        } else if (!emailAdd.getText().matches(emailFormat)){
        	emailLabel.setText("That isn't a valid Email!");
        	emailLabel.setAlignment(Pos.CENTER);
        	emailLabel.setStyle("-fx-background-color: #ff652f");
        	
        	Check_3 = 0;
        	
        } else {
        	emailLabel.setText("Satisfactory");
        	emailLabel.setStyle("-fx-background-color: #14a75c");
        	
        	Check_3 = 1;
        }
        
        // Error checking for Affiliation
        if (affil.getText().length() <= 0){
        	affilLabel.setText("Enter your Affiliation!");
        	affilLabel.setAlignment(Pos.CENTER_LEFT);
        	affilLabel.setStyle("-fx-background-color: #ff652f");
        	
        	Check_4 = 0;
        	
        } else {
        	affilLabel.setText("Satisfactory");
        	affilLabel.setAlignment(Pos.CENTER_LEFT);
        	affilLabel.setStyle("-fx-background-color: #14a75c");
        	
        	Check_4 = 1;
        }
        
        // Error checking for Account Type selection
        if (checkSelection == null){
        	accTypeLabel.setText("Enter your Account Type!");
        	accTypeLabel.setAlignment(Pos.CENTER_LEFT);
        	accTypeLabel.setStyle("-fx-background-color: #ff652f");
        	
        	Check_5 = 0;
        	
        } else {
        	accTypeLabel.setText("Satisfactory");
        	accTypeLabel.setAlignment(Pos.CENTER_LEFT);
        	accTypeLabel.setStyle("-fx-background-color: #14a75c");
        	
        	Check_5 = 1;
        }
        
        // Determines whether the form is acceptable for submission
        if (Check_1 + Check_2 + Check_3 + Check_4 + Check_5 == 5) {
        	acceptableReg = true;
        }
        else {
        	acceptableReg = false;
        }
    }

    @FXML
    /**
     * This function stores the users information into the appropriate CSV files.
     * If the user is a reviewer, a reviewer ID is created for them which is then written to reviewer_info.csv along with their first name, last name, email, and affiliation.
     * If the user is an author, an author ID is created for them which is then written to author_info.csv along with their first name, last name, email, and affiliation.
     * The user's first name, last name, email, affiliation, and account type is written to users_info.csv
     * The user's email, password, and account type is written to users.csv
     * @param event - the button click event 
     */
    private void handleSubmit (ActionEvent event)
    {
    	if (acceptableReg == true) {
	    	// Gets all the necessary text and stores them as strings
	        String firstNameInput = firstName.getText();
	        String lastNameInput = lastName.getText();
	        String passwordInput = passW.getText();
	        String emailInput = emailAdd.getText();
	        String affilInput = affil.getText();
	        String selectedAccType = accType.getSelectionModel().getSelectedItem();
	        
	        String filepath1 = "user_info.csv";
	        String filepath2 = "users.csv";
	        String filepath3 = "author_info.csv";
	        String filepath4 = "reviewer_info.csv";
	        
	    	DateTimeFormatter dateID = DateTimeFormatter.ofPattern("ddMMyyyHHmmss");  
	    	LocalDateTime now = LocalDateTime.now();  
	    	
            try {
            	
            	// Storing User's information
            	FileWriter fw1 = new FileWriter(filepath1, true);
            	BufferedWriter bw1 = new BufferedWriter(fw1);
            	PrintWriter pw1 = new PrintWriter(bw1);
            	
            	pw1.println(firstNameInput+","+lastNameInput+","+emailInput+","+affilInput+","+selectedAccType);
            	pw1.flush();
            	pw1.close();

            	// Storing User's password with the associated Name(s)
            	FileWriter fw2 = new FileWriter(filepath2, true);
            	BufferedWriter bw2 = new BufferedWriter(fw2);
            	PrintWriter pw2 = new PrintWriter(bw2);

            	pw2.println(emailInput+","+passwordInput+","+selectedAccType);
            	pw2.flush();
            	pw2.close();
            	
            	// Storing Author account type User information
            	if (selectedAccType == "Author") {
                	FileWriter fw3 = new FileWriter(filepath3, true);
                	BufferedWriter bw3 = new BufferedWriter(fw3);
                	PrintWriter pw3 = new PrintWriter(bw3);

                	pw3.println("A"+dateID.format(now)+","+firstNameInput+","+lastNameInput+","+emailInput+","+affilInput);
                	pw3.flush();
                	pw3.close();
                	
            	}
            	// Storing Reviewer account type User information
            	if (selectedAccType == "Reviewer") {
                	FileWriter fw4 = new FileWriter(filepath4, true);
                	BufferedWriter bw4 = new BufferedWriter(fw4);
                	PrintWriter pw4 = new PrintWriter(bw4);

                	pw4.println("R"+dateID.format(now)+","+firstNameInput+","+lastNameInput+","+emailInput+","+affilInput);
                	pw4.flush();
                	pw4.close();
                	
            	}
            	
            }
            catch (Exception E) {
            	System.out.println("EXCEPTION e!");
            }
            
            JOptionPane.showMessageDialog(null, "Sign Up Successful!");
            
        	try
    	   	{
            	Parent root = FXMLLoader.load(getClass().getResource("LoginUserGUI.fxml"));
            	Main.stage.setTitle("Login");
            	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    	   	}
    	   	catch (Exception e)
    	   	{
    	   		e.printStackTrace();
			}
            
        } else {
        	JOptionPane.showMessageDialog(null, "Confirm the form before you submit!");
        }
    }
    
    
    /**
	 * This function initializes the JavaFx controllers on the registration page.
	 */
    public void initialize(URL url, ResourceBundle rb){
        ObservableList<String> accTypeChoices = FXCollections.observableArrayList("Author", "Reviewer");
        accType.setItems(accTypeChoices);

        confirmForm.setOnAction(this::errChecking);
        submitForm.setOnAction(this::handleSubmit);
        cancelButton.setOnAction(arg0 ->
        {
			try
			{
				cancel(arg0);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
    }  
}