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
 * Class to represent a city.
 *
 * @author Ben Hardesty
 */
public class City {
    
    private final IntegerProperty cityId;
    private final StringProperty city;
    private final IntegerProperty countryId;
    private Country country;
    
    /**
     * Public constructor to initialize a City object without an associated 
     * country object.
     * 
     * @param cityId
     * @param city
     * @param countryId 
     */
    public City(int cityId, String city, int countryId) {
        this.cityId = new SimpleIntegerProperty(cityId);
        this.city = new SimpleStringProperty(city);
        this.countryId = new SimpleIntegerProperty(countryId);
        this.country = null;
    }
    
    /**
     * Public constructor to initialize a City object with an associated
     * country object.
     * 
     * @param cityId
     * @param city
     * @param countryId
     * @param country 
     */
    public City(int cityId, String city, int countryId, String country) {
        this.cityId = new SimpleIntegerProperty(cityId);
        this.city = new SimpleStringProperty(city);
        this.countryId = new SimpleIntegerProperty(countryId);
        this.country = new Country(countryId, country);
    }
    
    // Getters and setters.
    
    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }
    
    public int getCityId() {
        return cityId.get();
    }
    
    public IntegerProperty cityIdProperty() {
        return cityId;
    }
    
    public void setCity(String city) {
        this.city.set(city);
    }
    
    public String getCity() {
        return city.get();
    }
    
    public StringProperty cityProperty() {
        return city;
    }
    
    public void setCountryId(int countryId) {
        this.countryId.set(countryId);
    }
    
    public int getCountryId() {
        return countryId.get();
    }
    
    public IntegerProperty countryIdProperty() {
        return countryId;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public Country getCountry() {
        return country;
    }
}
