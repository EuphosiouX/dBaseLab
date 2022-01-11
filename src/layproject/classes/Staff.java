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
public class Staff {
    private Integer staff_id;
    private String first_name;
    private String last_name;
    private String address;
    private String phone_no;
    private String status;
    private String username;
    private String password;
    private Integer staff_category_id;

    public Staff(Integer staff_id, String first_name, String last_name, String address, String phone_no, String status, String username, String password, Integer staff_category_id) {
        this.staff_id = staff_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.phone_no = phone_no;
        this.status = status;
        this.username = username;
        this.password = password;
        this.staff_category_id = staff_category_id;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Integer getStaff_category_id() {
        return staff_category_id;
    }
}
