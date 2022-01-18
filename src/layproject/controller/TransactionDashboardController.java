/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
public class TransactionDashboardController implements Initializable, CheckTextField, ItemQuery {
    
    private final JDBConnection dbLink = new JDBConnection("restaurant");
    private final Connection con = dbLink.getConnection();
    private final FxmlLoader loader = new FxmlLoader(); 
    
    private String query;
    private ResultSet rs;
    private Stage stage;
    private String gid;
    private String gname;
    private String mid;
    private String mfname;
    private String mlname;
    private String mphoneNo;
    private String mbdate;
    private String mjdate;
    private String mpoints;
    private Integer i = 0;
    private Double total = 0.0;
    private Double grandTotal = 0.0;
    private Integer points = 0;
    private Integer invoice_id = 0;
    private Integer order_id = 0;
    private Integer initial_order_id = 0;
    private boolean checkout = false;

    @FXML
    private Label cashierIdLabel;
    @FXML
    private Label cashierNameLabel;
    @FXML
    private Label membershipIdLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label phoneNoLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label joinDateLabel;
    @FXML
    private Label pointsLabel;
    @FXML
    private JFXButton backBtn;
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
    private JFXTextField menuNameSearchField;
    @FXML
    private Label menuIdLabel;
    @FXML
    private Label menuNameLabel; 
    @FXML
    private Label priceLabel;
    @FXML
    private Label stockLabel;
    @FXML
    private JFXTextField qtyField;
    @FXML
    private JFXButton searchBtn;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton voidBtn;
    @FXML
    private JFXButton clearBtn;
    @FXML
    private TextArea receipt;
    @FXML
    private JFXComboBox<String> paymentComb;
    @FXML
    private JFXTextField cashField;
    @FXML
    private JFXButton checkoutBtn;
    @FXML
    private JFXButton finishBtn;
    @FXML
    private Label receiveLabel;
    @FXML
    private Label amountField;
    @FXML
    private JFXButton historyBtn;
   
    public void getCashier(String id, String name){
        cashierIdLabel.setText(id);
        cashierNameLabel.setText(name);
        gid = id;
        gname = name;
    }
    
    public void getMember(String id, String fname, String lname, String phoneNo, String bDate, String jDate, String points){
        membershipIdLabel.setText(id);
        firstNameLabel.setText(fname);
        lastNameLabel.setText(lname);
        phoneNoLabel.setText(phoneNo);
        birthDateLabel.setText(bDate);
        joinDateLabel.setText(jDate);
        pointsLabel.setText(points);
        mid = id;
        mfname = fname;
        mlname = lname;
        mphoneNo = phoneNo;
        mbdate = bDate;
        mjdate = jDate; 
        mpoints = points;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setEmpty();
        ObservableList<String> categoryList = FXCollections.observableArrayList("Debit Card", "Credit Card", "Cash");
        paymentComb.setItems(categoryList);
        
        showItemList("");     
    }
    
    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException, SQLException {
        if (i > 0) {
            con.rollback();
            con.setAutoCommit(true);
            resetIncrement();
            con.setAutoCommit(false);
        }
        FXMLLoader ldr = loader.loadStage("/layproject/fxml/askMembership.fxml");
        Parent root = ldr.load();
        
        AskMembershipController controller = ldr.getController();
        
        controller.getCashier(gid, gname);
        
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();  
    }

    @FXML
    private void menuTableClicked(MouseEvent event) {
        Menu menu = menuTb.getSelectionModel().getSelectedItem();
        menuIdLabel.setText("" + menu.getMenu_id());
        menuNameLabel.setText(menu.getMenu_name());
        priceLabel.setText("" + menu.getPrice());
        stockLabel.setText("" + menu.getStock());
    }

    @FXML
    private void searchButtonClicked(ActionEvent event) {
        searchItem();
    }

