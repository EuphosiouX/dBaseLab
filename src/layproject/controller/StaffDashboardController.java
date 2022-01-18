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
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import layproject.classes.FxmlLoader;
import layproject.classes.JDBConnection;
import layproject.classes.Staff;
import layproject.interfaces.CheckTextField;
import layproject.interfaces.ItemQuery;

/**
 * FXML Controller class
 *
 * @author Michael Christopher
 */
public class StaffDashboardController implements Initializable, CheckTextField, ItemQuery {

    private final JDBConnection dbLink = new JDBConnection("restaurant");
    private final Connection con = dbLink.getConnection();
    private final FxmlLoader loader = new FxmlLoader();
    
    private String query;
    private ResultSet rs;
    private int id = 0;
    
    @FXML
    private TableView<Staff> staffTb;
    @FXML
    private TableColumn<Staff, Integer> staffId;
    @FXML
    private TableColumn<Staff, String> firstName;
    @FXML
    private TableColumn<Staff, String> lastName;
    @FXML
    private TableColumn<Staff, String> address;
    @FXML
    private TableColumn<Staff, String> phoneNo;
    @FXML
    private TableColumn<Staff, String> status;
    @FXML
    private TableColumn<Staff, String> username;
    @FXML
    private TableColumn<Staff, String> password;
    @FXML
    private TableColumn<Staff, Integer> staffCategoryId;
    @FXML
    private JFXComboBox<String> statusComb;
    @FXML
    private JFXComboBox<String> staffCategoryComb;
    @FXML
    private JFXButton searchBtn;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton updateBtn;
    @FXML
    private JFXButton removeBtn;
    @FXML
    private Label staffIdLabel;
    @FXML
    private JFXTextField firstNameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXTextField addressField;
    @FXML
    private JFXTextField phoneNoField;
    @FXML
    private JFXTextField firstNameSearchField;
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> statusList = FXCollections.observableArrayList("Full Time", "Part Time");
        statusComb.setItems(statusList);
        
        ObservableList<String> staffCategoryList = FXCollections.observableArrayList();
        
        query = "SELECT staff_category_id, category_name, working_days, working_hours FROM staffcategory WHERE category_name != 'Manager'";
        rs = dbLink.queryResult(query);
        
