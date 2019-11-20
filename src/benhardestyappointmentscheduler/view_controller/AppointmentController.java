/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.Main;
import benhardestyappointmentscheduler.database.DAO;
import benhardestyappointmentscheduler.model.Appointment;
import benhardestyappointmentscheduler.model.Customer;
import benhardestyappointmentscheduler.util.AlertUtil;
import benhardestyappointmentscheduler.util.DateTimeUtil;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class AppointmentController implements Initializable {
    
    private Main main;
    private Stage stage;
    private Appointment appointment;
    private Customer customer;
    private int userId;
    private boolean appointmentCreatedOrUpdated;
    private String addOrUpdate;
    private AnchorPane rootPane;
    
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField customerTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox startChoiceBox;
    @FXML
    private ChoiceBox endChoiceBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    
    @FXML
    private void handleSelectCustomer() {
        
        // Load the selectCustomerLayout and retreive the selected customer.
        Customer c = main.loadSelectCustomerLayout();
        
        // Update the UI with the selected customer.
        if (c != null) {
            customer = c;
            customerTextField.setText(customer.getCustomerName());
            locationTextField.setText(customer.getAddress().getAddress() + " " +
                    customer.getAddress().getAddress2() + " " + 
                    customer.getAddress().getCity().getCity() + " " +
                    customer.getAddress().getCity().getCountry().getCountry());
        }
    }
    
    @FXML
    private void handleSave() {
        if (validate()) {
            
            int customerId = customer.getCustomerId();
            String title = titleTextField.getText();
            String description = descriptionTextArea.getText();
            String location = locationTextField.getText();
            int contact = userId;
            String url = String.valueOf(customerId);
            Date start = DateTimeUtil.convert_time_to_date(datePicker.getValue(), 
                    (String) startChoiceBox.getSelectionModel().getSelectedItem());
            Date end = DateTimeUtil.convert_time_to_date(datePicker.getValue(), 
                    (String) endChoiceBox.getSelectionModel().getSelectedItem());
            String type = typeTextField.getText();
            
            try {
                // Check if the proposed updated appointment is within business hours.
                DateTimeUtil.withinBusinessHours(start);
                DateTimeUtil.withinBusinessHours(end);
            } catch (Exception e) {
                AlertUtil.showErrorAlert(stage, "Error", "Error", 
                        "Appointment must be within business hours.\n"
                                + "Business hours are between 7:00 am and 7:00 pm.");
                return;
            }
            
            // Change the timezone to America/Los_Angeles.
            TimeZone myTimeZone = TimeZone.getDefault();
            TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
            
            // Check if the proposed appointment overlaps with an existing appointment.
            try {
                // Throws an exception if overlapping appointments exist.
                DAO.overlappingAppointments(start, end, 0, userId);
            } catch (Exception e) {
                // Set the timezone back to the user's timezone.
                TimeZone.setDefault(myTimeZone);
                
                // Alert the user.
                AlertUtil.showErrorAlert(stage, "Error", "Error", e.getMessage());
                return;
            }
            
            // Add the customer to the database.
            boolean created = DAO.createAppointment(customerId, title, description, 
                    location, contact, url, start, end, type, userId);
            
            // If adding the customer was successful.
            if (created) {
                
                // Alert the user.
                AlertUtil.showInformationAlert(stage, "Success", "Success", "Appointment created!");
                appointmentCreatedOrUpdated = true;
                
                // Set the timezone back to the user's timezone.
                TimeZone.setDefault(myTimeZone);
                
                // Close the stage.
                stage.close();
            } else {
                
                // Set the timezone back to the user's timezone.
                TimeZone.setDefault(myTimeZone);
                
                // Alert the user.
                AlertUtil.showErrorAlert(stage, "Error", "Error", 
                        "An error occured.");
            }
        }
    }
    
    @FXML
    private void handleCancel() {
        stage.close();
    }
    
    private void updateAppointment() {
        if (validate()) {
            
            int appointmentId = appointment.getAppointmentId();
            int customerId = customer.getCustomerId();
            String title = titleTextField.getText();
            String description = descriptionTextArea.getText();
            String location = locationTextField.getText();
            int contact = userId;
            String url = String.valueOf(customerId);
            Date start = DateTimeUtil.convert_time_to_date(datePicker.getValue(), 
                    (String) startChoiceBox.getSelectionModel().getSelectedItem());
            Date end = DateTimeUtil.convert_time_to_date(datePicker.getValue(), 
                    (String) endChoiceBox.getSelectionModel().getSelectedItem());
            String type = typeTextField.getText();
            
            try {
                // Check if the proposed updated appointment is within business hours.
                DateTimeUtil.withinBusinessHours(start);
                DateTimeUtil.withinBusinessHours(end);
            } catch (Exception e) {
                AlertUtil.showErrorAlert(stage, "Error", "Error", 
                        "Appointment must be within business hours.\n"
                                + "Business hours are between 7:00 am and 7:00 pm.");
                return;
            }
            
            // Change the timezone to America/Los_Angeles.
            TimeZone myTimeZone = TimeZone.getDefault();
            TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
            
            // Check if the proposed appointment overlaps with an existing appointment.
            try {
                // Throws an exception if overlapping appointments exist.
                DAO.overlappingAppointments(start, end, appointmentId, userId);
            } catch (Exception e) {
                // Set the timezone back to the user's timezone.
                TimeZone.setDefault(myTimeZone);
                
                // Alert the user.
                AlertUtil.showErrorAlert(stage, "Error", "Error", e.getMessage());
                return;
            }
            
            // Update the customer in the database.
            boolean updated = DAO.updateAppointment(appointmentId, customerId, 
                    title, description, location, contact, url, start, end, type, userId);
            
            // If update was successful.
            if (updated) {
                // Alert the user.
                AlertUtil.showInformationAlert(stage, "Success", "Success", "Appointment updated!");
                appointmentCreatedOrUpdated = true;
                
                // Set the timezone back to the user's timezone and close the stage.
                TimeZone.setDefault(myTimeZone);
                stage.close();
            } else {
                // Set the timezone back to the user's timezone and alert the user.
                TimeZone.setDefault(myTimeZone);
                AlertUtil.showErrorAlert(stage, "Error", "Error", 
                        "An error occured.");
            }
        }
    }
    
    /**
     * Delete an appointment from the database.
     */
    private void deleteAppointment() {
        
        // Have the user confirm they meant to delete a appointment.
        Optional<ButtonType> result = AlertUtil.showConfirmationAlert(stage, 
                "Confirmation", "Click 'OK' to confirm you wish to delete\n"
                        + "the appointment.");
        
        if (result.get() == ButtonType.OK) {
            
            boolean deleted = DAO.deleteAppointment(appointment.getAppointmentId());
            
            if (deleted) {
                AlertUtil.showInformationAlert(stage, "Success", "Success", "Appointment deleted.");
                appointmentCreatedOrUpdated = true;
                stage.close();
            } else {
                AlertUtil.showErrorAlert(stage, "Error", "Error", "There was an error.");
            }
        }
    }
    
    /**
     * Validate the form input.
     * 
     * @return 
     */
    private boolean validate() {
        boolean valid = true;
        String errorMessage = "";
        
        if (titleTextField.getText().equals("") || titleTextField.getText().isEmpty()) {
            valid = false;
            errorMessage += "Title is required.\n";
        } else {
            if (titleTextField.getText().length() > 255) {
                valid = false;
                errorMessage += "Title cannot be longer than 255 characters.\n";
            }
        }
        
        if (typeTextField.getText().equals("") || typeTextField.getText().isEmpty()) {
            valid = false;
            errorMessage += "Type is required.\n";
        }
        
        if (customer == null) {
            valid = false;
            errorMessage += "A customer must be selected.\n";
        }
        
        if (locationTextField.getText().equals("") || locationTextField.getText().isEmpty()) {
            valid = false;
            errorMessage += "Location is required.\n";
        }
        
        try {
            LocalDate date = datePicker.getValue();
            if (date == null) {
                valid = false;
                errorMessage += "Date is required.\n";
            }
        } catch (Exception e) {
            valid = false;
            errorMessage += "Date is required.\n";
        }
        
        if (startChoiceBox.getSelectionModel().getSelectedIndex() == -1) {
            valid = false;
            errorMessage += "Start time is required\n";
        }
        
        if (endChoiceBox.getSelectionModel().getSelectedIndex() == -1) {
            valid = false;
            errorMessage += "End time is required.\n";
        }
        
        if (endChoiceBox.getSelectionModel().getSelectedIndex() <= startChoiceBox.getSelectionModel().getSelectedIndex()) {
            valid = false;
            errorMessage += "The end time must be after the start time.\n";
        }
        
        // If the input is not valid, display an alert to the user.
        if (!valid) {
            AlertUtil.showErrorAlert(stage, "Error", "Invalid Input", errorMessage);
        }
        
        return valid;
    }
    
    /**
     * Load the appointment view with information from a passed in appointment 
     * and customer.
     * 
     * @param appointment
     * @param customer 
     */
    private void loadAppointment(Appointment appointment, Customer customer) {
        
        // Load the text fields.
        titleTextField.setText(appointment.getTitle());
        typeTextField.setText(appointment.getType());
        customerTextField.setText(customer.getCustomerName());
        locationTextField.setText(appointment.getLocation());
        descriptionTextArea.setText(appointment.getDescription());
        
        // Load the date picker.
        Calendar c = Calendar.getInstance();
        c.setTime(appointment.getStart());
        LocalDate date = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, 
                c.get(Calendar.DAY_OF_MONTH));
        datePicker.setValue(date);
        
        // Load the time choiceboxes.
        String startString = DateTimeUtil.retreive_time_of_day_from_date(appointment.getStart());
        startChoiceBox.getSelectionModel().select(startString);
        String endString = DateTimeUtil.retreive_time_of_day_from_date(appointment.getEnd());
        endChoiceBox.getSelectionModel().select(endString);
        
        
    }
    
    /**
     * Return whether or not an appointment was created or updated.
     * @return boolean
     */
    public boolean getAppointmentCreatedOrUpdated() {
        return appointmentCreatedOrUpdated;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customer = null;
        appointmentCreatedOrUpdated = false;
    }    
    
    /**
     * Give this class access to the main application, the userId, the customer
     * and appointment (if any), and the root layout for the scene.
     * 
     * @param main
     * @param stage
     * @param userId
     * @param appointment
     * @param customer
     * @param addOrUpdate
     * @param pane 
     */
    public void setStage(Main main, Stage stage, int userId, Appointment appointment,
            Customer customer, String addOrUpdate, AnchorPane pane) {
        
        this.main = main;
        this.stage = stage;
        this.userId = userId;
        this.appointment = appointment;
        this.customer = customer;
        this.addOrUpdate = addOrUpdate;
        this.rootPane = pane;
        
        // Add time options to start and end choiceboxes.
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 4; j++) {
                String hour = String.valueOf(i % 12);
                String minute = String.valueOf(15*j);
                String AMPM = "AM";
                
                if (i > 11) {
                    AMPM = "PM";
                }
                
                if (i == 0 || i == 12) {
                    hour = "12";
                }
                
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                
                String time = hour + ":" + minute + " " + AMPM;
                startChoiceBox.getItems().add(time);
                endChoiceBox.getItems().add(time);
            }
        }
        
        // Update the UI if an appointment is being updated rather than a new one
        // being made.
        if (addOrUpdate.equals("update")) {
            
            // Adjust layout constraints of the gridPane to make room for a 
            // delete button.
            GridPane gridPane = (GridPane) pane.getChildren().get(0);
            AnchorPane.setBottomAnchor(gridPane, 118.0);
            
            // Change the save button to an edit button.
            pane.getChildren().remove(1);
            Button editButton = new Button("Save Changes");
            AnchorPane.setBottomAnchor(editButton, 82.0);
            AnchorPane.setRightAnchor(editButton, 10.0);
            AnchorPane.setLeftAnchor(editButton, 10.0);
            pane.getChildren().add(editButton);
            
            // Create a delete button and add it to the scene.
            Button deleteButton = new Button("Delete Appointment");
            AnchorPane.setBottomAnchor(deleteButton, 46.0);
            AnchorPane.setRightAnchor(deleteButton, 10.0);
            AnchorPane.setLeftAnchor(deleteButton, 10.0);
            pane.getChildren().add(2, deleteButton);
            
            // Using lambda expression to improve readability.
            editButton.setOnAction(action -> {
                updateAppointment();
            });
            
            // Using lambda expression to improve readability.
            deleteButton.setOnAction(action -> {
                deleteAppointment();
            });
            
            cancelButton.setText("Close");
            
            // Load appointment info to the UI.
            loadAppointment(appointment, customer);
        }
    }
    
}
