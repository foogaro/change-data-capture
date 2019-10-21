package com.foogaro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date checkin;
    private String code;
    private String description;
    private long instock;
    private double price;
    private Status status;

    public Product() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCheckin() {
        return checkin;
    }

    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getInstock() {
        return instock;
    }

    public void setInstock(long instock) {
        this.instock = instock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Product{");
        sb.append("id=").append(id);
        sb.append(", checkin=").append(checkin);
        sb.append(", code='").append(code).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", instock=").append(instock);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
