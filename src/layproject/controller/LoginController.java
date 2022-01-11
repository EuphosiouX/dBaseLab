/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import layproject.classes.FxmlLoader;
import layproject.classes.JDBConnection;
import layproject.interfaces.CheckTextField;

/**
 * FXML Controller class
 *
 * @author Michael Christopher
 */
public class LoginController implements Initializable, CheckTextField {
    
    private final JDBConnection dbLink = new JDBConnection("restaurant");
    private final Connection con = dbLink.getConnection(); 
    private final FxmlLoader loader = new FxmlLoader();
    private Stage stage;
    
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXButton cancelBtn;
    @FXML
    private JFXComboBox<String> staffComb;
    

    /**
     * Initializes the controller class.
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set items in 'Genre' combo box
        ObservableList<String> staffList = FXCollections.observableArrayList("Manager", "Staff");
        staffComb.setItems(staffList);
    }    

    @FXML
    private void loginButtonClicked(ActionEvent event) throws IOException {
        
        FXMLLoader ldr = loader.loadStage("/layproject/fxml/askMembership.fxml");
        Parent root = ldr.load();
        String user = username.getText();
        String pass = password.getText();
        AskMembershipController controller = ldr.getController();
        
        // Check if textFieldIsEmpty() == false
        if (!textFieldIsEmpty())      
            if(staffComb.getSelectionModel().getSelectedItem() == "Manager"){         
                if(checkLoginManager()){
                    // Show Dashboard.fxml by calling showStage() from FxmlLoader
                    loader.showStage("/layproject/fxml/ManagerDashboard.fxml");
                    // Close current FXML
                    stage = (Stage) loginBtn.getScene().getWindow();
                    stage.close();
                }
                else{
                    // Throw alert
                    loader.showAlert("Username / password invalid or doesn't exist!!");
                }
            }
            else{
                if(checkLoginCashier()){
                    String[] list = setCashier(user, pass);                 
                    controller.getCashier(list[0], list[1]);
//                    System.out.println(list[0]);
//                    System.out.println(list[1]);

                    stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                    stage = (Stage) loginBtn.getScene().getWindow();
                    stage.close();
                }
                else{
                    // Throw alert
                    loader.showAlert("Username / password invalid or doesn't exist!!");
                }
            }               
        else{
            // Throw alert
            loader.showAlert("Fill in required data!!");
        }
        setEmpty();
    }

    @FXML
    private void cancelBtnClicked(ActionEvent event) {
        // Close current FXML
        stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    
    public boolean checkLoginManager(){
        // Set query
        String query = "SELECT count(1)\n" +
                       "FROM staff\n" +
                       "INNER JOIN staffcategory ON staff.staff_category_id = staffcategory.staff_category_id\n" +
                       "WHERE staffcategory.category_name = 'Manager'\n" + 
                       "AND staff.username = '" + username.getText() + "' AND staff.password ='" + password.getText() + "'" ;
        ResultSet rs= dbLink.queryResult(query);
        
        // Try the query
        try{
            while(rs.next()){
                // Check if value in first column in databse == 1
                if(rs.getInt(1) != 0){
                    return true;
                }
            }    
        }
        // Catch exception if query is not correct
        catch(Exception ex){
            // Print stack trace
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean checkLoginCashier(){
         // Set query
        String query = "SELECT count(1)\n" +
                       "FROM staff\n" +
                       "INNER JOIN staffcategory ON staff.staff_category_id = staffcategory.staff_category_id\n" +
                       "WHERE staffcategory.category_name != 'Manager'\n" + 
                       "AND staff.username = '" + username.getText() + "' AND staff.password ='" + password.getText() + "'" ;
        ResultSet rs= dbLink.queryResult(query);
        
        // Try the query
        try{
            while(rs.next()){
                // Check if value in first column in databse == 1
                if(rs.getInt(1) != 0){
                    return true;
                }
            }    
        }
        // Catch exception if query is not correct
        catch(Exception ex){
            // Print stack trace
            ex.printStackTrace();
        }
        return false;
    }

    public String[] setCashier(String user, String pass){
        
        String[] list = new String[2];
         // Set query
        String query = "SELECT staff_id, first_name, last_name FROM staff WHERE username = '" + user + "' AND password = '" + pass + "'";
        ResultSet rs= dbLink.queryResult(query);
        
        // Try the query
        try{
            while(rs.next()){
                list[0] = "" + rs.getInt("staff_id");
                list[1] = rs.getString("first_name") + " " + rs.getString("last_name");
            }  
        }
        // Catch exception if query is not correct
        catch(Exception ex){
            // Print stack trace
            ex.printStackTrace();
        }
        return list;
    }
    
    @Override
    public boolean textFieldIsEmpty() {
        return (username.getText().isEmpty() || password.getText().isEmpty() || staffComb.getSelectionModel().getSelectedItem() == null);
    }

    @Override
    public void setEmpty() {
        username.setText("");
        password.setText("");
    }

}
