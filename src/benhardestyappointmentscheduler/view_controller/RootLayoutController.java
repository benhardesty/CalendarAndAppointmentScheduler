/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.util.AlertUtil;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class RootLayoutController implements Initializable {
    
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    /**
     * Called by the main application to give this controller access to it's 
     * stage.
     * 
     * @param stage 
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Close the application.
     */
    @FXML
    private void handleExit() {
        
        // Have the user confirm they wish to close the application.
        Optional<ButtonType> result = AlertUtil.showConfirmationAlert(stage, 
                "Confirmation", "Click 'OK' to confirm you wish to close\n"
                        + "the application.");
        
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }
    
}
