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
public class Memberships {
    
    private Integer membership_id;
    private String first_name;
    private String last_name;
    private String phone_no;
    private String birth_date;
    private String join_date;
    private Integer points;

    public Memberships(Integer membership_id, String first_name, String last_name, String phone_no, String birth_date, String join_date, Integer points) {
        this.membership_id = membership_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_no = phone_no;
        this.birth_date = birth_date;
        this.join_date = join_date;
        this.points = points;
    }



    public Integer getMembership_id() {
        return membership_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getJoin_date() {
        return join_date;
    }

    public Integer getPoints() {
        return points;
    }
    
    
}
