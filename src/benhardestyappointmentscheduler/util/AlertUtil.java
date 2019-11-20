/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Ben Hardesty
 */
public class AlertUtil {
    
    /**
     * Show an error alert.
     * 
     * @param stage
     * @param title
     * @param headerText
     * @param contentText 
     */
    public static void showErrorAlert(Stage stage, String title, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    /**
     * Show an informational alert.
     * 
     * @param stage
     * @param title
     * @param headerText
     * @param contentText 
     */
    public static void showInformationAlert(Stage stage, String title, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    /**
     * Show a confirmation alert.
     * 
     * @param stage
     * @param title
     * @param contentText
     * @return 
     */
    public static Optional<ButtonType> showConfirmationAlert(Stage stage, String title,String contentText) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        
        return result;
    }
}
