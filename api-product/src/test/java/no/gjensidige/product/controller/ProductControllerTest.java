package no.gjensidige.product.controller;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;


    @Mock
    private ProductService productService;

    @Before
    public void init() {
        openMocks(this);
    }

    @Test
    public void getProducts() {
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name ->
        {
            Product p = new Product();
            p.setProductName(name);
            productList.add(p);
        });


        when(productService.getAllProducts()).thenReturn(productList);

        List<Product> productList1 = productController.getProducts();

        verify(productService).getAllProducts();

        assertEquals(3, productList1.size());

    }

    @Test
    public void getProduct() {
        Product p = new Product();
        p.setId(1l);

        when(productService.getProduct(1l)).thenReturn(p);

        Product product = productController.getProduct(1l);

        verify(productService).getProduct(1l);
        assertEquals(1l, product.getId().longValue());
    }

    @Test
    public void createProduct() {
        Long id = 1L;

        ProductDTO inputProductDTO = new ProductDTO();
        inputProductDTO.setId(id);
        inputProductDTO.setCategory("Hardware");
        inputProductDTO.setProductName("Seagate Baracuda 500GB");
        inputProductDTO.setNumbersold(BigInteger.valueOf(200));
        inputProductDTO.setUnitPrice(55.50);

        Product expectedProduct = new Product();
        expectedProduct.setId(id);
        expectedProduct.setCategory("Hardware");
        expectedProduct.setProductName("Seagate Baracuda 500GB");
        expectedProduct.setNumberSold(BigInteger.valueOf(200));
        expectedProduct.setUnitPrice(55.50);

        when(productService.createProduct(inputProductDTO)).thenReturn(expectedProduct);

        Product actualProduct = productController.createProduct(inputProductDTO);

        verify(productService).createProduct(inputProductDTO);
        assertEquals(expectedProduct.getId(), actualProduct.getId());
        assertEquals(expectedProduct.getProductName(), actualProduct.getProductName());
    }

    @Test
    public void updateProduct() {
        Long id = 1L;

        ProductDTO inputProductDTO = new ProductDTO();
        inputProductDTO.setId(id);
        inputProductDTO.setCategory("Hardware 2.0");
        inputProductDTO.setProductName("Seagate Baracuda 500GB");
        inputProductDTO.setNumbersold(BigInteger.valueOf(300));
        inputProductDTO.setUnitPrice(65.50);

        Product expectedUpdatedProduct = new Product();
        expectedUpdatedProduct.setId(id);
        expectedUpdatedProduct.setCategory("Hardware 2.0");
        expectedUpdatedProduct.setProductName("Seagate Baracuda 500GB");
        expectedUpdatedProduct.setNumberSold(BigInteger.valueOf(300));
        expectedUpdatedProduct.setUnitPrice(65.50);

        when(productService.updateProduct(id, inputProductDTO)).thenReturn(expectedUpdatedProduct);

        Product actualUpdatedProduct = productController.updateProduct(id, inputProductDTO);

        verify(productService).updateProduct(id, inputProductDTO);
        assertEquals(expectedUpdatedProduct.getCategory(), actualUpdatedProduct.getCategory());
        assertEquals(expectedUpdatedProduct.getProductName(), actualUpdatedProduct.getProductName());
    }

    @Test
    public void deleteProduct() {

        Product p = new Product();
        p.setId(1l);

        when(productService.deleteProduct(1l)).thenReturn(p);

        Product product = productController.deleteProduct(1l);

        verify(productService).deleteProduct(1l);

        assertEquals(1l, product.getId().longValue());

    }
}