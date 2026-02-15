package no.gjensidige.product.controller;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.service.ReportService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFinancialReport(){
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name ->
        {
            Product p = new Product();
            p.setProductName(name);
            p.setNumberSold(BigInteger.valueOf(200));
            p.setUnitPrice(55.50);
            p.setUnitCost(10.50);
            productList.add(p);
        });

        Product product = productList.get(0);

        Double expectedTotalTurnover =
                productList.stream().mapToDouble(p -> p.getNumberSold().doubleValue() * p.getUnitPrice()).sum();

        Double expectedTotalCost =
                productList.stream().mapToDouble(p -> p.getNumberSold().doubleValue() * p.getUnitCost()).sum();

        Double expectedTotalMargin = expectedTotalTurnover - expectedTotalCost;

        FinancialReport expectedReport = new FinancialReport();
        expectedReport.setHighestMarginProduct(product);
        expectedReport.setLeastSoldProduct(product);
        expectedReport.setMostSoldProduct(product);
        expectedReport.setLowestMarginProduct(product);
        expectedReport.setTotalCost(expectedTotalCost);
        expectedReport.setTotalMargin(expectedTotalMargin);
        expectedReport.setTotalTurnover(expectedTotalTurnover);

        when(reportService.generateFinancialReport()).thenReturn(expectedReport);

        FinancialReport actualReport = reportController.getFinancialReport();

        verify(reportService).generateFinancialReport();

        assertEquals(expectedReport.getCreatedTime(), actualReport.getCreatedTime());
        assertEquals(expectedReport.getHighestMarginProduct(), actualReport.getHighestMarginProduct());
    }
}