    @FXML
    private void addButtonClicked(ActionEvent event) throws SQLException {
        // Check if textFieldIsEmpty() == true
        if(textFieldIsEmpty()){
            // Throw alert
            loader.showAlert("Fill in required data!!");
        }
        else if (!isNum(qtyField.getText())){
            loader.showAlert("Enter valid input!!");
        }
        else if (belowZero(qtyField.getText())){
            loader.showAlert("Number can't be 0 / negative!!");
        }
        else if (Integer.parseInt(qtyField.getText()) > Integer.parseInt(stockLabel.getText())){
            loader.showAlert("Insufficient stock!!");
        }
        else{
            con.setAutoCommit(false);
            if (i==0){
                insertItem();
                receipt.setText(receipt.getText() + "========WELCOME TO RESTOWLRANT========\n");
                receipt.setText(receipt.getText() + "===================================\n");
                receipt.setText(receipt.getText() + "\tCashier\t\t       : " + gname + "\n");
                if (mid == null){
                    receipt.setText(receipt.getText() + "\tMember\t\t       : -\n");
                }
                else{
                    receipt.setText(receipt.getText() + "\tMember\t\t       : " + mfname + " " + mlname + "\n");
                }
                receipt.setText(receipt.getText() + "\tDate\t\t\t       : " + LocalDate.now() + "\n");
                receipt.setText(receipt.getText() + "===================================\n");
                updateItem();
                i++;
//                Double total = Double.parseDouble(priceLabel.getText()) * Double.parseDouble(qtyField.getText());
//                total = Math.round(total*100)/100.0;
//                grandTotal = grandTotal + total;
                points = grandTotal.intValue()/10;
                receipt.setText(receipt.getText() + "\tNo\t\t               : " + i + "\n");
                receipt.setText(receipt.getText() + "\tMenu Name(id)      : " + menuNameLabel.getText() + "(" + menuIdLabel.getText() + ")\n");
                receipt.setText(receipt.getText() + "\tPrice\t\t\t       : " + priceLabel.getText() + "\n");
                receipt.setText(receipt.getText() + "\tQty\t\t               : " + qtyField.getText() + "\n");
                receipt.setText(receipt.getText() + "\tTotal\t\t       : " + total + "\n");
                receipt.setText(receipt.getText() + "===================================\n"); 
            }
            else{
                updateItem();
                i++;
//                Double total = Double.parseDouble(priceLabel.getText()) * Double.parseDouble(qtyField.getText());
//                total = Math.round(total*100)/100.0;
//                grandTotal = grandTotal + total;
                points = grandTotal.intValue()/10;
                receipt.setText(receipt.getText() + "\tNo\t\t               : " + i + "\n");
                receipt.setText(receipt.getText() + "\tMenu Name(id)      : " + menuNameLabel.getText() + "(" + menuIdLabel.getText() + ")\n");
                receipt.setText(receipt.getText() + "\tPrice\t\t\t       : " + priceLabel.getText() + "\n");
                receipt.setText(receipt.getText() + "\tQty\t\t               : " + qtyField.getText() + "\n");
                receipt.setText(receipt.getText() + "\tTotal\t\t       : " + total + "\n");
                receipt.setText(receipt.getText() + "===================================\n");  
            }   
        }
        setEmpty();
        showPoints();
        showTotal();
    }

    @FXML
    private void voidButtonClicked(ActionEvent event) throws SQLException {
        if(i < 1){
            loader.showAlert("No transaction!!");
        }
        else if(textFieldIsEmpty()){
            // Throw alert
            loader.showAlert("Fill in required data!!");
        }
        else if (!isNum(qtyField.getText())){
            loader.showAlert("Enter valid input!!");
        }
        else if (belowZero(qtyField.getText())){
            loader.showAlert("Number can't be 0 / negative!!");
        }
        else if (isExist() == false){
            loader.showAlert("Items not found in order!!");
        }
        else if (Integer.parseInt(qtyField.getText()) > basketQty()){
            loader.showAlert("You can't void more than what you ordered!!");
        }
        else{
            con.setAutoCommit(false);
            deleteItem();
            i++;
//            Double total = Double.parseDouble(priceLabel.getText()) * -Double.parseDouble(qtyField.getText());
//            total = Math.round(total*100)/100.0;
//            grandTotal = grandTotal + total;
            points = grandTotal.intValue()/10;
            receipt.setText(receipt.getText() + "\tNo\t\t               : VOID\n");
            receipt.setText(receipt.getText() + "\tMenu Name(id)      : " + menuNameLabel.getText() + "(" + menuIdLabel.getText() + ")\n");
            receipt.setText(receipt.getText() + "\tPrice\t\t\t       : " + priceLabel.getText() + "\n");
            receipt.setText(receipt.getText() + "\tQty\t\t               : " + qtyField.getText() + "\n");
            receipt.setText(receipt.getText() + "\tTotal\t\t       : " + total + "\n");
            receipt.setText(receipt.getText() + "===================================\n");
        }
        setEmpty();
        showPoints();
        showTotal();
    }

