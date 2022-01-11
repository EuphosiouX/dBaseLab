// interfaces package
package layproject.interfaces;

// Importing required module, libary, and package
import javafx.collections.ObservableList;

// ItemQuery interface
public interface ItemQuery {
    // Declare methods
    public ObservableList getItemList(String q);
    public void showItemList(String q);
    public void insertItem();
    public void updateItem();
    public void deleteItem();
    public void searchItem();
    public boolean isExist();
}