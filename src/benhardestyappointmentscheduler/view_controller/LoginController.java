/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.Main;
import benhardestyappointmentscheduler.database.DAO;
import benhardestyappointmentscheduler.util.AlertUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class LoginController implements Initializable {
    
    private Stage stage;
    private Main main;
    private Locale locale;
    
    @FXML
    private Label userNameLabel;
    @FXML
    private TextField userName;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        locale = Locale.getDefault();
    }    
    
    /**
     * Called by the main application to give this controller access to the
     * stage and the main application.
     * 
     * @param main
     * @param stage 
     */
    public void setStage(Main main, Stage stage) {
        this.main = main;
        this.stage = stage;
        
        // If the user is in Greece, update the language on the login form to 
        // greek.
        if (locale.getCountry().equals("GR")) {
            userNameLabel.setText("όνομα χρήστη");
            passwordLabel.setText("Κωδικός πρόσβασης");
            loginButton.setText("Σύνδεση");
            registerButton.setText("έντυπο εγγραφής");
        }
    }
    
    /**
     * Validate the login info.
     * 
     * @return boolean
     */
    private boolean validate() {
        boolean valid = true;
        String errorMessage = "";
        
        if (userName.getText().isEmpty() || userName.getText().equals("")) {
            if (locale.getCountry().equals("GR")) {
                errorMessage += "όνομα χρήστη απαιτείται\n";
            } else {
                errorMessage += "Username is required\n";
            }
            valid = false;
        }
        
        if (password.getText().isEmpty() || password.getText().equals("")) {
            if (locale.getCountry().equals("GR")) {
                errorMessage += "Απαιτείται κωδικός\n";
            } else {
                errorMessage += "Password is required.\n";
            }
            valid = false;
        }
        
        if (!valid) {
            if (locale.getCountry().equals("GR")) {
                AlertUtil.showErrorAlert(stage, "λάθος", "μη έγκυρη είσοδο", errorMessage);
            } else {
                AlertUtil.showErrorAlert(stage, "Error", "Invalid Input", errorMessage);
            }
        }
        
        return valid;
    }
    
    /**
     * Log a user into the application.
     */
    @FXML
    private void handleLogin() {
        
        if (validate()) {
            
            Integer userId = null;
            
            // Get the user id for the provided username and password.
            try {
                
                // An exception will be thrown if the login fails.
                userId = DAO.login(userName.getText(), password.getText());
            } catch (Exception e) {
                if (locale.getCountry().equals("GR")) {
                    AlertUtil.showErrorAlert(stage, "λάθος", "λάθος", "η σύνδεση απέτυχε");
                } else {
                    AlertUtil.showErrorAlert(stage, "Error", "Error", e.getMessage());
                }
                return;
            }

            try {
                // Log user login activity.
                String fileName = "log.txt";
                File file = new File(fileName);
                    
                // Create a new log file if it doesn't already exist.
                if (!file.exists()) {
                    file.createNewFile();
                }
                    
                // Set up the logger and file handler.
                FileHandler fileHandler = new FileHandler(fileName, true);
                Logger logger = Logger.getLogger(getClass().getName());
                logger.addHandler(fileHandler);
                logger.setLevel(Level.INFO);
                    
                // Log the user login.
                logger.info(Calendar.getInstance().getTime() + ": User " + userId + " logged.");
            } catch (IOException | SecurityException e) {
                
            }
            
            // Set the userId field of the main application.
            // I would never send a userId to the client side, but the rubric
            // requires the database functions be implemented on the client side.
            this.main.setUserId(userId);
                
            // Load the calendar layout.
            this.main.loadCalendarLayout();
        }
    }
    
    /**
     * Send user to the registration page.
     */
    @FXML
    private void handleRegister() {
        this.main.loadRegisterLayout();
    }
    
}
