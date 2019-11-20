/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.view_controller;

import benhardestyappointmentscheduler.Main;
import benhardestyappointmentscheduler.database.DAO;
import benhardestyappointmentscheduler.model.Appointment;
import benhardestyappointmentscheduler.model.Customer;
import benhardestyappointmentscheduler.util.AlertUtil;
import benhardestyappointmentscheduler.util.DateTimeUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class CalendarController implements Initializable {
    
    private Main main;
    private Stage stage;
    private int userId;
    private AnchorPane rootPane;
    private String view = "Month";
    private Calendar calendar;
    
    @FXML
    private Button toggleViewButton;
    @FXML
    private Label monthYear;
    @FXML
    private Label location;
    
    @FXML
    private void handleNewAppointment() {
        if (main.loadAppointmentLayout(null, null)) {
            loadCalendar();
        }
    }
    
    @FXML
    private void handleReports() {
        main.loadReportsLayout();
    }
    
    @FXML
    private void handleNext() {
        if (view.equals("Month")) {
            nextMonth();
        } else {
            nextWeek();
        }
    }
    
    /**
     * Move the calendar ahead by 1 month.
     */
    private void nextMonth() {
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        
        if (month == 11) {
            month = 0;
            year++;
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
        } else {
            month++;
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, month);
        }
        loadCalendar();
    }
    
    /**
     * Move the calendar ahead by 1 week.
     */
    private void nextWeek() {
        // Set the current day to the first day of the week if it is not already.
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, -(dayOfWeek-1));
        
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        loadCalendar();
    }
    
    @FXML
    private void handlePrevious() {
        if (view.equals("Month")) {
            previousMonth();
        } else {
            previousWeek();
        }
    }
    
    /**
     * Move the calendar back by 1 month.
     */
    private void previousMonth() {
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        
        if (month == 0) {
            month = 11;
            year--;
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
        } else {
            month--;
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, month);
        }
        loadCalendar();
    }
    
    /**
     * Move the calendar back by 1 week.
     */
    private void previousWeek() {
        // Set the current day to the first day of the week if it is not already.
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, -(dayOfWeek-1));
        
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        loadCalendar();
    }
    
    /**
     * Toggle between month and week views.
     */
    @FXML
    private void handleToggleView() {
        if (view.equals("Month")) {
            view = "Week";
            toggleViewButton.setText("Month");
            calendar = Calendar.getInstance();
            loadWeek();
        } else {
            view = "Month";
            toggleViewButton.setText("Week");
            calendar = Calendar.getInstance();
            loadMonth();
        }
    }
    
    /**
     * Load the appointment layout when an appointment on the calendar is 
     * double clicked.
     * 
     * @param appointment 
     */
    private void viewAppointment(Appointment appointment) {
        
        // Get the custoemr for the appointment selected.
        Customer customer = DAO.getCustomer(appointment.getCustomerId());
        
        // Load the appointment layout. If the appointment is changed, reload
        // the calendar so the update is reflected on the calendar.
        if (main.loadAppointmentLayout(appointment, customer)) {
            loadCalendar();
        }
    }
    
    /**
     * Return the month name given an integer. January is 0.
     * 
     * @param calendar
     * @return 
     */
    private String getMonthString(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        
        return "";
    }
    
    /**
     * Create and return an AnchorPane with a label and a ListView inside
     * to represent a day on a calendar.
     * 
     * @param day
     * @param appointments
     * @return AnchorPane.
     */
    private AnchorPane getCalendarDayPane(int day, ArrayList<Appointment> appointments) {
        
        // Create a new AnchorPane object and add css styling to it.
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-border-color: black; -fx-border-insets: 1;");
        
        // Label for day of the month.
        Label label = new Label(String.valueOf(day));
        
        // Set constraints for the label.
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        
        // ListView to hold appointments.
        ListView listView = new ListView();
        
        for (int i = 0; i < appointments.size(); i++) {
            Calendar start = Calendar.getInstance();
            start.setTime(appointments.get(i).getStart());
            
            listView.getItems().add(DateTimeUtil.retreive_time_of_day_from_date(appointments.get(i).getStart()) + 
                    "-" + DateTimeUtil.retreive_time_of_day_from_date(appointments.get(i).getEnd()) + 
                    " | " + appointments.get(i).getCustomerName());
        }
        
        // If an appointment is double clicked, load the appointment into
        // the appointment view.
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    int index = listView.getSelectionModel().getSelectedIndex();
                    
                    if (index > -1) {
                        Appointment appointment = appointments.get(index);
                        viewAppointment(appointment);
                    }
                }
            }
        });
        
        // Set constraints for the ListView.
        AnchorPane.setTopAnchor(listView, 20.0);
        AnchorPane.setRightAnchor(listView, 0.0);
        AnchorPane.setLeftAnchor(listView, 0.0);
        AnchorPane.setBottomAnchor(listView, 0.0);
        
        // Add the label and ListView to the AnchorPane.
        pane.getChildren().add(label);
        pane.getChildren().add(listView);
        
        // Return the AnchorPane.
        return pane;
    }
    
    /**
     * Load the monthly or weekly calendar view and populate it with the 
     * appointments during the time frame.
     */
    private void loadCalendar() {
        if (view.equals("Month")) {
            loadMonth();
        } else {
            loadWeek();
        }
    }
    
    /**
     * Load the month calendar view and the appointments for the month.
     */
    private void loadMonth() {
        
        // Create a GridPane object.
        GridPane gridPane = new GridPane();
        
        // Set the label for the month and year of the selected month.
        monthYear.setText(getMonthString(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR));
        
        // Number of days in the current month.
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Set calendar date to the first of the month.
        calendar.set(Calendar.DATE, 1);
        
        // Get the day of the week for the first day of the month.
        int firstDayOfWeekOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        
        // Create a start date of the first of the month.
        Calendar c = Calendar.getInstance();
        c.setTime(calendar.getTime());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date start = c.getTime();
        
        // Create an end date of the first day of the next month.
        c.set(Calendar.DAY_OF_MONTH, daysInMonth);
        c.set(Calendar.HOUR_OF_DAY, 24);
        Date end = c.getTime();
        
        // Get an array of ArrayLists contains appointments for each day of the current month.
        ArrayList<Appointment>[] appointmentList = DAO.getAppointments(start, end, userId);
        
        // Add day of week labels to the gridpane columns.
        gridPane.add(new Label("Sunday"), 0, 0);
        gridPane.add(new Label("Monday"), 1, 0);
        gridPane.add(new Label("Tuesday"), 2, 0);
        gridPane.add(new Label("Wednesday"), 3, 0);
        gridPane.add(new Label("Thursday"), 4, 0);
        gridPane.add(new Label("Friday"), 5, 0);
        gridPane.add(new Label("Saturday"), 6, 0);
        
        // Add column constraints to the gridpane so that it fills the entire screen.
        ColumnConstraints alwaysGrow = new ColumnConstraints();
        alwaysGrow.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(alwaysGrow,alwaysGrow,alwaysGrow,
                alwaysGrow,alwaysGrow,alwaysGrow,alwaysGrow);
        
        // Add AnchorPanes with a day label and appointment listview to the 
        // gridPane for each day of the month.
        int x = firstDayOfWeekOfMonth - 1;
        int y = 1;
        int day = 1;
        while(day <= daysInMonth) {
            
            // Get an AnchorPane with a label and a listview for the day of the month.
            AnchorPane dayPane = getCalendarDayPane(day, appointmentList[day]);
            
            // Add the AnchorPane to the gridPane.
            gridPane.add(dayPane, x, y);
            
            x++;
            if (x == 7) {
                x = 0;
                y++;
            }
            day++;
        }
        
        // If there is already a GridPane calendar view, remove it. This will
        // be necessary when changing to a new month.
        if (rootPane.getChildren().size() > 2) {
            rootPane.getChildren().remove(2);
        }
        
        // Set layout constraints for the gridPane.
        AnchorPane.setTopAnchor(gridPane, 90.0);
        AnchorPane.setBottomAnchor(gridPane, 10.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        AnchorPane.setLeftAnchor(gridPane, 10.0);
        
        // Add the new gridPane onto the scene.
        rootPane.getChildren().add(2, gridPane);
    }
    
    /**
     * Load the week calendar view and the appointments for the week.
     */
    private void loadWeek() {
        
        // Create a GridPane object.
        GridPane gridPane = new GridPane();
        
        // Set the label for the month and year of the selected month.
        monthYear.setText(getMonthString(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR));
        
        // Create a new calendar instance equal to the time of the global calendar.
        Calendar c = Calendar.getInstance();
        c.setTime(calendar.getTime());
        
        // Set time of day to 0.
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        // Set the current day to the first day of the week if it is not already.
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_MONTH, -(dayOfWeek-1));
        
        Date start = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 7);
        Date end = c.getTime();
        
        // Get an array of ArrayLists contains appointments for each day of the current month.
        ArrayList<Appointment>[] appointmentList = DAO.getAppointments(start, end, userId);
        
        // Add day of week labels to the gridpane columns.
        gridPane.add(new Label("Sunday"), 0, 0);
        gridPane.add(new Label("Monday"), 1, 0);
        gridPane.add(new Label("Tuesday"), 2, 0);
        gridPane.add(new Label("Wednesday"), 3, 0);
        gridPane.add(new Label("Thursday"), 4, 0);
        gridPane.add(new Label("Friday"), 5, 0);
        gridPane.add(new Label("Saturday"), 6, 0);
        
        // Add column constraints to the gridpane so that it fills the entire screen.
        ColumnConstraints alwaysGrow = new ColumnConstraints();
        alwaysGrow.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(alwaysGrow,alwaysGrow,alwaysGrow,
                alwaysGrow,alwaysGrow,alwaysGrow,alwaysGrow);
        
        c.add(Calendar.DAY_OF_MONTH, -7);
        
        for (int x = 0; x < 7; x++) {
            AnchorPane dayPane = getCalendarDayPane(c.get(Calendar.DAY_OF_MONTH), appointmentList[c.get(Calendar.DAY_OF_MONTH)]);
            gridPane.setVgrow(dayPane, Priority.ALWAYS);
            gridPane.add(dayPane, x, 1);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        // If there is already a GridPane calendar view, remove it. This will
        // be necessary when changing to a new month.
        if (rootPane.getChildren().size() > 2) {
            rootPane.getChildren().remove(2);
        }
        
        // Set layout constraints for the gridPane.
        AnchorPane.setTopAnchor(gridPane, 90.0);
        AnchorPane.setBottomAnchor(gridPane, 10.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        AnchorPane.setLeftAnchor(gridPane, 10.0);
        
        // Add the new gridPane onto the scene.
        rootPane.getChildren().add(2, gridPane);
    }
    
    private void checkForUpcomingAppointment() {
        
        // Set start time.
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date start = c.getTime();
        
        // Move time forward 15 minutes and set end time.
        c.add(Calendar.MINUTE, 15);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        Date end = c.getTime();
        
        // Move time back in the offchance that it caused the calendar to
        // go into the next day.
        c.add(Calendar.MINUTE, -15);
        
        // Get an array of ArrayLists containing appointments that start within 
        // the next 15 minutes.
        ArrayList<Appointment>[] appointmentList = DAO.getAppointments(start, end, userId);
        
        // Check the length of the arraylist in the index of the array for the 
        // day of the start time.
        if (appointmentList[c.get(Calendar.DAY_OF_MONTH)].size() > 0) {
            
            // Alert the user than 
            Appointment appointment = appointmentList[c.get(Calendar.DAY_OF_MONTH)].get(0);
            AlertUtil.showInformationAlert(stage, "Upcoming Appointment", 
                    "Upcoming Appointment", "Your appointment with " + 
                    appointment.getCustomerName() + "\nstarts at " + 
                    DateTimeUtil.retreive_time_of_day_from_date(appointment.getStart()));
        
        // Do the same for the index of the end time, in case it is on the next day.
        // This will not be possible for this application since appointments cannot
        // be outside business hours, but will prevent future bugs if the business
        // logic changes.
        } else {
            c.add(Calendar.MINUTE, 15);
            if (appointmentList[c.get(Calendar.DAY_OF_MONTH)].size() > 0) {
                Appointment appointment = appointmentList[c.get(Calendar.DAY_OF_MONTH)].get(0);
                AlertUtil.showInformationAlert(stage, "Upcoming Appointment", 
                        "Upcoming Appointment", "Your appointment with " + 
                        appointment.getCustomerName() + " starts at " + 
                        DateTimeUtil.retreive_time_of_day_from_date(appointment.getStart()));
            }
            c.add(Calendar.MINUTE, -15);
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        calendar = Calendar.getInstance();
    }
    
    /**
     * Called by the main application to give this controller access to the
     * stage and the main application.
     * 
     * @param main
     * @param stage 
     * @param rootPane 
     */
    public void setStage(Main main, Stage stage, AnchorPane rootPane, int userId) {
        this.main = main;
        this.stage = stage;
        this.rootPane = rootPane;
        this.userId = userId;
        
        Locale locale = Locale.getDefault();
        location.setText(locale.getDisplayCountry());
        toggleViewButton.setText("Week View");
        
        // Load the calendar view.
        loadCalendar();
        
        // Check for appointments that start in the next 15 minutes.
        checkForUpcomingAppointment();
    }
    
}