        try {
            while(rs.next()){
                staffCategoryList.add(rs.getInt("staff_category_id") + " -- " + rs.getString("category_name") + " -- " + rs.getString("working_days") + "->" + rs.getString("working_hours"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        staffCategoryComb.setItems(staffCategoryList);
        showItemList("");
    }    

    @FXML
    private void searchButtonClicked(ActionEvent event) {
        searchItem();
        firstNameSearchField.setText("");
    }

    @FXML
    private void addButtonClicked(ActionEvent event) {
         // Check if textFieldIsEmpty() == true
        if(textFieldIsEmpty()){
            // Throw alert
            loader.showAlert("Fill in required data!!");
        }
        // Check if searchTitle() == true
        else if(isExist()){
            // Throw alert
            loader.showAlert("Item already exist!!");
        }
        else{
            // Insert item into table by calling insertItem()
            insertItem();
            // Throw alert
            loader.showAlert("Item successfully inserted");
        }  
        setEmpty();
    }

    @FXML
    private void updateButtonClicked(ActionEvent event) {
                 // Check if textFieldIsEmpty() == true
        if(textFieldIsEmpty()){
            // Throw alert
            loader.showAlert("Fill in required data!!");
        }
        else{
            updateItem();
            // Throw alert
            loader.showAlert("Item successfully inserted");
        }  
        setEmpty();
    }

    @FXML
    private void removeButtonClicked(ActionEvent event) {
        // Check if textFieldIsEmpty() == true
        if(textFieldIsEmpty()){
            // Throw alert
            loader.showAlert("Fill in required data!!");
        }
        else{
            deleteItem();
            // Throw alert
            loader.showAlert("Item successfully inserted");
        }  
        setEmpty();
    }

    @FXML
    private void cashierTableClicked(MouseEvent event) {
        Staff staff = staffTb.getSelectionModel().getSelectedItem();
        staffIdLabel.setText("" + staff.getStaff_id());
        firstNameField.setText(staff.getFirst_name());
        lastNameField.setText(staff.getLast_name());
        addressField.setText(staff.getAddress());
        phoneNoField.setText(staff.getPhone_no());
    }

    @Override
    public boolean textFieldIsEmpty() {
        return (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || addressField.getText().isEmpty() || phoneNoField.getText().isEmpty() || statusComb.getSelectionModel().getSelectedItem() == null || staffCategoryComb.getSelectionModel().getSelectedItem() == null);
    }

    @Override
    public ObservableList getItemList(String q) {
        
        ObservableList<Staff> staffList = FXCollections.observableArrayList();
        query = "SELECT staff.*\n" +
                "FROM staff\n" +
                "INNER JOIN staffcategory ON staff.staff_category_id = staffcategory.staff_category_id" +
                " WHERE staffcategory.category_name != 'Manager'";
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                Staff staff = new Staff(rs.getInt("staff_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("address"), rs.getString("phone_no"), rs.getString("status"), rs.getString("username"), rs.getString("password"), rs.getInt("staff_category_id"));
                staffList.add(staff);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return staffList;
    }

    @Override
    public void showItemList(String q) {
        // Create list by calling getItemList() 
        ObservableList<Staff> list = getItemList(q);
        // Set cell value factory of each table's cell
        staffId.setCellValueFactory(new PropertyValueFactory<>("staff_id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNo.setCellValueFactory(new PropertyValueFactory<>("phone_no"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));   
        username.setCellValueFactory(new PropertyValueFactory<>("username"));  
        password.setCellValueFactory(new PropertyValueFactory<>("password"));   
        staffCategoryId.setCellValueFactory(new PropertyValueFactory<>("staff_category_id"));   
        // Set items in the table
        staffTb.setItems(list);
    }

    @Override
    public void insertItem() {
        
        query = "INSERT INTO staff VALUES (NULL, '" + firstNameField.getText() + "','" + lastNameField.getText() + "','" + addressField.getText() + "','" + phoneNoField.getText() + "','"
                       + statusComb.getSelectionModel().getSelectedItem() + "','" + "|" + "','" + firstNameField.getText().substring(0,1) + lastNameField.getText().substring(0,1) + phoneNoField.getText() + "'," + staffCategoryComb.getSelectionModel().getSelectedItem().substring(0,1) +  ")";
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        
        query = "SELECT staff_id FROM staff ORDER BY staff_id DESC LIMIT 1";
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                id = rs.getInt("staff_id");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "UPDATE staff SET username ='" + firstNameField.getText() + lastNameField.getText().substring(0,1) + id + "' WHERE staff_id = " + id;
        dbLink.executeQuery(query);
        
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void updateItem() {
        query = "UPDATE staff SET first_name  = '" + firstNameField.getText() + "', last_name = '" + lastNameField.getText()+ "', address = '" + addressField.getText() + "', phone_no = '" + phoneNoField.getText() + 
                "', status = '" + statusComb.getSelectionModel().getSelectedItem() + "', staff_category_id = " + staffCategoryComb.getSelectionModel().getSelectedItem().substring(0,1) + " WHERE staff_id = " + staffIdLabel.getText();
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void deleteItem() {
         query = "DELETE FROM staff WHERE staff_id = " + staffIdLabel.getText();
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void searchItem() {
        showItemList(" AND first_name LIKE '%" + firstNameSearchField.getText() + "%'");
    }

    @Override
    public boolean isExist() {
        query = "SELECT count(1) FROM staff WHERE first_name = '" + firstNameField.getText() + "' AND last_name = '" + lastNameField.getText() + "' AND phone_no = '" + phoneNoField.getText() + "' AND staff_category_id = " + staffCategoryComb.getSelectionModel().getSelectedItem().substring(0,1);
        rs = dbLink.queryResult(query);
        
        try {
            while(rs.next()){
                if(rs.getInt(1) != 0){
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void setEmpty() {
        staffIdLabel.setText("Id");
        firstNameField.setText("");
        lastNameField.setText("");
        address.setText("");
        phoneNo.setText("");
    }
}
