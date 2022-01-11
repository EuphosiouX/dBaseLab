// classes.connection package
package layproject.classes;

// Importing required module, libary, and package
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// JDBConnection class
public class JDBConnection {
    
    // Set class variables
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private Connection con;

    // Constructor 1
    public JDBConnection(String dbName, String dbUsername, String dbPassword) {
        this.dbName = dbName;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }
    
    // Constructor 2
    public JDBConnection(String dbName) {
        this.dbName = dbName;
        // Set default username and password if not provided
        this.dbUsername = "root";
        this.dbPassword = "";
    }

    // DbName getter
    public String getDbName() {
        return dbName;
    }

    // DbUsername getter
    public String getDbUsername() {
        return dbUsername;
    }

    // DbPassword getter
    public String getDbPassword() {
        return dbPassword;
    }   
    
    // Method to connect to desired database
    public Connection getConnection(){
        try{
            // Try to connect to the databse
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + getDbName(), getDbUsername(), getDbPassword());
            return con;
        }
        catch(Exception ex){
            // Display error if can't connect to database
            System.out.println("Error: "+ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    // Method to execute the provided query
    public void executeQuery(String query){
        // Declare st variable
        Statement st;     
        try{
            // Create statement
            st = con.createStatement();
            // Execute the query
            st.executeUpdate(query);
        }
        // Catch exception if query is not correct
        catch(Exception ex){
            // Print stack trace
            ex.printStackTrace();
        }
    }
    
    public ResultSet queryResult(String query){
        Statement st;
        ResultSet rs = null;
        
        try{
            // Create statement
            st = con.createStatement();
            // Execute the query
            rs = st.executeQuery(query);
        }
        // Catch exception if query is not correct        
        catch(Exception ex){
            // Print stack trace
            ex.printStackTrace();
        }
        return rs;
    }
}