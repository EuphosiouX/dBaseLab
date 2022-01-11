/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
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
import layproject.classes.Memberships;
import layproject.interfaces.CheckTextField;
import layproject.interfaces.ItemQuery;

/**
 * FXML Controller class
 *
 * @author Michael Christopher
 */
public class MembershipsDashboardController implements Initializable, CheckTextField, ItemQuery {

    private final JDBConnection dbLink = new JDBConnection("restaurant");
    private final Connection con = dbLink.getConnection();
    private final FxmlLoader loader = new FxmlLoader();
    
    private String query;
    private ResultSet rs;
    
    @FXML
    private TableView<Memberships> membershipsTb;
    @FXML
    private TableColumn<Memberships, Integer> membershipsId;
    @FXML
    private TableColumn<Memberships, String> firstName;
    @FXML
    private TableColumn<Memberships, String> lastName;
    @FXML
    private TableColumn<Memberships, String> phoneNo;
    @FXML
    private TableColumn<Memberships, String> birthDate;
    @FXML
    private TableColumn<Memberships, String> joinDate;
    @FXML
    private TableColumn<Memberships, Integer > points;
    @FXML
    private JFXButton searchBtn;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton updateBtn;
    @FXML
    private JFXButton removeBtn;
    @FXML
    private JFXDatePicker birthDatePick;
    @FXML
    private Label membershipIdLabel;
    @FXML
    private JFXTextField firstNameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXTextField phoneNoField;
    @FXML
    private JFXTextField pointsField;
    @FXML
    private JFXTextField phoneNoSearchField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showItemList("");
        birthDatePick.setValue(LocalDate.now());
        pointsField.setText("0");
    }    

    @FXML
    private void searchButtonClicked(ActionEvent event) {
        searchItem();
        phoneNoSearchField.setText("");
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
            // Insert item into table by calling insertItem()
            updateItem();
            // Throw alert
            loader.showAlert("Item successfully updated");
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
            // Insert item into table by calling insertItem()
            deleteItem();
            // Throw alert
            loader.showAlert("Item successfully deleted");
        }
        setEmpty();
    }

    @FXML
    private void membershipTableClicked(MouseEvent event) {
        Memberships membership = membershipsTb.getSelectionModel().getSelectedItem();
        membershipIdLabel.setText("" + membership.getMembership_id());
        firstNameField.setText(membership.getFirst_name());
        lastNameField.setText(membership.getLast_name());
        phoneNoField.setText(membership.getPhone_no());
        pointsField.setText("" + membership.getPoints());
        birthDatePick.setValue(LocalDate.parse(membership.getBirth_date()));
    }
    
    @Override
    public boolean textFieldIsEmpty() {
        return (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || phoneNoField.getText().isEmpty() || birthDatePick.getValue() == null || pointsField.getText() == "0");
    }

    @Override
    public ObservableList getItemList(String q) {
        
        ObservableList<Memberships> membershipsList = FXCollections.observableArrayList();
        query = "SELECT * FROM memberships" + q;
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                Memberships membership = new Memberships(rs.getInt("membership_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("phone_no"), rs.getString("birth_date"), rs.getString("join_date"), rs.getInt("points"));
                membershipsList.add(membership);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return membershipsList;
    }

    @Override
    public void showItemList(String q) {
        // Create list by calling getItemList() 
        ObservableList<Memberships> list = getItemList(q);
        // Set cell value factory of each table's cell
        membershipsId.setCellValueFactory(new PropertyValueFactory<>("membership_id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        phoneNo.setCellValueFactory(new PropertyValueFactory<>("phone_no"));
        birthDate.setCellValueFactory(new PropertyValueFactory<>("birth_date"));   
        joinDate.setCellValueFactory(new PropertyValueFactory<>("join_date")); 
        points.setCellValueFactory(new PropertyValueFactory<>("points"));   
        // Set items in the table
        membershipsTb.setItems(list);
    }

    @Override
    public void insertItem() {
        query = "INSERT INTO memberships VALUES (NULL, '" + firstNameField.getText() + "','" + lastNameField.getText() + "','" 
                       + phoneNoField.getText() + "','" + birthDatePick.getValue().toString() + "', CURRENT_DATE()," + points.getText() + ")";
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void updateItem() {
        query = "UPDATE memberships SET first_name  = '" + firstNameField.getText() + "', last_name = '" + lastNameField.getText()+ "', phone_no = '" + 
                phoneNoField.getText() + "', birth_date = '" + birthDatePick.getValue().toString() + "', points = " + pointsField.getText() + " WHERE membership_id = " + membershipIdLabel.getText();
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void deleteItem() {
        query = "DELETE FROM memberships WHERE membership_id = " + membershipIdLabel.getText();
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void searchItem() {
        showItemList(" WHERE phone_no LIKE '%" + phoneNoSearchField.getText() + "%'");
    }

    @Override
    public boolean isExist() {
        query = "SELECT count(1) FROM memberships WHERE first_name = '" + firstNameField.getText() + "' AND last_name = '" + lastNameField.getText() + "' AND phone_no = '" + phoneNoField.getText() + "'";
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
        membershipIdLabel.setText("Id");
        firstNameField.setText("");
        lastNameField.setText("");
        phoneNoField.setText("");
        pointsField.setText("");
        birthDatePick.setValue(LocalDate.now());
        pointsField.setText("0");
    }

}
