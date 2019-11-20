/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.Main;
import benhardestyappointmentscheduler.database.DAO;
import benhardestyappointmentscheduler.util.AlertUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class RegisterController implements Initializable {
    
    private Main main;
    private Stage stage;
    
    @FXML
    private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
    }
    
    /**
     * Validate the user registration input.
     * 
     * @return boolean
     */
    private boolean validate() {
        boolean valid = true;
        String errorMessage = "";
        
        if (userName.getText().isEmpty() || userName.getText().equals("")) {
            errorMessage += "Username is required\n";
            valid = false;
        }
        
        if (password.getText().isEmpty() || password.getText().equals("")) {
            errorMessage += "Password is required.\n";
            valid = false;
        }
        
        if (!password.getText().equals(confirmPassword.getText())) {
            errorMessage += "Password and confirm password must match.\n";
            valid = false;
        }
        
        if (!valid) {
            AlertUtil.showErrorAlert(stage, "Error", "Invalid Input", errorMessage);
        }
        
        return valid;
    }
    
    @FXML
    public void handleRegister() {
        if (validate()) {
            
            // Register a new user in the database.
            if (DAO.registerUser(userName.getText(), password.getText())) {
                AlertUtil.showInformationAlert(stage, "Success", "Success", "Account created!");
                this.main.loadLoginLayout();
            } else {
                AlertUtil.showErrorAlert(stage, "Error", "Error", "Username already exists.");
            }
        }
    }
    
    /**
     * Cancel user registration.
     * Take the user back to the login form.
     */
    @FXML
    private void handleCancel() {
        this.main.loadLoginLayout();
    }
    
}
