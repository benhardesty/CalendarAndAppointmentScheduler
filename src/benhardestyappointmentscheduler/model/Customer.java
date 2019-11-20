/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class to represent a customer.
 *
 * @author Ben Hardesty
 */
public class Customer {
    
    private final IntegerProperty customerId;
    private final StringProperty customerName;
    private final IntegerProperty addressId;
    private final IntegerProperty active;
    private Address address;
    private final StringProperty addressProperty;
    
    /**
     * Public constructor to initialize a Customer object without an associated
     * address object.
     * 
     * @param customerId
     * @param customerName
     * @param active
     * @param addressId 
     */
    public Customer(int customerId, String customerName, int active, int addressId) {
        
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.addressId = new SimpleIntegerProperty(addressId);
        this.active = new SimpleIntegerProperty(active);
        this.address = null;
        this.addressProperty = null;
    }
    
    /**
     * Public constructor to initialize a Customer object with as associated
     * address object.
     * 
     * @param customerId
     * @param customerName
     * @param active
     * @param addressId
     * @param address
     * @param address2
     * @param cityId
     * @param postalCode
     * @param phone
     * @param city
     * @param countryId
     * @param country 
     */
    public Customer(int customerId, String customerName, int active, int addressId, 
            String address, String address2, int cityId, String postalCode, 
            String phone, String city, int countryId, String country) {
        
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.addressId = new SimpleIntegerProperty(addressId);
        this.active = new SimpleIntegerProperty(active);
        this.address = new Address(addressId, address, address2, postalCode, phone, cityId, 
        city, countryId, country);
        this.addressProperty = new SimpleStringProperty(this.address.getAddress() + 
                " " + this.address.getAddress2() + " " + this.address.getCity().getCity() +
                " " + this.address.getCity().getCountry().getCountry());
    }
    
    
    // Getters and setters.
    
    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }
    
    public int getCustomerId() {
        return customerId.get();
    }
    
    public IntegerProperty customerIdProperty() {
        return customerId;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }
    
    public String getCustomerName() {
        return customerName.get();
    }
    
    public StringProperty customerNameProperty() {
        return customerName;
    }
    
    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }
    
    public int getAddressId() {
        return addressId.get();
    }
    
    public IntegerProperty addressIdProperty() {
        return addressId;
    }
    
    public void setActive(int active) {
        this.active.set(active);
    }
    
    public int getActive() {
        return active.get();
    }
    
    public IntegerProperty activeProperty() {
        return active;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public StringProperty addressProperty() {
        return addressProperty;
    }
}
