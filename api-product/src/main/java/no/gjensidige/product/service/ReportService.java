package no.gjensidige.product.service;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReportService {

    @Autowired
    ProductRepository productRepository;

    public FinancialReport generateFinancialReport(){

        List<Product> productList = productRepository.findAll();

        if (productList.isEmpty()){
            throw new NoSuchElementException("Product list is empty");
        }

        Double totalTurnover = productList.stream().mapToDouble(this::getProductTurnover).sum();

        Double totalCost = productList.stream().mapToDouble(this::getProductCost).sum();

        Double totalMargin = totalTurnover - totalCost;

        Product highestMarginProduct = Collections.max(productList, Comparator.comparing(this::getProductMargin));

        Product lowestMarginProduct = Collections.min(productList, Comparator.comparing(this::getProductMargin));

        Product leastSoldProduct = Collections.min(productList, Comparator.comparing(Product::getNumberSold));

        Product mostSoldProduct = Collections.max(productList, Comparator.comparing(Product::getNumberSold));

        FinancialReport report = new FinancialReport();
        report.setLowestMarginProduct(lowestMarginProduct);
        report.setMostSoldProduct(mostSoldProduct);
        report.setTotalTurnover(totalTurnover);
        report.setLeastSoldProduct(leastSoldProduct);
        report.setTotalCost(totalCost);
        report.setTotalMargin(totalMargin);
        report.setHighestMarginProduct(highestMarginProduct);

        return report;
    }

    public Double getProductTurnover(Product product){
        return product.getNumberSold().doubleValue() * product.getUnitPrice();
    }

    public Double getProductCost(Product product){
        return product.getNumberSold().doubleValue() * product.getUnitCost();
    }

    public Double getProductMargin(Product product){
        return getProductTurnover(product) - getProductCost(product);
    }
}
