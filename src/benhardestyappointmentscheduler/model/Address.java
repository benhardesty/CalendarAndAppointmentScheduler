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
 * Class to represent an Address.
 *
 * @author Ben Hardesty
 */
public class Address {
    
    private final IntegerProperty addressId;
    private final StringProperty address;
    private final StringProperty address2;
    private final IntegerProperty cityId;
    private final StringProperty postalCode;
    private final StringProperty phone;
    private City city;
    
    /**
     * Constructor to initialize an address without a city object.
     * 
     * @param addressId
     * @param address
     * @param address2
     * @param cityId
     * @param postalCode
     * @param phone 
     */
    public Address(int addressId, String address, String address2, int cityId,
            String postalCode, String phone) {
        
        this.addressId = new SimpleIntegerProperty(addressId);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.cityId = new SimpleIntegerProperty(cityId);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.phone = new SimpleStringProperty(phone);
        this.city = null;
    }
    
    /**
     * Constructor to initialize an address with a city object.
     * 
     * @param addressId
     * @param address
     * @param address2
     * @param postalCode
     * @param phone
     * @param cityId
     * @param city
     * @param countryId
     * @param country 
     */
    public Address(int addressId, String address, String address2,
            String postalCode, String phone, int cityId, String city, 
            int countryId, String country) {
        
        this.addressId = new SimpleIntegerProperty(addressId);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.cityId = new SimpleIntegerProperty(cityId);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.phone = new SimpleStringProperty(phone);
        this.city = new City(cityId, city, countryId, country);
    }
    
    // Setters and getters.
    
    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }
    
    public int getAddressId() {
        return addressId.get();
    }
    
    public IntegerProperty addressIdProperty() {
        return addressId;
    }
    
    public void setAddress(String address) {
        this.address.set(address);
    }
    
    public String getAddress() {
        return address.get();
    }
    
    public StringProperty addressProperty() {
        return address;
    }
    
    public void setAddress2(String address2) {
        this.address2.set(address2);
    }
    
    public String getAddress2() {
        return address2.get();
    }
    
    public StringProperty address2Property() {
        return address2;
    }
    
    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }
    
    public int getCityId() {
        return cityId.get();
    }
    
    public IntegerProperty cityIdProperty() {
        return cityId;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }
    
    public String getPostalCode() {
        return postalCode.get();
    }
    
    public StringProperty postalCodeProperty() {
        return postalCode;
    }
    
    public void setPhone(String phone) {
        this.phone.set(phone);
    }
    
    public String getPhone() {
        return phone.get();
    }
    
    public StringProperty phoneProperty() {
        return phone;
    }
    
    public City getCity() {
        return city;
    }
}
