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
 * Class to represent a Country.
 *
 * @author Ben Hardesty
 */
public class Country {
    
    private final IntegerProperty countryId;
    private final StringProperty country;
    
    /**
     * Public constructor to initialize a Country object.
     * 
     * @param countryId
     * @param country 
     */
    public Country(int countryId, String country) {
        this.countryId = new SimpleIntegerProperty(countryId);
        this.country = new SimpleStringProperty(country);
    }
    
    // Getters and setters.

    public void setCountryId(int countryId) {
        this.countryId.set(countryId);
    }
    
    public int getCountryId() {
        return countryId.get();
    }
    
    public IntegerProperty countryIdProperty() {
        return countryId;
    }
    
    public void setCountry(String country) {
        this.country.set(country);
    }
    
    public String getCountry() {
        return country.get();
    }

    public StringProperty getCountryProperty() {
        return country;
    }
    
    
}
