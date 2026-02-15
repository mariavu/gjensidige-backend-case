package no.gjensidige.product.entity;

import jakarta.persistence.*;
import java.math.BigInteger;

/**
 *  Product entity class.
 *
 *  This class is used for storing Products to the database.
 */

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String productName;
    String category;
    String imageLink;
    Double unitCost;
    Double unitPrice;
    BigInteger numberSold;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productname) {
        this.productName = productname;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigInteger getNumberSold() {
        return numberSold;
    }

    public void setNumberSold(BigInteger numberSold) {
        this.numberSold = numberSold;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
