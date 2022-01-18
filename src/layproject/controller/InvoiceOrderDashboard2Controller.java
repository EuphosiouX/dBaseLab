/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject.controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.stage.Stage;
import layproject.classes.FxmlLoader;
import layproject.classes.Invoice;
import layproject.classes.JDBConnection;
import layproject.classes.Order;
import layproject.interfaces.CheckTextField;

/**
 * FXML Controller class
 *
 * @author Michael Christopher
 */
public class InvoiceOrderDashboard2Controller implements Initializable {

    private final JDBConnection dbLink = new JDBConnection("restaurant");
    private final Connection con = dbLink.getConnection();
    private final FxmlLoader loader = new FxmlLoader();
    
    private String query;
    private ResultSet rs;
    private int ivcId = 0;
    private Stage stage;
    
    @FXML
    private JFXTextField invoiceIdSearchField;
    @FXML
    private JFXButton searchBtn;
    @FXML
    private TableView<Order> orderTb;
    @FXML
    private TableColumn<Order, Integer> orderId;
    @FXML
    private TableColumn<Order, Integer> menuId;
    @FXML
    private TableColumn<Order, Float> price;
    @FXML
    private TableColumn<Order, Integer> quantity;
    @FXML
    private TableColumn<Order, Float> total;
    @FXML
    private TableColumn<Order, Integer> orderInvoiceId;
    @FXML
    private TableView<Invoice> invoiceTb;
    @FXML
    private TableColumn<Invoice, Integer> invoiceId;
    @FXML
    private TableColumn<Invoice, String> transactionDate;
    @FXML
    private TableColumn<Invoice, String> paymentMethod;
    @FXML
    private TableColumn<Invoice, Float> invoicePrice;
    @FXML
    private TableColumn<Invoice, Integer> staffId;
    @FXML
    private TableColumn<Invoice, Integer> membershipId;
    @FXML
    private Label cashierIdLabel;
    @FXML
    private Label cashierNameLabel;
    @FXML
    private Label membershipIdLabel;
    @FXML
    private Label membershipNameLabel;
    @FXML
    private Label menuIdLabel;
    @FXML
    private Label menuNameLabel;
    @FXML
    private JFXButton resetBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showInvoice("");
        showOrder("");
    }    

    @FXML
    private void searchButtonClicked(ActionEvent event) {
        searchItem();
        invoiceIdSearchField.setText("");
    }
    
    @FXML
    private void resetbuttonClicked(ActionEvent event) {
        showInvoice("");
        showOrder("");
        ivcId = 0;
        setInvoiceEmpty();
        setOrderEmpty();
    }

    @FXML
    private void invoiceTableClicked(MouseEvent event) {
        
        Invoice invoice = invoiceTb.getSelectionModel().getSelectedItem();
        
        ivcId = invoice.getInvoice_id();
        query = "SELECT staff_id, first_name, last_name FROM staff WHERE staff_id = " + invoice.getStaff_id();
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                cashierIdLabel.setText("" + rs.getInt("staff_id"));
                cashierNameLabel.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "SELECT membership_id, first_name, last_name FROM memberships WHERE membership_id = " + invoice.getMembership_id();
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                membershipIdLabel.setText("" + rs.getInt("membership_id"));
                membershipNameLabel.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        showChosen();
        setOrderEmpty();
    }
    
    @FXML
    private void orderTableClicked(MouseEvent event) {
        Order order = orderTb.getSelectionModel().getSelectedItem();
        
        query = "SELECT menu_id, menu_name FROM menu WHERE menu_id = " + order.getMenu_id();
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                menuIdLabel.setText("" + rs.getInt("menu_id"));
                menuNameLabel.setText(rs.getString("menu_name"));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public ObservableList getInvoice(String q) {
        
        ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();
        query = "SELECT * FROM invoice" + q;
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                Invoice invoice = new Invoice(rs.getInt("invoice_id"), rs.getString("transaction_date"), rs.getString("payment_method"), rs.getFloat("price"), rs.getInt("staff_id"), rs.getInt("membership_id"));
                invoiceList.add(invoice);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return invoiceList;
    }
    
    public ObservableList getOrder(String q) {
        
        ObservableList<Order> orderList = FXCollections.observableArrayList();
        query = "SELECT * FROM orders" + q;
        rs = dbLink.queryResult(query);
        
        try{
            while(rs.next()){
                Order order = new Order(rs.getInt("order_id"), rs.getInt("menu_id"), rs.getFloat("price"), rs.getInt("quantity"), rs.getFloat("total"), rs.getInt("invoice_id"));
                orderList.add(order);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return orderList;
    }

    public void showInvoice(String q) {
        // Create list by calling getItemList() 
        ObservableList<Invoice> list = getInvoice(q);
        // Set cell value factory of each table's cell
        invoiceId.setCellValueFactory(new PropertyValueFactory<>("invoice_id"));
        transactionDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
        paymentMethod.setCellValueFactory(new PropertyValueFactory<>("payment_method"));
        invoicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        staffId.setCellValueFactory(new PropertyValueFactory<>("staff_id"));
        membershipId.setCellValueFactory(new PropertyValueFactory<>("membership_id"));   
        // Set items in the table
        invoiceTb.setItems(list);
    }
    
    public void showOrder(String q) {
        // Create list by calling getItemList() 
        ObservableList<Order> list = getOrder(q);
        // Set cell value factory of each table's cell
        orderId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        menuId.setCellValueFactory(new PropertyValueFactory<>("menu_id"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        orderInvoiceId.setCellValueFactory(new PropertyValueFactory<>("invoice_id"));   
        // Set items in the table
        orderTb.setItems(list);
    }
    
    public void showChosen(){
        showOrder(" WHERE invoice_id = " + ivcId);
    }

    public void searchItem() {
        showInvoice(" WHERE invoice_id LIKE '%" + invoiceIdSearchField.getText() + "%'");
    }
    
    public void setInvoiceEmpty() {
        cashierIdLabel.setText("-");
        cashierNameLabel.setText("-");
        membershipIdLabel.setText("-");
        membershipNameLabel.setText("-");
    } 
    
    public void setOrderEmpty() {
        menuIdLabel.setText("-");
        menuNameLabel.setText("-");
    }
}
