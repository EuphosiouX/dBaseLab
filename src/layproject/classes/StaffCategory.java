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
public class StaffCategory {
    private Integer staff_category_id;
    private String category_name;
    private Float hourly_salary;
    private String working_days;
    private String working_hours;

    public StaffCategory(Integer staff_category_id, String category_name, Float hourly_salary, String working_days, String working_hours) {
        this.staff_category_id = staff_category_id;
        this.category_name = category_name;
        this.hourly_salary = hourly_salary;
        this.working_days = working_days;
        this.working_hours = working_hours;
    }

    public Integer getStaff_category_id() {
        return staff_category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public Float getHourly_salary() {
        return hourly_salary;
    }

    public String getWorking_days() {
        return working_days;
    }

    public String getWorking_hours() {
        return working_hours;
    }
    
}
