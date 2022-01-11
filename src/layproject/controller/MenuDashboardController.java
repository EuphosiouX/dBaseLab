/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;
import layproject.classes.FxmlLoader;
import layproject.classes.Menu;
import layproject.classes.JDBConnection;
import layproject.interfaces.CheckTextField;
import layproject.interfaces.ItemQuery;

/**
 * FXML Controller class
 *
 * @author Michael Christopher
 */
public class MenuDashboardController implements Initializable, CheckTextField, ItemQuery {
    
    private final JDBConnection dbLink = new JDBConnection("restaurant");
    private final Connection con = dbLink.getConnection();
    private final FxmlLoader loader = new FxmlLoader();
    
    private String query;
    private ResultSet rs;

    @FXML
    private TableView<Menu> menuTb;
    @FXML
    private TableColumn<Menu, Integer> menuId;
    @FXML
    private TableColumn<Menu, String> menuName;
    @FXML
    private TableColumn<Menu, Float> price;
    @FXML
    private TableColumn<Menu, Integer> stock;
    @FXML
    private TableColumn<Menu, String> category;
    @FXML
    private JFXComboBox<String> categoryComb;
    @FXML
    private JFXButton searchBtn;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton updateBtn;
    @FXML
    private JFXButton removeBtn;
    @FXML
    private JFXTextField menuNameField;
     @FXML
    private JFXTextField priceField;
    @FXML
    private JFXTextField stockField;
    @FXML
    private JFXTextField menuNameSearchField;
    @FXML
    private Label menuIdLabel;
    
  

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> categoryList = FXCollections.observableArrayList("Appetizer", "Main Course", "Desert", "Beverages");
        categoryComb.setItems(categoryList); 
        showItemList("");
    }    

    @FXML
    private void searchButtonClicked(ActionEvent event) {
        searchItem();
        menuNameSearchField.setText("");
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
    private void menuTableClicked(MouseEvent event) {
        Menu menu = menuTb.getSelectionModel().getSelectedItem();
        menuIdLabel.setText("" + menu.getMenu_id());
        menuNameField.setText(menu.getMenu_name());
        priceField.setText("" + menu.getPrice());
        stockField.setText("" + menu.getStock());
    }
    
    @Override
    public boolean textFieldIsEmpty() {
        return (menuNameField.getText().isEmpty() || stockField.getText().isEmpty() || categoryComb.getSelectionModel().getSelectedItem() == null);
    }

    @Override
    public ObservableList getItemList(String q) {
        
        ObservableList<Menu> menuList = FXCollections.observableArrayList();
        query = "SELECT * FROM menu" + q;
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                Menu menu = new Menu(rs.getInt("menu_id"), rs.getString("menu_name"), rs.getFloat("price"), rs.getInt("stock"), rs.getString("category"));
                menuList.add(menu);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return menuList;
    }

    @Override
    public void showItemList(String q) {
        // Create list by calling getItemList() 
        ObservableList<Menu> list = getItemList(q);
        // Set cell value factory of each table's cell
        menuId.setCellValueFactory(new PropertyValueFactory<>("menu_id"));
        menuName.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        // Set items in the table
        menuTb.setItems(list);
    }

    @Override
    public void insertItem() {
        query = "INSERT INTO menu VALUES (NULL, '" + menuNameField.getText() + "'," + priceField.getText() + "," + stockField.getText() + ",'" 
                       + categoryComb.getSelectionModel().getSelectedItem() + "')";
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void updateItem() {
        query = "UPDATE menu SET menu_name  = '" + menuNameField.getText() + "', price = " + priceField.getText() + ", stock = " + stockField.getText()+ ", category = '" + 
                categoryComb.getSelectionModel().getSelectedItem() + "' WHERE menu_id = " + menuIdLabel.getText();
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void deleteItem() {
        query = "DELETE FROM menu WHERE menu_id = " + menuIdLabel.getText();
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void searchItem() {
        showItemList(" WHERE menu_name LIKE '%" + menuNameSearchField.getText() + "%'");
    }
    
    @Override
    public boolean isExist(){
        query = "SELECT count(1) FROM menu WHERE menu_name = '" + menuNameField.getText() + "'";
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
    public void setEmpty(){
        menuIdLabel.setText("Id");
        menuNameField.setText("");
        priceField.setText("");
        stockField.setText("");
    }
}
