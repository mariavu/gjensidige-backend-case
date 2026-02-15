package no.gjensidige.product.model;

import no.gjensidige.product.entity.Product;
import java.sql.Timestamp;

/**
 *  FinancialReport
 *
 *  The model we would like to fill with data in exercise 2
 *
 */

public class FinancialReport {

    Timestamp created;
    Product highestMarginProduct;
    Product lowestMarginProduct;
    Product mostSoldProduct;
    Product leastSoldProduct;
    Double totalTurnover;
    Double totalCost;
    Double totalMargin;

    public Timestamp getCreatedTime() {
        return this.created = new Timestamp(System.currentTimeMillis());
    }

    public Product getHighestMarginProduct() {
        return highestMarginProduct;
    }

    public void setHighestMarginProduct(Product highestMarginProduct) {
        this.highestMarginProduct = highestMarginProduct;
    }

    public Product getLowestMarginProduct() {
        return lowestMarginProduct;
    }

    public void setLowestMarginProduct(Product lowestMarginProduct) {
        this.lowestMarginProduct = lowestMarginProduct;
    }

    public Product getMostSoldProduct() {
        return mostSoldProduct;
    }

    public void setMostSoldProduct(Product mostSoldProduct) {
        this.mostSoldProduct = mostSoldProduct;
    }

    public Double getTotalTurnover() {
        return totalTurnover;
    }

    public Product getLeastSoldProduct(){return leastSoldProduct;}

    public void setLeastSoldProduct(Product leastSoldProduct){this.leastSoldProduct = leastSoldProduct;}

    public void setTotalTurnover(Double totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getTotalMargin() {
        return totalMargin;
    }

    public void setTotalMargin(Double totalMargin) {
        this.totalMargin = totalMargin;
    }
}
