package no.gjensidige.product.controller;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.service.ReportService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @Before
    public void init() {
        openMocks(this);
    }

    @Test
    public void getFinancialReport(){
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<String> names = new ArrayList<>(uniqueNames);
        List<Product> productList = new ArrayList<>();
        for(int i = 0; i<uniqueNames.size(); i++) {
            Product p = new Product();
            p.setProductName(names.get(i));
            p.setNumberSold(BigInteger.valueOf(200L * (i + 1)));
            p.setUnitPrice(55.50 + (i * 10));
            p.setUnitCost(10.50 + (i * 10));
            productList.add(p);
        }

        Product leastSoldProduct = productList.get(0);
        Product mostSoldProduct = productList.get(2);

        Double expectedTotalTurnover =
                productList.stream().mapToDouble(p -> p.getNumberSold().doubleValue() * p.getUnitPrice()).sum();

        Double expectedTotalCost =
                productList.stream().mapToDouble(p -> p.getNumberSold().doubleValue() * p.getUnitCost()).sum();

        Double expectedTotalMargin = expectedTotalTurnover - expectedTotalCost;

        FinancialReport expectedReport = new FinancialReport();
        expectedReport.setHighestMarginProduct(mostSoldProduct);
        expectedReport.setLeastSoldProduct(leastSoldProduct);
        expectedReport.setMostSoldProduct(mostSoldProduct);
        expectedReport.setLowestMarginProduct(leastSoldProduct);
        expectedReport.setTotalCost(expectedTotalCost);
        expectedReport.setTotalMargin(expectedTotalMargin);
        expectedReport.setTotalTurnover(expectedTotalTurnover);

        when(reportService.generateFinancialReport()).thenReturn(expectedReport);

        FinancialReport actualReport = reportController.getFinancialReport();

        verify(reportService).generateFinancialReport();

        assertEquals(expectedReport.getCreatedTime(), actualReport.getCreatedTime());
        assertEquals(expectedReport.getHighestMarginProduct(), actualReport.getHighestMarginProduct());
        assertEquals(expectedReport.getLeastSoldProduct(), actualReport.getLeastSoldProduct());
    }
}
