/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.database;

import benhardestyappointmentscheduler.model.Appointment;
import benhardestyappointmentscheduler.model.City;
import benhardestyappointmentscheduler.model.Country;
import benhardestyappointmentscheduler.model.Customer;
import benhardestyappointmentscheduler.model.GroupCount;
import benhardestyappointmentscheduler.util.DateTimeUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Database access object used to query the database.
 * No one would ever implement a database access object like this from the client
 * side, but it was a requirement in the project rubric. The SQL should be on
 * the server side and a session id should be passed around instead of a userId.
 * 
 * @author Ben Hardesty
 */
public class DAO {
    
    /**
     * Check if a user exists in the database.
     * 
     * @param userName
     * @return 
     */
    private static boolean userAlreadyExists(String userName) {
        
        boolean userExists = false;
        
        // SQL Select statement.
        String sql = "SELECT userName FROM user WHERE userName = ?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind parameters to the statement.
            stmt.setString(1, userName);
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                
                // If rows were returned, a user exists.
                if (rs.next()) {
                    userExists = true;
                }
            }
        } catch (Exception e) {
            userExists = true;
        }
        
        return userExists;
    }
    
    /**
     * Register a user in the database.
     * 
     * @param userName
     * @param password
     * @return 
     */
    public static boolean registerUser(String userName, String password) {
        
        boolean registered = false;
        
        // Change the timezone to the user's timezone.
        TimeZone myTimeZone = TimeZone.getDefault();
        String appTimeZone = "America/Los_Angeles";
        TimeZone.setDefault(TimeZone.getTimeZone(appTimeZone));
        
        // SQL Insert statement
        String sql = "INSERT INTO user set userName=?, password=?, active=?, "
                + "createBy=?, createDate=?, lastUpdate=?, lastUpdateBy=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Check if the userName already exists in the database.
            if (userAlreadyExists(userName)) {
                throw new Exception();
            }
            
            // Bind parameters to the statement
            stmt.setString(1, userName);
            stmt.setString(2, password);
            stmt.setInt(3, 1);
            stmt.setString(4, userName);
            stmt.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setString(7, userName);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // Close the connection.
            conn.close();
            
            // Set the timezone back to the user's timezone.
            TimeZone.setDefault(myTimeZone);
            
            // If the user was successfully added, return true.
            if (rows > 0) {
                registered = true;
            } else {
                registered = false;
            }
            
        } catch (Exception e) {
            TimeZone.setDefault(myTimeZone);
        }
        return registered;
    }
    
    /**
     * Log a user into the application.
     * 
     * @param userName
     * @param password
     * 
     * @return ResultSet
     */
    public static Integer login(String userName, String password) throws Exception {
        
        Integer userId = null;
        // SQL statement.
        String sql = "SELECT userId FROM user WHERE userName=? and password=?";
        
        // Connect to the database.
        try(Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareCall(sql)) {
            
            // Bind parameters to the SQL statement
            stmt.setString(1, userName);
            stmt.setString(2, password);
            
            // Execute the statement.
            try (ResultSet rs = stmt.executeQuery()) {
                
                // If successful, return the userId, else throw an exception.
                try {
                    rs.next();
                    userId = rs.getInt("userId");
                } catch (Exception e) {
                    throw new Exception("Login failed.");
                }
            }
        }
        return userId;
    }
    
    /**
     * Return an ArrayList of City objects for all cities in the database.
     * 
     * @return 
     */
    public static ArrayList<City> getCities() {
        
        ArrayList<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM city";
        
        try(Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                // If rows were returned.
                while (rs.next()) {
                    int cityId = rs.getInt("cityId");
                    String city = rs.getString("city");
                    int countryId = rs.getInt("countryId");

                    // Create a City and add it to the ArrayList.
                    City newCity = new City(cityId, city, countryId);
                    cities.add(newCity);
                }
            }
        } catch (Exception e) {
            
        }
        
        // Return the ArrayList of cities.
        return cities;
    }
    
    /**
     * Return an ArrayList of Country objects for all countries in the database.
     * 
     * @return 
     */
    public static ArrayList<Country> getCountries() {
        
        ArrayList<Country> countries = new ArrayList<>();
        String sql = "SELECT * FROM country";
        
        try(Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                // If rows were returned.
                while (rs.next()) {
                    int countryId = rs.getInt("countryId");
                    String country = rs.getString("country");

                    // Create a new country and add it to the ArrayList.
                    Country newCountry = new Country(countryId, country);
                    countries.add(newCountry);
                }
            }
        } catch (Exception e) {
            
        }
        
        // Return the ArrayList of countries.
        return countries;
    }
    
    /**
     * Retrieve country data for a specific country, create a Country object, 
     * and return it.
     * 
     * @param countryId
     * @return 
     */
    public static Country getCountry(int countryId) {
        
        String sql = "SELECT * FROM country WHERE countryId=?";
        Country country = null;
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind parameters to the SQL statement
            stmt.setInt(1, countryId);
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                // If rows were returned, create a Country object and return it.
                if (rs.next()) {
                    int countryID = rs.getInt("countryId");
                    String countryName = rs.getString("country");
                    country = new Country(countryID, countryName);
                }
            }
        } catch (Exception e) {
            
        }
        
        // If an exception occurred or no country was found, return null.
        return country;
    }
    
    /**
     * Retrieve country data for a specific country, create a Country object, 
     * and return it.
     * 
     * @param countryName
     * @return 
     */
    public static Country getCountry(String countryName) {
        
        String sql = "SELECT * FROM country WHERE country=?";
        Country country = null;
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind parameters to the SQL statement
            stmt.setString(1, countryName);
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                // If rows were returned, create a Country object and return it.
                if (rs.next()) {
                    int countryID = rs.getInt("countryId");
                    String countryN = rs.getString("country");
                    country = new Country(countryID, countryN);
                }
            }
        } catch (Exception e) {
            
        }
        
        // If an exception occurred or no country was found, return null.
        return country;
    }
    
    /**
     * Create a new country in the database.
     * 
     * @param country
     * @param userId
     * 
     * @return key of the new country entry in the database.
     */
    public static Integer createCountry(String country, int userId) {
        
        // Check if a country with the given name already exists.
        Country countryObject = getCountry(country);
        
        // If a country with the given name already exists, return it's ID.
        if (countryObject != null) {
            return countryObject.getCountryId();
        }
        
        Integer key = null;
        String sql = "INSERT INTO country set country=?, createdBy=?, "
                    + "createDate=?, lastUpdate=?, lastUpdateBy=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to the statement
            stmt.setString(1, country);
            stmt.setInt(2, userId);
            stmt.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(5, userId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If insert was successful, return the key of the new entry.
            if (rows > 0) {
                
                // Return the key of the new entry in the database.
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        key = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            
        }
        
        // If an exception occurred, or a generated key wasn't returned, return null.
        return key;
    }
    
    /**
     * Insert a new city into the database.
     * 
     * @param city
     * @param countryId
     * @param userId
     * 
     * @return key of the new city entry in the database.
     */
    public static Integer createCity(String city, int countryId, int userId) {
        
        // Check if a city with the given name and country already exists.
        City cityObject = getCity(city, countryId);
        
        // If the city already exists, return the ID.
        if (cityObject != null) {
            return cityObject.getCityId();
        }
        
        Integer key = null;
        String sql = "INSERT INTO city set city=?, countryId=?, createdBy=?, "
                    + "createDate=?, lastUpdate=?, lastUpdateBy=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to the statement
            stmt.setString(1, city);
            stmt.setInt(2, countryId);
            stmt.setInt(3, userId);
            stmt.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(6, userId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If the insert was successful, return the key of the new entry.
            if (rows > 0) {
                
                // Return the key of the new entry.
                try (ResultSet generatedKey = stmt.getGeneratedKeys()) {
                    if (generatedKey.next()) {
                        key = generatedKey.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            
        }
        
        // If an exception occurred or no key was returned, return null;
        return key;
    }
    
    /**
     * Return an ArrayList of City objects for all cities in the database.
     * 
     * @param cityName
     * @param country
     * @return 
     */
    public static City getCity(String cityName, Integer country) {
        
        City city = null;
        String sql = "SELECT * FROM city WHERE city=? AND countryId=?";
        
        try(Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cityName);
            stmt.setInt(2, country);
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                
                // If rows were returned.
                if (rs.next()) {
                    int cityId = rs.getInt("cityId");
                    String cityN = rs.getString("city");
                    int countryId = rs.getInt("countryId");

                    // Create a City and add it to the ArrayList.
                    city = new City(cityId, cityN, countryId);
                }
            }
        } catch (Exception e) {
            
        }
        
        // Return the City.
        return city;
    }
    
    /**
     * Insert a new address into the database.
     * 
     * @param address
     * @param address2
     * @param cityId
     * @param postalCode
     * @param phone
     * @param userId
     * 
     * @return key of the new address entry in the database.
     */
    public static Integer createAddress(String address, String address2, int cityId,
            String postalCode, String phone, int userId) {
        
        Integer key = null;
        String sql = "INSERT INTO address SET address=?, address2=?, "
                    + "cityId=?, postalCode=?, phone=?, createdBy=?, "
                    + "createDate=?, lastUpdate=?, lastUpdateBy=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to the statement
            stmt.setString(1, address);
            stmt.setString(2, address2);
            stmt.setInt(3, cityId);
            stmt.setString(4, postalCode);
            stmt.setString(5, phone);
            stmt.setInt(6, userId);
            stmt.setTimestamp(7, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setTimestamp(8, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(9, userId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If insert was successful.
            if (rows > 0) {
                
                // Return the key of the new entry.
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        key = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {

        }
        
        // If an exception occurred or a generated key wasn't returned, return null.
        return key;
    }
    
    /**
     * Create a new customer.
     * 
     * @param customerName
     * @param addressId
     * @param active
     * @param userId
     * 
     * @return the key of the customer entry in the database.
     */
    public static Integer createCustomer(String customerName, int addressId, 
            int active, int userId) {
        
        Integer key = null;
        String sql = "INSERT INTO customer SET customerName=?, addressId=?, "
                    + "active=?, createdBy=?, createDate=?, lastUpdate=?, lastUpdateBy=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to the statement
            stmt.setString(1, customerName);
            stmt.setInt(2, addressId);
            stmt.setInt(3, active);
            stmt.setInt(4, userId);
            stmt.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(7, userId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If the insert was successful.
            if (rows > 0) {
                
                // Return the key of the new entry.
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        key = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            
        }
        
        // If an exception occurred or no key was returned, return null.
        return key;
    }
    
    /**
     * Update a customer.
     * 
     * @param customerId
     * @param customerName
     * @param addressId
     * @param userId
     * 
     * @return boolean
     */
    public static boolean updateCustomer(int customerId, String customerName, 
            int addressId, int userId) {
        
        boolean udpated = false;
        String sql = "UPDATE customer SET customerName=?, addressId=?, "
                    + "lastUpdate=?, lastUpdateBy=? WHERE customerId=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            // Bind parameters to the statement
            stmt.setString(1, customerName);
            stmt.setInt(2, addressId);
            stmt.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(4, userId);
            stmt.setInt(5, customerId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If update was successful, return true.
            if (rows > 0) {
                udpated = true;
            }
        } catch (Exception e) {
            
        }
        
        // If update failed or an exception occurred, return false.
        return udpated;
    }
    
    /**
     * Return an Customer object.
     * 
     * @return Customer
     */
    public static Customer getCustomer(int customerId) {
        
        Customer customer = null;
        String sql = "SELECT customer.customerId, customer.customerName, customer.addressId, customer.Active, "
                    + "address.address, address.address2, address.cityId, address.postalCode, address.phone, "
                    + "city.city, city.countryId, country.country FROM customer "
                    + "INNER JOIN address ON customer.addressId=address.addressId "
                    + "INNER JOIN city ON address.cityId=city.cityId "
                    + "INNER JOIN country ON city.countryId=country.countryId "
                    + "WHERE customerId=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind parameters to the statement.
            stmt.setInt(1, customerId);
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                
                // If rows were returned, create a Customer Object and return it.
                if (rs.next()) {
                    String customerName = rs.getString("customerName");
                    int addressId = rs.getInt("addressId");
                    int active = rs.getInt("active");
                    String address = rs.getString("address");
                    String address2 = rs.getString("address2");
                    int cityId = rs.getInt("cityId");
                    String postalCode = rs.getString("postalCode");
                    String phone = rs.getString("phone");
                    String city = rs.getString("city");
                    int countryId = rs.getInt("countryId");
                    String country = rs.getString("country");

                    customer = new Customer(customerId, customerName, active, addressId,
                    address, address2, cityId, postalCode, phone, city, countryId, country);
                }
            }
            
            
        } catch (Exception e) {
            
        }
        
        // If the customer wasn't found or an exception occurred, return null.
        return customer;
    }
    
    /**
     * Return an ArrayList of all customers in the database.
     * 
     * @return
     */
    public static ArrayList<Customer> getCustomers() {
        
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer.customerId, customer.customerName, customer.addressId, customer.Active, "
                    + "address.address, address.address2, address.cityId, address.postalCode, address.phone, "
                    + "city.city, city.countryId, country.country FROM customer "
                    + "INNER JOIN address ON customer.addressId=address.addressId "
                    + "INNER JOIN city ON address.cityId=city.cityId "
                    + "INNER JOIN country ON city.countryId=country.countryId "
                    + "WHERE customer.active=1 ";
        
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                
                // If rows were returned.
                while (rs.next()) {

                    // Create a Customer object and add it to the ArrayList.
                    int customerId = rs.getInt("customerId");
                    String customerName = rs.getString("customerName");
                    int addressId = rs.getInt("addressId");
                    int active = rs.getInt("active");
                    String address = rs.getString("address");
                    String address2 = rs.getString("address2");
                    int cityId = rs.getInt("cityId");
                    String postalCode = rs.getString("postalCode");
                    String phone = rs.getString("phone");
                    String city = rs.getString("city");
                    int countryId = rs.getInt("countryId");
                    String country = rs.getString("country");

                    Customer newCustomer = new Customer(customerId, customerName, active, addressId,
                        address, address2, cityId, postalCode, phone, city, countryId, country);

                    customers.add(newCustomer);
                }
            }
        } catch (Exception e) {
            
        }
        
        return customers;
    }
    
    /**
     * "Delete" a customer by setting active to 0.
     * The customer will remain in the database for record keeping purposes.
     * 
     * @param customerId
     * @param userId
     * 
     * @return boolean
     */
    public static boolean deleteCustomer(int customerId, int userId) {
        
        boolean deleted = false;
        String sql = "UPDATE customer SET active=?, lastUpdate=?, lastUpdateBy=? WHERE customerId=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to the statement
            stmt.setInt(1, 0);
            stmt.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(3, userId);
            stmt.setInt(4, customerId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If the update was successful, return true, otherwise return false.
            if (rows > 0) {
                deleted = true;
            }
        } catch (Exception e) {
            
        }
        
        return deleted;
    }
    
    /**
     * Insert a new appointment into the database.
     * 
     * @param customerId
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param url
     * @param start
     * @param end
     * @param type
     * @param userId
     * 
     * @return True if successful, false otherwise.
     */
    public static boolean createAppointment(int customerId, String title,
            String description, String location, int contact, String url, Date start, 
            Date end, String type, int userId) {
        
        boolean created = false;
        String sql = "INSERT INTO appointment SET customerId=?, title=?, "
                    + "description=?, location=?, contact=?, url=?, start=?, "
                    + "end=?, type=?, userId=?, createdBy=?, createDate=?, lastUpdate=?, "
                    + "lastUpdateBy=?";
        
        // Make a connection to the database.
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to the statement
            stmt.setInt(1, customerId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, location);
            stmt.setInt(5, contact);
            stmt.setString(6, url);
            stmt.setTimestamp(7, new Timestamp(start.getTime()));
            stmt.setTimestamp(8, new Timestamp(end.getTime()));
            stmt.setString(9, type);
            stmt.setInt(10, userId);
            stmt.setInt(11, userId);
            stmt.setTimestamp(12, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setTimestamp(13, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(14, userId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If the insert was successful, return true, else return false.
            if (rows > 0) {
                created = true;
            }
        } catch (Exception e) {
            
        }
        
        return created;
    }
    
    /**
     * Update an appointment in the database.
     * 
     * @param customerId
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param url
     * @param start
     * @param end
     * @param type
     * @param userId
     * 
     * @return True if successful, false otherwise.
     */
    public static boolean updateAppointment(int appointmentId, int customerId, String title,
            String description, String location, int contact, String url, Date start, 
            Date end, String type, int userId) {
        
        boolean updated = false;
        String sql = "UPDATE appointment SET customerId=?, title=?, "
                    + "description=?, location=?, contact=?, url=?, start=?, "
                    + "end=?, type=?, userId=?, lastUpdate=?, "
                    + "lastUpdateBy=? WHERE appointmentId=?";
        
        // Make a connection to the database.
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to the statement
            stmt.setInt(1, customerId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, location);
            stmt.setInt(5, contact);
            stmt.setString(6, url);
            stmt.setTimestamp(7, new Timestamp(start.getTime()));
            stmt.setTimestamp(8, new Timestamp(end.getTime()));
            stmt.setString(9, type);
            stmt.setInt(10, userId);
            stmt.setTimestamp(11, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            stmt.setInt(12, userId);
            stmt.setInt(13, appointmentId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If the insert was successful, return true, else return false.
            if (rows > 0) {
                updated = true;
            }
        } catch (Exception e) {
            
        }
        
        return updated;
    }
    
    /**
     * Delete an appointment.
     * 
     * @param appointmentId
     * 
     * @return boolean
     */
    public static boolean deleteAppointment(int appointmentId) {
        
        boolean deleted = false;
        String sql = "DELETE FROM appointment WHERE appointmentId=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind parameters to the statement.
            stmt.setInt(1, appointmentId);
            
            // Execute the statement.
            int rows = stmt.executeUpdate();
            
            // If the insert was successful, return true, else return false.
            if (rows > 0) {
                deleted = true;
            }
        } catch (Exception e) {
            
        }
        
        return deleted;
    }
    
    /**
     * Retrieve appointments from the database. Create an array with a size of
     * the number of days in the month of the start date, plus 1. Place an 
     * ArrayList of Appointment objects in each index of the array. Add 
     * appointments to the ArrayList in the index of the array for the day
     * the appointment is on.
     * 
     * @param start
     * @param end
     * 
     * @return an Array of ArrayLists of appointments.
     */
    public static ArrayList<Appointment>[] getAppointments(Date start, Date end, int userId) {
        
        
        
        // Create an ArrayList the length of the start date's month. The start
        // date and end date will be on the same day of the month, but different
        // times.
        Calendar c = Calendar.getInstance();
        
        c.setTime(start);
        int daysInStartMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        c.setTime(end);
        int daysInEndMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        int daysInMonth = Integer.max(daysInStartMonth, daysInEndMonth);
        
        ArrayList<Appointment>[] appointmentList = new ArrayList[daysInMonth+1];
        
        // Place an ArrayList in each index of the array.
        for (int i = 0; i < daysInMonth + 1; i++) {
            appointmentList[i] = new ArrayList<>();
        }
        
        TimeZone myTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
        
        String sql = "SELECT appointment.appointmentId, appointment.customerId, "
                    + "appointment.title, appointment.description, appointment.location, "
                    + "appointment.contact, appointment.url, appointment.start, "
                    + "appointment.end, appointment.type, appointment.userId, "
                    + "customer.customerName FROM appointment "
                    + "INNER JOIN customer ON appointment.customerId=customer.customerId "
                    + "WHERE start>=? and start<=? and userId=? ORDER BY start";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind parameters to the SQL statement
            stmt.setTimestamp(1, new Timestamp(start.getTime()));
            stmt.setTimestamp(2, new Timestamp(end.getTime()));
            stmt.setInt(3, userId);
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                // If rows were returned.
                while (rs.next()) {

                    // Create an Appointment object.
                    int appointmentId = rs.getInt("appointmentId");
                    int customerId = rs.getInt("customerId");
                    String customerName = rs.getString("customerName");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String location = rs.getString("location");
                    String contact = rs.getString("contact");
                    String url = rs.getString("url");
                    Date startDate = rs.getTimestamp("start");
                    Date endDate = rs.getTimestamp("end");
                    String type = rs.getString("type");
                    int userID = rs.getInt("userId");
                    Appointment appointment = new Appointment(appointmentId, customerId, customerName, 
                        title, description, location, contact, startDate, endDate, type, userID);

                    // Add the appointment to the ArrayList in the index of the array
                    // that matches the day of the month of the appointment.
                    c.setTime(startDate);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    appointmentList[day].add(appointment);
                }
            }
            
        } catch (Exception e) {
            
        }
        
        TimeZone.setDefault(myTimeZone);
        return appointmentList;
    }
    
    /**
     * Retrieve appointments from the database. Create an array with a size of
     * the number of days in the month of the start date, plus 1. Place an 
     * ArrayList of Appointment objects in each index of the array. Add 
     * appointments to the ArrayList in the index of the array for the day
     * the appointment is on.
     * 
     * @param start
     * @param end
     * @param appointmentId
     * @param userId
     * @throws java.lang.Exception if overlapping appointments are found.
     */
    public static void overlappingAppointments(Date start, Date end, 
            int appointmentId, int userId) throws Exception {
        
        String sql = "SELECT appointment.appointmentId, appointment.customerId, "
                    + "appointment.title, appointment.description, appointment.location, "
                    + "appointment.contact, appointment.url, appointment.start, "
                    + "appointment.end, appointment.type, appointment.userId, "
                    + "customer.customerName FROM appointment "
                    + "INNER JOIN customer ON appointment.customerId=customer.customerId "
                    + "WHERE ((start>=? and start<?) OR (end>? and end<=?) OR "
                    + "(start<=? AND end>=?)) AND userId=? "
                    + "AND appointmentId!=?";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind parameters to the SQL statement
            stmt.setTimestamp(1, new Timestamp(start.getTime()));
            stmt.setTimestamp(2, new Timestamp(end.getTime()));
            stmt.setTimestamp(3, new Timestamp(start.getTime()));
            stmt.setTimestamp(4, new Timestamp(end.getTime()));
            stmt.setTimestamp(5, new Timestamp(start.getTime()));
            stmt.setTimestamp(6, new Timestamp(end.getTime()));
            stmt.setInt(7, userId);
            stmt.setInt(8, appointmentId);
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    throw new Exception("Appointment overlaps with an \nexisting appointment.");
                }
            }
        }
    }
    
    
    /**
     * Retrieve the number of appointment types by month.
     * 
     * @return 
     */
    public static ObservableList<GroupCount> getAppointmentTypeByMonth() {
        
        ObservableList<GroupCount> appointmentsByMonth = FXCollections.observableArrayList();
        
        String sql = "SELECT COUNT(sub.type) AS numTypes, sub.start AS month "
                    + "FROM (SELECT type, MONTH(start) AS start FROM appointment "
                    + "GROUP BY type, MONTH(start)) sub GROUP BY sub.start";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String month = DateTimeUtil.getMonthString(rs.getInt("month"));
                    int numTypes = rs.getInt("numTypes");

                    GroupCount reportHelper = new GroupCount(month, numTypes);
                    appointmentsByMonth.add(reportHelper);
                }
            }
            
        } catch (Exception e) {
            
        }
        
        return appointmentsByMonth;
    }
    
    /**
     * Retrieve the number of appointment types by month.
     * 
     * @return 
     */
    public static ObservableList<Appointment> getAllAppointments() {
        
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String sql = "SELECT appointment.appointmentId, appointment.customerId, "
                    + "appointment.title, appointment.description, appointment.location, "
                    + "appointment.contact, appointment.url, appointment.start, "
                    + "appointment.end, appointment.type, appointment.userId, "
                    + "customer.customerName FROM appointment "
                    + "INNER JOIN customer ON appointment.customerId=customer.customerId "
                    + "ORDER BY appointment.userId, appointment.start";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Create an Appointment object.
                    int appointmentId = rs.getInt("appointmentId");
                    int customerId = rs.getInt("customerId");
                    String customerName = rs.getString("customerName");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String location = rs.getString("location");
                    String contact = rs.getString("contact");
                    String url = rs.getString("url");
                    Date startDate = rs.getTimestamp("start");
                    Date endDate = rs.getTimestamp("end");
                    String type = rs.getString("type");
                    int userId = rs.getInt("userId");
                    Appointment appointment = new Appointment(appointmentId, customerId, customerName, 
                        title, description, location, contact, startDate, endDate, type, userId);

                    allAppointments.add(appointment);
                }
            }
            
        } catch (Exception e) {
            
        }
        
        return allAppointments;
    }
    
    /**
     * Retrieve the number of customers by consultant.
     * 
     * @return 
     */
    public static ObservableList<GroupCount> getCustomersByConsultant() {
        
        ObservableList<GroupCount> appointmentsByMonth = FXCollections.observableArrayList();
        String sql = "SELECT COUNT(sub.customer) as customers, userId FROM (SELECT "
                    + "DISTINCT(customerId) AS customer, userId FROM appointment) "
                    + "sub GROUP BY userId";
        
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Execute the query.
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String month = String.valueOf(rs.getInt("userId"));
                    int numTypes = rs.getInt("customers");

                    GroupCount reportHelper = new GroupCount(month, numTypes);
                    appointmentsByMonth.add(reportHelper);
                }
            }
            
        } catch (Exception e) {
            
        }
        
        return appointmentsByMonth;
    }
}
