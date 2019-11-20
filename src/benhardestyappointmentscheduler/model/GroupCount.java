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
 * Class to assist with reporting.
 * 
 * @author Ben Hardesty
 */
public class GroupCount {
    private final StringProperty group;
    private final IntegerProperty count;
    
    /**
     * Public constructor.
     * 
     * @param group
     * @param count 
     */
    public GroupCount(String group, int count) {
        this.group = new SimpleStringProperty(group);
        this.count = new SimpleIntegerProperty(count);
    }
    
    
    // Getters and setters.
    
    public void setGroup(String group) {
        this.group.set(group);
    }
    
    public String getGroup() {
        return group.get();
    }
        
    public StringProperty groupProperty() {
        return group;
    }
    
    public void setCount(int count) {
        this.count.set(count);
    }
    
    public int getCount() {
        return count.get();
    }
        
    public IntegerProperty countProperty() {
        return count;
    }
}
