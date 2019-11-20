/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.model;

import benhardestyappointmentscheduler.util.DateTimeUtil;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class to represent an appointment.
 *
 * @author Ben Hardesty
 */
public class Appointment {
    
    private final IntegerProperty appointmentId;
    private final IntegerProperty customerId;
    private final StringProperty customerName;
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty location;
    private final StringProperty contact;
    private Date start;
    private Date end;
    private final StringProperty type;
    private final StringProperty startProperty;
    private final StringProperty endProperty;
    
    // I would never send a userId to the client side, but since the rubric
    // requires the database functions be implemented on the client side,
    // I am retrieving the userIds.
    private final IntegerProperty userId;
    
    /**
     * Public constructor.
     * Appointment requires a customer name, which is not part of the appointment
     * table in the database. Requiring customer name in the appointment class
     * requires 1 sql join to be done up front in retrieving appointment data rather
     * than doing many sql joins afterwards. Since it is forseeable that the 
     * name of the customer for which one would have an appointment would be 
     * pertinent to anything related to that appointment, it will be more efficient
     * to pull this data up front.
     * 
     * @param appointmentId
     * @param customerId
     * @param customerName
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param start
     * @param end
     * @param type
     * @param userId 
     */
    public Appointment(int appointmentId, int customerId, String customerName, 
            String title, String description, String location, String contact, 
            Date start, Date end, String type, int userId) {
        
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.start = start;
        this.end = end;
        this.type = new SimpleStringProperty(type);
        this.userId = new SimpleIntegerProperty(userId);
        this.startProperty = new SimpleStringProperty(String.valueOf(start.getTime()));
        this.endProperty = new SimpleStringProperty(String.valueOf(end.getTime()));
    }
    
    // Getters and setters
    
    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }
    
    public int getAppointmentId() {
        return appointmentId.get();
    }
    
    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }
    
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
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public StringProperty titleProperty() {
        return title;
    }
    
    public void setDescription(String description) {
        this.description.set(description);
    }
    
    public String getDescription() {
        return description.get();
    }
    
    public StringProperty descriptionProperty() {
        return description;
    }
    
    public void setLocation(String location) {
        this.location.set(location);
    }
    
    public String getLocation() {
        return location.get();
    }
    
    public StringProperty locationProperty() {
        return location;
    }
    
    public void setContact(String contact) {
        this.contact.set(contact);
    }
    
    public String getContact() {
        return contact.get();
    }
    
    public StringProperty contactProperty() {
        return contact;
    }
    
    public void setStart(Date start) {
        this.start = start;
    }
    
    public Date getStart() {
        return start;
    }
    
    public void setEnd(Date end) {
        this.end = end;
    }
    
    public Date getEnd() {
        return end;
    }
    
    public void setType(String type) {
        this.type.set(type);
    }
    
    public String getType() {
        return type.get();
    }
    
    public StringProperty typeProperty() {
        return type;
    }
    
    public void setUserId(int userId) {
        this.userId.set(userId);
    }
    
    public int getUserId() {
        return userId.get();
    }
    
    public IntegerProperty userIdProperty() {
        return userId;
    }
    
    // Provided for use with table views.
    public StringProperty startProperty() {
        String date = DateTimeUtil.convert_date_object_to_date_string(start);
        String time = DateTimeUtil.retreive_time_of_day_from_date(start);
        
        startProperty.set(date + " " + time);
        return startProperty;
    }
    
    // Provided for use with table views.
    public StringProperty endProperty() {
        
        String date = DateTimeUtil.convert_date_object_to_date_string(end);
        String time = DateTimeUtil.retreive_time_of_day_from_date(end);
        
        endProperty.set(date + " " + time);
        return endProperty;
    }
    
}
