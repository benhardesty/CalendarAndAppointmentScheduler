/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.interfaces.AlertInterface;
import benhardestyappointmentscheduler.Main;
import benhardestyappointmentscheduler.database.DAO;
import benhardestyappointmentscheduler.model.City;
import benhardestyappointmentscheduler.model.Country;
import benhardestyappointmentscheduler.model.Customer;
import benhardestyappointmentscheduler.util.AlertUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class AddOrEditCustomerController implements Initializable {
    
    private Main main;
    private Stage stage;
    private AnchorPane rootPane;
    private String addOrUpdate;
    private Customer customer;
    private int userId;
    private ArrayList<City> cities;
    private City selectedCity;
    private ArrayList<Country> countries;
    private Country selectedCountry;
    private AlertInterface errorAlert;
    
    // Booleans to indicate a new city or country is being added.
    private boolean newCity;
    private boolean newCountry;
    
    // Boolean to indicate whether a new customer was created.
    private boolean customerCreatedOrUpdated = false;
    
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField address2TextField;
    @FXML
    private Button newCityButton;
    @FXML
    private ChoiceBox cityChoiceBox;
    @FXML
    private TextField cityTextField;
    @FXML
    private Button newCountryButton;
    @FXML
    private ChoiceBox countryChoiceBox;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private Button addUpdateButton;
    
    /**
     * Allow the user to create a new city if their city is not listed.
     */
    @FXML
    private void handleNewCity() {
        newCity = true;
        GridPane gridPane = (GridPane) rootPane.getChildren().get(0);
        gridPane.getChildren().remove(newCityButton);
        
        // Replace the city choice box with a text field.
        gridPane.getChildren().remove(cityChoiceBox);
        gridPane.add(cityTextField, 1, 3);
        
        // Replace the country textfield with a choicebox.
        gridPane.getChildren().remove(countryTextField);
        countryChoiceBox.setPrefWidth(1000.0);
        gridPane.add(countryChoiceBox, 1, 4);
        
        // Get countries from the db and add to the choicebox.
        countries = DAO.getCountries();
        for (Country country : countries) {
            countryChoiceBox.getItems().add(country.getCountry());
        }
        
        // Add new country button. Lambda reduces code and increases readability.
        newCountryButton.setOnAction((ActionEvent e) -> {
            handleNewCountry();
        });
        
        gridPane.add(newCountryButton, 2, 4);
    }
    
    /**
     * Allow the user to create a new country if their country is not listed.
     */
    @FXML
    private void handleNewCountry() {
        newCountry = true;
        GridPane gridPane = (GridPane) rootPane.getChildren().get(0);
        
        // Remove new country button.
        gridPane.getChildren().remove(newCountryButton);
        
        // Replace the country choicebox with a text field.
        gridPane.getChildren().remove(countryChoiceBox);
        gridPane.add(countryTextField, 1, 4);
        countryTextField.setEditable(true);
        countryTextField.clear();
    }
    
    @FXML
    private void handleAddOrUpdate() {
        
        // If form data is valid.
        try {
            
            // Throws an exception if form input is not valid.
            validate();
            
            // If the user clicked 'Create new customer' to get to the current 
            // view, create a new customer.
            if (addOrUpdate.equals("add")) {
                addCustomer();
            
            // Else, update the customer selected from the previous view.
            } else {
                updateCustomer();
            }
        } catch (Exception e){
            
            // Display an alert to the user.
            AlertUtil.showErrorAlert(stage, "Error", "Invalid Input", e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        stage.close();
    }
    
    /**
     * Create a new customer.
     */
    private void addCustomer() {
        
        // Create a new address.
        Integer addressId = addAddress();
        
        // If creating the address failed, alert the user an error occurred.
        if (addressId == null) {
            errorAlert.displayAlert("An error occured.");
            return;
        }
        
        String customerName = nameTextField.getText();
        int active = 1;
        
        // Add the customer to the database.
        Integer customerId = DAO.createCustomer(customerName, addressId, 
                active, userId);
        
        // If the customer was successfully created, close the stage.
        if (customerId != null) {
            AlertUtil.showConfirmationAlert(stage, "Success", "Customer created!");
            customerCreatedOrUpdated = true;
            stage.close();
        } else {
            errorAlert.displayAlert("An error occured.");
        }
    }
    
    /**
     * Add a new address to the database.
     * 
     * @return Integer
     */
    private Integer addAddress() {
        Integer countryId = null;
        Integer cityId = null;
        
        // If a new country is being created, add the country to the database.
        if (newCountry) {
            countryId = DAO.createCountry(countryTextField.getText(), userId);
            if (countryId == null) {
                return null;
            }
        // Else, get the Id of the selected country.
        } else {
            countryId = selectedCountry.getCountryId();
        }
        
        // If a new city is being created, add the city to the database.
        if (newCity) {
            cityId = DAO.createCity(cityTextField.getText(), countryId, userId);
            if (cityId == null) {
                return null;
            }
        // Else, get the Id of the selected city.
        } else {
            cityId = selectedCity.getCityId();
        }
            
        String address = addressTextField.getText();
        String address2 = address2TextField.getText();
        String postalCode = postalCodeTextField.getText().replace("-", "");
        String phoneNumber = phoneNumberTextField.getText().replace("-", "");
        
        // Return the addressId of the newly created address; return null if unsuccessful.
        return DAO.createAddress(address, address2, cityId,
                postalCode, phoneNumber, userId);
    }
    
    /**
     * Update a customer.
     */
    private void updateCustomer() {
        
        boolean newAddress = false;
        
        // Check if the address has been changed.
        if (!addressTextField.getText().equals(customer.getAddress().getAddress())) {
            newAddress = true;
        }
        if (!address2TextField.getText().equals(customer.getAddress().getAddress2())) {
            newAddress = true;
        }
        if (newCity) {
            newAddress = true;
        } else {
            if (!cities.get(cityChoiceBox.getSelectionModel().getSelectedIndex()).
                    getCity().equals(customer.getAddress().getCity().getCity())) {
                newAddress = true;
            }
        }
        if (!postalCodeTextField.getText().equals(customer.getAddress().getPostalCode())) {
            newAddress = true;
        }
        if (!phoneNumberTextField.getText().equals(customer.getAddress().getPhone())) {
            newAddress = true;
        }
        
        Integer addressId = customer.getAddressId();
        
        //  If the address changed, create a new address.
        if (newAddress) {
            addressId = addAddress();
            
            // If creating the address failed, alert the user that an error occurred.
            if (addressId == null) {
                errorAlert.displayAlert("An error occured.");
                return;
            }
        }
        
        String customerName = nameTextField.getText();
        
        // Update the customer in the database.
        boolean updated = DAO.updateCustomer(customer.getCustomerId(), customerName, addressId, userId);
        
        // If the customer was successfully updated, close the stage.
        if (updated) {
            AlertUtil.showConfirmationAlert(stage, "Success", "Customer updated!");
            customerCreatedOrUpdated = true;
            stage.close();
        
        // Else, alert the user that an error occurred.
        } else {
            errorAlert.displayAlert("An error occured.");
        }
    }
    
    /**
     * Update the country based on the selected city.
     */
    private void handleSelectCity() {
        
        // Index of the city selected.
        int index = cityChoiceBox.getSelectionModel().getSelectedIndex();
        
        // Get the city from the cities array list.
        selectedCity = cities.get(index);
        
        // Get the country for the selected city.
        selectedCountry = DAO.getCountry(cities.get(index).getCountryId());
        
        // Populate the country field with the country that matches the selected city.
        if (selectedCountry != null) {
            countryTextField.setText(selectedCountry.getCountry());
        }
    }
    
    /**
     * Fill the view with the data from the customer object. This will be called
     * when a customer was selected to edit from the previous view.
     */
    private void loadCustomerData() {
        
        nameTextField.setText(customer.getCustomerName());
        addressTextField.setText(customer.getAddress().getAddress());
        address2TextField.setText(customer.getAddress().getAddress2());
        countryTextField.setText(customer.getAddress().getCity().getCountry().getCountry());
        postalCodeTextField.setText(customer.getAddress().getPostalCode());
        phoneNumberTextField.setText(customer.getAddress().getPhone());
        
        int cityIndex = 0;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getCityId() == customer.getAddress().getCityId()) {
                cityIndex = i;
            }
        }
        cityChoiceBox.getSelectionModel().select(cityIndex);
    }
    
    /**
     * Validate the create/update customer form.
     * 
     * @return boolean
     */
    private boolean validate() throws Exception {
        boolean valid = true;
        String errorMessage = "";
        
        // Check the name field.
        if (nameTextField.getText().equals("") || nameTextField.getText().isEmpty()) {
            valid = false;
            errorMessage += "The name field is required.\n";
        } else {
            if (nameTextField.getText().length() > 45) {
                valid = false;
                errorMessage += "The name field cannot be longer than 45 characters.\n";
            }
        }
        
        // Check the address field.
        if (addressTextField.getText().equals("") || addressTextField.getText().isEmpty()) {
            valid = false;
            errorMessage += "The address field is required.\n";
        } else {
            if (addressTextField.getText().length() > 50) {
                valid = false;
                errorMessage += "The address field cannot be longer than 50 characters.\n";
            }
        }
        
        if (address2TextField.getText().length() > 50) {
                valid = false;
                errorMessage += "The address2 field cannot be longer than 50 characters.\n";
            }

        // If a new city is being created.
        if (newCity) {
            
            // Check the city text field.
            if (cityTextField.getText().equals("") || cityTextField.getText().isEmpty()) {
                valid = false;
                errorMessage += "The city field is required.\n";
            } else {
                if (cityTextField.getText().length() > 50) {
                    valid = false;
                    errorMessage += "The city field cannot be longer than 50 characters.\n";
                }
            }
            
            // If a new country is being created.
            if (newCountry) {
                if (countryTextField.getText().equals("") || countryTextField.getText().isEmpty()) {
                    valid = false;
                    errorMessage += "The country field is required.\n";
                } else {
                    if (countryTextField.getText().length() > 50) {
                        errorMessage += "The country field cannot be longer than 50 characters.\n";
                    }
                }
            } else {
                if (countryChoiceBox.getSelectionModel().isEmpty()) {
                    valid = false;
                    errorMessage += "The country field is required.\n";
                }
            }
            
        // If a previous city is being selected.
        } else {
            if (cityChoiceBox.getSelectionModel().isEmpty()) {
                valid = false;
                errorMessage += "The city field is required.\n";
            }
            if (countryTextField.getText().equals("") || countryTextField.getText().isEmpty()) {
                valid = false;
                errorMessage += "The country field is required.\n";
            }
        }
        
        // Check the postal code field is filled in.
        if (postalCodeTextField.getText().isEmpty() || postalCodeTextField.getText().equals("")) {
            valid = false;
            errorMessage += "The postal code field is requried.\n";
        } else {
            
            // Check that the postal code is a number.
            String postalCode = postalCodeTextField.getText().replace("-", "");
            if (postalCode.length() > 10) {
                valid = false;
                errorMessage += "Postal code cannot be more than 10 digits.\n";
            }
            try {
                Long postal = Long.valueOf(postalCode);
            } catch (NumberFormatException e) {
                valid = false;
                errorMessage += "The postal code field must be a number.\n";
            }
        }
        
        // Check the phone number field.
        if (phoneNumberTextField.getText().equals("") || phoneNumberTextField.getText().isEmpty()) {
            valid = false;
            errorMessage += "The phone number field is required.\n";
        } else {
            
            // Check that the phone number is a number.
            String phoneNumber = phoneNumberTextField.getText().replace("-", "");
            if (phoneNumber.length() > 11) {
                valid = false;
                errorMessage += "Phone number cannot be more than 11 digits.\n";
            } else {
                try {
                    Long phone = Long.valueOf(phoneNumber);
                } catch (NumberFormatException e) {
                    valid = false;
                    errorMessage += "The phone number field must be a number.\n";
                }
            }
        }
        
        // If the input is not valid, display an alert to the user.
        if (!valid) {
            throw new Exception(errorMessage);
        }
        
        return valid;
    }
    
    /**
     * Return whether a customer was created or updated.
     * @return boolean
     */
    public boolean getCustomerCreatedOrUpdated() {
        return customerCreatedOrUpdated;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedCity = null;
        selectedCountry = null;
        newCity = false;
        newCountry = false;
        cityTextField = new TextField();
        countryChoiceBox = new ChoiceBox();
        newCountryButton = new Button("New");
        
        // Get available cities from the database.
        cities = DAO.getCities();
        
        // Lambda expression to reduce repetition code since the title and header
        // of the alerts in this controller are the same.
        errorAlert = s -> {
            AlertUtil.showErrorAlert(stage, "Error", "Error", s);
        };
    }    
    
    /**
     * Called by the main application to give this controller access to the
     * stage and the main application.
     * 
     * @param main
     * @param stage
     * @param rootPane
     * @param addOrUpdate
     * @param customer
     * @param userId 
     */
    public void setStage(Main main, Stage stage, AnchorPane rootPane, 
            String addOrUpdate, Customer customer, int userId) {
        
        this.main = main;
        this.stage = stage;
        this.rootPane = rootPane;
        this.customer = customer;
        this.addOrUpdate = addOrUpdate;
        this.userId = userId;
        
        // Populate the city choicebox with the list of cities retrieved from
        // the database in the initialize method. Lambda reduces length of code
        // and increases readability.
        cities.forEach((city) -> {
            cityChoiceBox.getItems().add(city.getCity());
        });
        
        // Set a listner on the city choicebox to update the country text field
        // with the associated country.
        cityChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                handleSelectCity();
            }
        });
        
        // Update UI based on whether the view is being used to create a new 
        // customer or update an existing one.
        if (addOrUpdate.equals("add")) {
            addUpdateButton.setText("Create");
        } else {
            addUpdateButton.setText("Update");
            loadCustomerData();
        }
    }
}
