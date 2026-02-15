package no.gjensidige.product.service;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.*;

public class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    List<Product> productList;

    @Mock
    ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        openMocks(this);

        // Create a product list
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<String> names = new ArrayList<>(uniqueNames);

        productList = new ArrayList<>();
        for(int i = 0; i<uniqueNames.size(); i++) {
            Product p = new Product();
            p.setProductName(names.get(i));
            p.setNumberSold(BigInteger.valueOf(200L * (i + 1)));
            p.setUnitPrice(55.50 + (i * 10));
            p.setUnitCost(10.50 + (i * 10));
            productList.add(p);
        }
    }

    @Test
    public void generateFinancialReport(){
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

        when(productRepository.findAll()).thenReturn(productList);

        FinancialReport actualReport = reportService.generateFinancialReport();

        verify(productRepository).findAll();

        assertEquals(expectedReport.getCreatedTime(), actualReport.getCreatedTime());
        assertEquals(expectedReport.getHighestMarginProduct(), actualReport.getHighestMarginProduct());
        assertEquals(expectedReport.getLeastSoldProduct(), actualReport.getLeastSoldProduct());
    }

    @Test(expected = NoSuchElementException.class)
    public void generateFinancialReport_WithEmptyProductList_ThrowsException(){
        reportService.generateFinancialReport();
        fail("Did not throw NoSuchElementException");
    }

    @Test
    public void getTotalTurnover(){
        Product product = productList.get(0);

        Double expectedProductTurnover = product.getNumberSold().doubleValue() * product.getUnitPrice();

        Double actualProductTurnover = reportService.getProductTurnover(product);

        assertEquals(expectedProductTurnover, actualProductTurnover);
    }

    @Test
    public void getProductCost(){
        Product product = productList.get(0);

        Double expectedProductCost = product.getNumberSold().doubleValue() * product.getUnitCost();

        Double actualProductCost = reportService.getProductCost(product);

        assertEquals(expectedProductCost, actualProductCost);
    }

    @Test
    public void getProductMargin(){
        Product product = productList.get(0);

        Double expectedProductTurnover = product.getNumberSold().doubleValue() * product.getUnitPrice();
        Double expectedProductCost = product.getNumberSold().doubleValue() * product.getUnitCost();

        Double expectedProductMargin = expectedProductTurnover - expectedProductCost;

        Double actualProductMargin = reportService.getProductMargin(product);

        assertEquals(expectedProductMargin, actualProductMargin);
    }

}
