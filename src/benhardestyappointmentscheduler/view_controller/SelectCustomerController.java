/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.Main;
import benhardestyappointmentscheduler.database.DAO;
import benhardestyappointmentscheduler.model.Customer;
import benhardestyappointmentscheduler.util.AlertUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class SelectCustomerController implements Initializable {
    
    private Main main;
    private Stage stage;
    private int userId;
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private Customer selectedCustomer = null;
    
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearSearchButton;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    
    @FXML
    private void handleNewCustomer() {
        
        // Load the addOrEditCustomer layout. If a customer is added,
        // reload the customer table to capture the new customer in the table.
        if(main.loadAddOrEditCustomerLayout(null)) {
            customers.clear();
            loadCustomerTable();
        }
    }
    
    @FXML
    private void handleSelectCustomer() {
        
        // Check that a customer was selected.
        if (customerTableView.getSelectionModel().getSelectedItem() == null) {
            AlertUtil.showInformationAlert(stage, "Error", "Error", 
                    "You must select a customer before pressing select.");
            return;
        }
        
        // Set the selected customer and close the stage.
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        stage.close();
    }
    
    @FXML
    private void handleEditCustomer() {
        
        // Check that a customer was selected.
        if (customerTableView.getSelectionModel().getSelectedItem() == null) {
            AlertUtil.showInformationAlert(stage, "Error", "Error", 
                    "You must select a customer before pressing edit.");
            return;
        }
        
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        
        // Load the addOrEditCustomer layout. If the customer was updated, 
        // reload the customer table to capture the update in the table.
        if(main.loadAddOrEditCustomerLayout(customer)) {
            customers.clear();
            loadCustomerTable();
        }
        
    }
    
    @FXML
    private void handleDeleteCustomer() {
        
        // Check that a customer was selected.
        if (customerTableView.getSelectionModel().getSelectedItem() == null) {
            AlertUtil.showInformationAlert(stage, "Error", "Error", 
                    "You must select a customer before pressing delete.");
            return;
        }
        
        // Confirm that the user meant to delete the cusotmer.
        Optional<ButtonType> result = AlertUtil.showConfirmationAlert(stage, 
                "Confirmation", "Click 'OK' to confirm you wish to delete\n"
                        + "the customer.");
        
        // If user confirms they want to delete the custoemr.
        if (result.get() == ButtonType.OK) {
            
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            
            // Set the active field in the customer table to 0.
            if (DAO.deleteCustomer(customer.getCustomerId(), userId)) {
                AlertUtil.showInformationAlert(stage, "Success", "Success", "Customer deleted.");
                customers.clear();
                loadCustomerTable();
            } else {
                AlertUtil.showErrorAlert(stage, "Error", "Error", "There was an error.");
            }
        }
    }
    
    @FXML
    private void handleCancel() {
        stage.close();
    }
    
    /**
     * Return the customer that was selected; null if no customer selected.
     * 
     * @return 
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }
    
    /**
     * Load customers from the database into the customerTableView.
     */
    private void loadCustomerTable() {
        ArrayList<Customer> customerList = DAO.getCustomers();
        for (Customer customer : customerList) {
            customers.add(customer);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Initialize the customer table.
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        customerAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
    }    
    
    /**
     * Called by the main application to give this controller access to the
     * stage and the main application.
     * 
     * @param main
     * @param stage
     * @param userId 
     */
    public void setStage(Main main, Stage stage, int userId) {
        this.main = main;
        this.stage = stage;
        this.userId = userId;
        
        // Load customers from the database.
        loadCustomerTable();
        
        // Add customers to the table view.
        FilteredList<Customer> filteredCustomers = new FilteredList<>(customers, p -> true);
        SortedList<Customer> sortedCustomers = new SortedList<>(filteredCustomers);
        sortedCustomers.comparatorProperty().bind(customerTableView.comparatorProperty());
        customerTableView.setItems(sortedCustomers);
        
        // Update customer table filter predicate when search button is clicked.
        searchButton.setOnAction(action -> {
            filteredCustomers.setPredicate(customer -> {
                return customer.getCustomerName().toLowerCase().contains(searchTextField.getText().toLowerCase());
            });
        });
        
        // Clear customer table filter when clear button is clicked.
        clearSearchButton.setOnAction(action -> {
            searchTextField.setText("");
            filteredCustomers.setPredicate(customer -> true);
        });
    }
    
}
