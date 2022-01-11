/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject.classes;

/**
 *
 * @author Michael Christopher
 */
public class Menu {
    
    private Integer menu_id;
    private String menu_name;
    private Float price;
    private Integer stock;
    private String category;

    public Menu(Integer menu_id, String menu_name, Float price, Integer stock, String category) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public Integer getMenu_id() {
        return menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }
    
     public Float getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }   
}