    @FXML
    private void clearButtonClicked(ActionEvent event) throws SQLException {
        receipt.setText("");
        con.rollback();
        con.setAutoCommit(true);
        resetIncrement();
        con.setAutoCommit(false);
        i = 0;
        total = 0.0;
        grandTotal = 0.0;
        points = 0;
        invoice_id = 0;
        order_id = 0;
        initial_order_id = 0;
        checkout = false;
        showItemList("");
        setEmpty();
        showPoints();
        showTotal();
    }
    
        @FXML
    private void checkoutButtonClicked(ActionEvent event) {
        Double cash = 0.0;
        Double change = 0.0;
        if(i < 1){
            loader.showAlert("No transaction!!");
        }
        else if(paymentComb.getSelectionModel().getSelectedItem() == null){
            loader.showAlert("Select payment method!!");
        }
        else{
            checkout = true;
            if(paymentComb.getSelectionModel().getSelectedItem() == "Debit Card" || paymentComb.getSelectionModel().getSelectedItem() == "Credit Card"){
                cash = grandTotal;
                change = cash - grandTotal;
                points = grandTotal.intValue()/10;
                receipt.setText(receipt.getText() + "\tPayment\t\t       : " + paymentComb.getSelectionModel().getSelectedItem() + "\n");
                receipt.setText(receipt.getText() + "\tGrand Total\t       : " + grandTotal + "\n");
                receipt.setText(receipt.getText() + "\tCash\t\t\t       : " + cash + "\n");
                receipt.setText(receipt.getText() + "\tChange\t\t       : " + change + "\n");
                receipt.setText(receipt.getText() + "\tPoints\t\t       : " + points + "\n");
                receipt.setText(receipt.getText() + "===================================\n");
                showPoints();
                showTotal();
            }
            else{
                if (!isNum(cashField.getText())){
                    loader.showAlert("Enter valid input!!");
                }
                else if (belowZero(cashField.getText())){
                    loader.showAlert("Number can't be negative!!");
                }
                else if (grandTotal > Integer.parseInt(cashField.getText())){
                    loader.showAlert("Insufficient cash!!");
                }
                else{
                    cash = Double.parseDouble(cashField.getText());
                    change = cash - grandTotal;
                    change = Math.round(change*100)/100.0;
                    points = grandTotal.intValue()/10;
                    receipt.setText(receipt.getText() + "\tPayment\t\t       : " + paymentComb.getSelectionModel().getSelectedItem() + "\n");
                    receipt.setText(receipt.getText() + "\tGrand Total\t       : " + grandTotal + "\n");
                    receipt.setText(receipt.getText() + "\tCash\t\t\t       : " + cash + "\n");
                    receipt.setText(receipt.getText() + "\tChange\t\t       : " + change + "\n");
                    receipt.setText(receipt.getText() + "\tPoints\t\t       : " + points + "\n");
                    receipt.setText(receipt.getText() + "===================================\n");
                    showPoints();
                    showTotal();
                }
            }
        }   
    }
    
    @FXML
    private void historyButtonClicked(ActionEvent event) throws IOException {
        loader.showStage("/layproject/fxml/invoiceOrderDashboard.fxml");
    }

    @FXML
    private void finishButtonClicked(ActionEvent event) throws SQLException {
        if(i < 1){
            loader.showAlert("No transaction!!");
        }
        else if (checkout == false){
            loader.showAlert("Checkout first!!");
        }
        else{
     //       insertItem();
            updateInvoice();
            if (mid != null){
                addPoints();
            }
            con.commit();
            i = 0;
            total = 0.0;
            grandTotal = 0.0;
            points = 0;
            invoice_id = 0;
            order_id = 0;
            initial_order_id = 0;
            checkout = false;
            receipt.setText("");
            loader.showAlert("Transaction completed!!"); 
        }
        
    }

    @Override
    public boolean textFieldIsEmpty() {
        return (menuIdLabel.getText().equals("Id"));
    }
    
    private boolean belowZero(String text){
        return (Integer.parseInt(text) <= 0);
    }
    
