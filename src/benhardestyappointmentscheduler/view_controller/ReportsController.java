/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.Main;
import benhardestyappointmentscheduler.interfaces.ReportsInterface;
import benhardestyappointmentscheduler.database.DAO;
import benhardestyappointmentscheduler.model.Appointment;
import benhardestyappointmentscheduler.model.GroupCount;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class ReportsController implements Initializable {
    
    Main main;
    Stage stage;
    int userId;
    AnchorPane rootPane;
    ReportsInterface reports;
    
    @FXML
    private ChoiceBox reportChoiceBox;
    
    @FXML
    private void handleRun() {
        
        // Get the chosen report.
        int report = reportChoiceBox.getSelectionModel().getSelectedIndex();
        
        // Load the report selected.
        switch (report) {
            case 0:
                appointmentsByMonth();
                break;
            case 1:
                consultantSchedules();
                break;
            case 2:
                consultantCustomers();
                break;
            default:
                break;
            }

    }
    
    /**
     * Populate the table view with the number of appointments aggregated by month.
     */
    private void appointmentsByMonth() {
        
        // Retreive the acount of appointments by month from the database.
        ObservableList<GroupCount> appointmentsByMonth = DAO.getAppointmentTypeByMonth();
        
        // Create the table view and table columns.
        TableView<GroupCount> tableView = new TableView<>();
        TableColumn<GroupCount, String> monthColumn = new TableColumn<>("Month");
        TableColumn<GroupCount, Integer> countColumn = new TableColumn<>("# of Appointment Types");
        
        // Add items to the table.
        tableView.setItems(appointmentsByMonth);
        
        // Bind data to the table columns.
        monthColumn.setCellValueFactory(cellData -> cellData.getValue().groupProperty());
        countColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());
        
        // Add the table columns to the table.
        tableView.getColumns().add(monthColumn);
        tableView.getColumns().add(countColumn);
        
        // Display report.
        reports.displayReport(tableView);
        
    }
    
    /**
     * Display the schedule for each consultant in a table view.
     */
    private void consultantSchedules() {
        
        // Retreive all appointments from the database.
        ObservableList<Appointment> allAppointments = DAO.getAllAppointments();
        
        // Create the table view and table columns.
        TableView<Appointment> tableView = new TableView<>();
        TableColumn<Appointment, String> userIdColumn = new TableColumn<>("Consultant ID");
        TableColumn<Appointment, String> startColumn = new TableColumn<>("Start");
        TableColumn<Appointment, String> endColumn = new TableColumn<>("End");
        TableColumn<Appointment, String> titleColumn = new TableColumn<>("Title");
        TableColumn<Appointment, String> typeColumn = new TableColumn<>("Type");
        TableColumn<Appointment, String> locationColumn = new TableColumn<>("Location");
        TableColumn<Appointment, String> customerNameColumn = new TableColumn<>("Customer Name");
        
        // Add the table columns to the table.
        tableView.getColumns().add(userIdColumn);
        tableView.getColumns().add(customerNameColumn);
        tableView.getColumns().add(titleColumn);
        tableView.getColumns().add(typeColumn);
        tableView.getColumns().add(locationColumn);
        tableView.getColumns().add(startColumn);
        tableView.getColumns().add(endColumn);
        
        // Bind data to the table columns.
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asString());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        
        // Add items to the table.
        tableView.setItems(allAppointments);
        
        // Display report.
        reports.displayReport(tableView);
    }
    
    /**
     * Display the number of customers each consultant has in a table view. The
     * number of customers is based on customers that the consultant has
     * scheduled a meeting with.
     */
    private void consultantCustomers() {
        
        // Retreive the number of customers for each consultant based on 
        // appointment records.
        ObservableList<GroupCount> customersByConsultant = DAO.getCustomersByConsultant();
        
        // Create the table view and table columns.
        TableView<GroupCount> tableView = new TableView<>();
        TableColumn<GroupCount, String> consultantColumn = new TableColumn<>("Consultant ID");
        TableColumn<GroupCount, Integer> customerColumn = new TableColumn<>("# of Customers");
        
        // Add the table columns to the table.
        tableView.getColumns().add(consultantColumn);
        tableView.getColumns().add(customerColumn);
        
        // Bind data to the table columns.
        consultantColumn.setCellValueFactory(cellData -> cellData.getValue().groupProperty());
        customerColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());
        
        // Add items to the table.
        tableView.setItems(customersByConsultant);
        
        // Display report.
        reports.displayReport(tableView);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Add report options to the report choice box.
        reportChoiceBox.getItems().add(("Appointment types by month"));
        reportChoiceBox.getItems().add(("Schedule for each consultant"));
        reportChoiceBox.getItems().add(("Customers per consultant"));
        
        // Lambda expression to generate reports reduces repeated code.
        reports = tableView -> {
            // Add layout constraints.
            AnchorPane.setLeftAnchor(tableView, 10.0);
            AnchorPane.setRightAnchor(tableView, 10.0);
            AnchorPane.setBottomAnchor(tableView, 10.0);
            AnchorPane.setTopAnchor(tableView, 46.0);

            // If a table is already present in the view, remove it.
            if (rootPane.getChildren().size() > 1) {
                rootPane.getChildren().remove(1);
            }

            // Add the table to the view.
            rootPane.getChildren().add(tableView);
        };
        
    }    
    
    /**
     * Give this controller access to the main application, it's stage, the
     * userId for the user who is logged in, and the root pane for the layout.
     * 
     * @param main
     * @param stage
     * @param userId
     * @param pane 
     */
    public void setStage(Main main, Stage stage, int userId, AnchorPane pane) {
        this.main = main;
        this.stage = stage;
        this.userId = userId;
        this.rootPane = pane;
    }
    
}
