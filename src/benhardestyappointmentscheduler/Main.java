/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler;

import benhardestyappointmentscheduler.model.Appointment;
import benhardestyappointmentscheduler.model.Customer;
import benhardestyappointmentscheduler.view_controller.AddOrEditCustomerController;
import benhardestyappointmentscheduler.view_controller.AppointmentController;
import benhardestyappointmentscheduler.view_controller.CalendarController;
import benhardestyappointmentscheduler.view_controller.LoginController;
import benhardestyappointmentscheduler.view_controller.RegisterController;
import benhardestyappointmentscheduler.view_controller.ReportsController;
import benhardestyappointmentscheduler.view_controller.RootLayoutController;
import benhardestyappointmentscheduler.view_controller.SelectCustomerController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Ben Hardesty
 */
public class Main extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    // I would never send a userId to the client side, but since the rubric
    // requires the database functions be implemented on the client side,
    // I have to retrieve the userIds. This really should be implemented
    // with a session ID.
    private int userId;
    
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        loadRootLayout();
        loadLoginLayout();
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Load a BorderPane layout as the root layout of the application.
     */
    public void loadRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            RootLayoutController controller = loader.getController();
            controller.setStage(primaryStage);
            
            Scene scene = new Scene(rootLayout);
            
            primaryStage.setTitle("Appointment Scheduler");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Load the login stage.
     */
    public void loadLoginLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/Login.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            rootLayout.setCenter(pane);
            
            // Give the controller for the new scene access to the current stage.
            LoginController controller = loader.getController();
            controller.setStage(this, primaryStage);
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Load the register stage.
     */
    public void loadRegisterLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/Register.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            rootLayout.setCenter(pane);
            
            // Give the controller for the new scene access to the current stage.
            RegisterController controller = loader.getController();
            controller.setStage(this, primaryStage);
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Load the calendar layout.
     */
    public void loadCalendarLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/Calendar.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            rootLayout.setCenter(pane);
            
            // Give the controller for the new scene access to the current stage.
            CalendarController controller = loader.getController();
            controller.setStage(this, primaryStage, pane, userId);
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Load the appointment layout.
     * 
     * @param appointment
     * @param customer
     * @return 
     */
    public boolean loadAppointmentLayout(Appointment appointment, Customer customer) {
        try {
            // Load the fxml for the new scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/Appointment.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            // Create a stage for the new scene.
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            
            // Give the controller a reference to the stage
            AppointmentController controller = loader.getController();
            
            // If a customer object was passed in, view the appointment for editing.
            if (customer != null) {
                stage.setTitle("Edit Appointment");
                controller.setStage(this, stage, userId, appointment, customer, "update", pane);
                
            // Else, load a blank appointment view.
            } else {
                stage.setTitle("New Appointment");
                controller.setStage(this, stage, userId, appointment, customer, "add", pane);
            }
            
            // Show the scene.
            stage.showAndWait();
            
            // Return whether or not an appointment was created or updated.
            return controller.getAppointmentCreatedOrUpdated();
            
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Load the reports layout.
     */
    public void loadReportsLayout() {
        try {
            // Load the fxml for the new scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/Reports.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            // Create a stage for the new scene.
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            
            // Give the controller a reference to the stage
            ReportsController controller = loader.getController();
            controller.setStage(this, stage, userId, pane);
            
            // Show the scene.
            stage.showAndWait();
            
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Load the Select Customer layout.
     * @return 
     */
    public Customer loadSelectCustomerLayout() {
        try {
            // Load the fxml for the new scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/SelectCustomer.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            // Create a stage for the new scene.
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Customers");
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            
            // Give the controller a reference to the stage
            SelectCustomerController controller = loader.getController();
            controller.setStage(this, stage, userId);
            
            // Show the scene.
            stage.showAndWait();
            
            // Return the selected customer.
            return controller.getSelectedCustomer();
            
        } catch (IOException e) {
            
        }
        
        // Return null if no customer was selected.
        return null;
    }
    
    /**
     * Load the Add Or Edit Customer layout.
     * 
     * @param customer
     * @return boolean
     */
    public boolean loadAddOrEditCustomerLayout(Customer customer) {
        try {
            // Load the fxml for the new scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/AddOrEditCustomer.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            // Create a stage for the new scene.
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            
            // Give the controller a reference to the stage
            AddOrEditCustomerController controller = loader.getController();
            
            if (customer == null) {
                stage.setTitle("Create Customer");
                controller.setStage(this, stage, pane, "add", customer, userId);
            } else {
                stage.setTitle("Edit Customer");
                controller.setStage(this, stage, pane, "edit", customer, userId);
            }
            
            // Show the scene.
            stage.showAndWait();
            
            // Return whether a customer was created or updated.
            return controller.getCustomerCreatedOrUpdated();
            
        } catch (IOException e) {
            // If an exception occurs, return true just in case so that the
            // customers list is refreshed.
            return true;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