    private boolean isNum(String text){
        try{
            Float.parseFloat(text);
        }
        catch(NumberFormatException e){
            return false;
        }
        catch(NullPointerException e) {
            return false;
        }
        return true;
    }
    
    private void showPoints(){
        receiveLabel.setText("Points received: " + points);
    }
    
    private void showTotal(){
        amountField.setText("Total Amount: " + grandTotal);
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
        if (mid == null){
            query = "INSERT INTO invoice VALUES (NULL, CURRENT_DATE(), '-', 0, " + gid + ", NULL)";
        }
        else{
            query = "INSERT INTO invoice VALUES (NULL, CURRENT_DATE(), '-', 0, " + gid + ", " + mid + ")";
        }
        dbLink.executeQuery(query);
        
        // Execute the query by calling executeQuery() from JDBConnection
        // Show the table by calling showItemList() 
        query = "SELECT invoice_id FROM invoice ORDER BY invoice_id DESC LIMIT 1";
        
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                invoice_id = rs.getInt("invoice_id");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                initial_order_id = rs.getInt("order_id");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void updateItem() {
        
        query = "INSERT INTO orders VALUES(NULL, " + menuIdLabel.getText() + "," + priceLabel.getText() + "," + qtyField.getText() + ", price*quantity, " + invoice_id + ")";
        dbLink.executeQuery(query);
        
        query = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                order_id = rs.getInt("order_id");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "SELECT total FROM orders WHERE order_id = " + order_id;
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                total = rs.getDouble("total");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "SELECT SUM(total) AS grand_total FROM orders WHERE invoice_id = " + invoice_id;
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                grandTotal = rs.getDouble("grand_total");
                grandTotal = Math.round(grandTotal*100)/100.0;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "UPDATE menu SET stock = stock - " + qtyField.getText() + " WHERE menu_id = " + menuIdLabel.getText();
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }

    @Override
    public void deleteItem() {
        
        query = "INSERT INTO orders VALUES(NULL, " + menuIdLabel.getText() + "," + priceLabel.getText() + ", -" + qtyField.getText() + ", price*quantity, " + invoice_id + ")";
        dbLink.executeQuery(query);
        
        query = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                order_id = rs.getInt("order_id");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "SELECT total FROM orders WHERE order_id = " + order_id;
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                total = rs.getDouble("total");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "SELECT SUM(total) AS grand_total FROM orders WHERE invoice_id = " + invoice_id;
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                grandTotal = rs.getDouble("grand_total");
                grandTotal = Math.round(grandTotal*100)/100.0;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        query = "UPDATE menu SET stock = stock + " + qtyField.getText() + " WHERE menu_id = " + menuIdLabel.getText();
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
    public boolean isExist() {
        query = "SELECT count(1) FROM orders WHERE menu_id = " + menuIdLabel.getText() + " AND invoice_id = " + invoice_id;
        rs = dbLink.queryResult(query);
        
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
    
    private void addPoints(){
        query = "UPDATE memberships SET  points = points + " + points + " WHERE membership_id = " + mid;
        // Execute the query by calling executeQuery() from JDBConnection
        dbLink.executeQuery(query);
        // Show the table by calling showItemList() 
        showItemList("");
    }
    
    private Integer basketQty(){
        Integer qty = 0;
        query = "SELECT SUM(quantity) AS qty FROM orders WHERE menu_id = " + menuIdLabel.getText() + " AND invoice_id = " + invoice_id;
        rs = dbLink.queryResult(query);
        try{
            while(rs.next()){
                qty = rs.getInt("qty");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        return qty;
    }
    
    private void updateInvoice(){
        query = "UPDATE invoice SET payment_method = '" + paymentComb.getSelectionModel().getSelectedItem() + "', price = " + grandTotal + " WHERE invoice_id = " + invoice_id;
        dbLink.executeQuery(query);
    }
    
    private void resetIncrement(){
        query = "ALTER TABLE orders AUTO_INCREMENT = " + (initial_order_id + 1);
        dbLink.executeQuery(query);
        query = "ALTER TABLE invoice AUTO_INCREMENT = " + invoice_id;
        dbLink.executeQuery(query);
    }
    
    @Override
    public void setEmpty() {
        menuIdLabel.setText("Id");
        menuNameLabel.setText("Menu Name");
        priceLabel.setText("Price");
        stockLabel.setText("Stock");
        qtyField.setText("0");
    }
}