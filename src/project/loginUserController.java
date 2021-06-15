package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
* Controller class for the Log in GUI.
* Allows the user to login with e-mail and password and verifies against the users.csv file.
*/
public class loginUserController implements Initializable
{
	private static Scanner input;
	private static Scanner curUserA;
	private static Scanner curUserR;
	boolean acceptableLogin = false;
    
    @FXML
    private TextField userEmail;
    
    @FXML
    private PasswordField passW;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button cancelButton;
    
	@FXML
	 /**
     * This function switches from the log in page to the start page 
     * @param event - the button click event 
     * @throws Exception
     */
    private void cancel (ActionEvent event) throws Exception
    {
    	Parent root = FXMLLoader.load(getClass().getResource("startPageGUI.fxml"));
    	Main.stage.setScene(new Scene(root, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
    	Main.stage.setTitle("Start Page");
    }
	
	@FXML
	/**
	 * This function makes sure that the email that was typed exist and matches with the password that was typed.
	 * This function also writes the logged in user's information to current_user.csv.  
	 * @param event - button click event.
	 */
	private void handleLogin (ActionEvent event)
    {
		boolean found = false;
        String emailInput = userEmail.getText();
        String passwordInput = passW.getText();
        String tempUsername = "";
		String tempPassword = "";
		String tempAccountType = "";
		String tempIdA = "";
		String tempIdR = "";
		String filename = "users.csv";
		String filenameAuth = "author_info.csv";
		String filenameRev = "reviewer_info.csv";
		
    	DateTimeFormatter dateID = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");  
    	LocalDateTime now = LocalDateTime.now();  

		try
		{
			input = new Scanner(new File (filename)); 	//reads the users.csv file 
			input.useDelimiter("[,\n]");				//each field is separated by a comma, and each record separated by a new line
			
			curUserA = new Scanner(new File (filenameAuth));
			curUserA.useDelimiter("[,\n]");
			
			curUserR = new Scanner(new File (filenameRev));
			curUserR.useDelimiter("[,\n]");
		
			while(input.hasNext() && !found)
			{
				tempUsername = input.next();
				tempPassword = input.next();
				tempAccountType = input.next();
				
				if (tempUsername.trim().contentEquals(emailInput.trim()) && tempPassword.trim().contentEquals(passwordInput.trim()))
				{
					JOptionPane.showMessageDialog(null, "User with "+tempAccountType+" account found! Fetching "+tempAccountType+" Interface");
					
					if (tempUsername.trim().contentEquals("admin") && tempPassword.trim().contentEquals("admin") && tempAccountType.trim().contentEquals("Editor"))
					{
						try {
							String filepath = "current_user.csv";
							FileWriter fwAD = new FileWriter(filepath, false);
							BufferedWriter bwAD = new BufferedWriter(fwAD);
							PrintWriter pwAD = new PrintWriter(bwAD);
							pwAD.println("admin"+","+"AD"+","+dateID.format(now));
							pwAD.flush();
							pwAD.close();
							
							JOptionPane.showMessageDialog(null, "Editor Account Authorized");
							Parent editAdmin = FXMLLoader.load(getClass().getResource("editorMainGUI.fxml"));
							Main.stage.setTitle("Homepage - Editor");
					    	Main.stage.setScene(new Scene(editAdmin, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
						}
						catch (Exception e)
						{
							
						}
					}
					
					else if (tempAccountType.trim().contentEquals("Author"))
					{
						while(curUserA.hasNext() && !found) {
							tempIdA = curUserA.next();
							curUserA.next();
							curUserA.next();
							String tempEmailA = curUserA.next();
							curUserA.next();
							
							if (tempUsername.trim().contentEquals(tempEmailA.trim())) {
								String userIdA = tempIdA;
								
								try {
									String filepath = "current_user.csv";
									FileWriter fwAuth = new FileWriter(filepath, false);
									BufferedWriter bwAuth = new BufferedWriter(fwAuth);
									PrintWriter pwAuth = new PrintWriter(bwAuth);
									pwAuth.println(tempUsername+","+userIdA+","+dateID.format(now));
									pwAuth.flush();
									pwAuth.close();
									
									Parent authUser = FXMLLoader.load(getClass().getResource("authorMainGUI.fxml"));
									Main.stage.setTitle("Homepage - Author");
							    	Main.stage.setScene(new Scene(authUser, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
							    	System.out.println("The author we're looking for!");
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
							else 
							{
								System.out.println("Not the author we're looking for!");
							}
						}
					}
					else if (tempAccountType.trim().contentEquals("Reviewer")) 
					{
						while(curUserR.hasNext() && !found) {
							tempIdR = curUserR.next();
							curUserR.next();
							curUserR.next();
							String tempEmailR = curUserR.next();
							curUserR.next();
							
							if (tempUsername.trim().contentEquals(tempEmailR.trim())) {
								String userIdR = tempIdR;
								
								try {
									String filepath = "current_user.csv";
									FileWriter fwRev = new FileWriter(filepath, false);
									BufferedWriter bwRev = new BufferedWriter(fwRev);
									PrintWriter pwRev = new PrintWriter(bwRev); 
									pwRev.println(tempUsername+","+userIdR+","+dateID.format(now));
									pwRev.flush();
									pwRev.close();
									
									Parent revUser = FXMLLoader.load(getClass().getResource("reviewerMainGUI.fxml"));
									Main.stage.setTitle("Homepage - Reviewer");
							    	Main.stage.setScene(new Scene(revUser, Main.screenSize.getWidth(), Main.screenSize.getHeight()));
							    	System.out.println("The reviewer we're looking for!");
								}
								catch (Exception e){
									e.printStackTrace();
								}
							}
							else {
								System.out.println("Not the reviewer we're looking for!");
							}	
						}
					}
					return;
				}
			}
			input.close();
			JOptionPane.showMessageDialog(null, "Invalid Login Attempt");
		}
		catch(Exception e)
		{
			System.out.println("Something ain't right, and it is not because of your username and password!");
		}
    }

	
	/**
	 * This function initializes the JavaFx controllers on the log in page.
	 */
    public void initialize(URL url, ResourceBundle rb)
    {

        loginButton.setOnAction(this::handleLogin);
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
