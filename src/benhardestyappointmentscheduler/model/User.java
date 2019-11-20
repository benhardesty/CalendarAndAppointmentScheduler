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
 * Class to represent a user/consultant.
 *
 * @author Ben Hardesty
 */
public class User {
    
    // I would never send a userId to the client side, but since the rubric
    // requires the database functions be implemented on the client side,
    // I am retrieving the userIds.
    private final IntegerProperty userId;
    
    private final StringProperty userName;
    private final IntegerProperty active;
    
    /**
     * Public constructor.
     * 
     * @param userId
     * @param userName
     * @param active 
     */
    public User(int userId, String userName, int active) {
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = new SimpleStringProperty(userName);
        this.active = new SimpleIntegerProperty(active);
    }
    
    // Getters and setters.
    
    public void setUserId(int userId) {
        this.userId.set(userId);
    }
    
    public int getUserId() {
        return userId.get();
    }
    
    public IntegerProperty userIdProperty() {
        return userId;
    }
    
    public void setUserName(String userName) {
        this.userName.set(userName);
    }
    
    public String getUserName() {
        return userName.get();
    }
    
    public StringProperty userNameProperty() {
        return userName;
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
}
