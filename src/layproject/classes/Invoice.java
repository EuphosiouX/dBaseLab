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
public class Invoice {
    private Integer invoice_id;
    private String transaction_date;
    private String payment_method;
    private Float price;
    private Integer staff_id;
    private Integer membership_id;

    public Invoice(Integer invoice_id, String transaction_date, String payment_method, Float price, Integer staff_id, Integer membership_id) {
        this.invoice_id = invoice_id;
        this.transaction_date = transaction_date;
        this.payment_method = payment_method;
        this.price = price;
        this.staff_id = staff_id;
        this.membership_id = membership_id;
    }

    public Integer getInvoice_id() {
        return invoice_id;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public Integer getMembership_id() {
        return membership_id;
    }
}
