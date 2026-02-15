package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class ProductServiceTest {

    public ModelMapper mm = new ModelMapper();

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ModelMapper modelMapper;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
    }

    @Test
    public void createProduct(){
        ProductDTO inputProductDTO = new ProductDTO();
        inputProductDTO.setCategory("Hardware");
        inputProductDTO.setProductName("Seagate Baracuda 500GB");
        inputProductDTO.setNumbersold(BigInteger.valueOf(200));
        inputProductDTO.setUnitPrice(55.50);

        Product expectedProduct = mm.map(inputProductDTO, Product.class);

        when(modelMapper.map(inputProductDTO, Product.class)).thenReturn(expectedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        // Should return the Product of the dto
        Product actualProduct = productService.createProduct(inputProductDTO);

        verify(productRepository).save(actualProduct);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getId(), actualProduct.getId());
        assertEquals(expectedProduct, actualProduct);
    }


    @Test
    public void updateProduct(){
        Long id = 1L;

        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setCategory("Hardware");
        existingProduct.setProductName("Seagate Baracuda 500GB");
        existingProduct.setNumberSold(BigInteger.valueOf(200));
        existingProduct.setUnitPrice(55.50);

        ProductDTO inputProductDTO = new ProductDTO();
        inputProductDTO.setId(id);
        inputProductDTO.setCategory("Hardware 2.0");
        inputProductDTO.setProductName("Seagate Baracuda 500GB");
        inputProductDTO.setNumbersold(BigInteger.valueOf(300));
        inputProductDTO.setUnitPrice(65.50);

        // Mock mapping of dto to existing product
        mm.map(inputProductDTO, existingProduct);

        // If mapping is done correctly, the updated product should have the new fields
        Product expectedProduct = mm.map(inputProductDTO, Product.class);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(modelMapper.map(inputProductDTO, Product.class)).thenReturn(expectedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product actualProduct = productService.updateProduct(id, inputProductDTO);

        verify(productRepository).findById(id);
        verify(productRepository).save(actualProduct);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getCategory(), actualProduct.getCategory());
        assertEquals(expectedProduct.getNumberSold(), actualProduct.getNumberSold());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateProduct_WithNullDTO_ThrowsException(){
        productService.updateProduct(1L, null);
        fail("Did not throw IllegalArgumentException");
    }

    @Test(expected = ProductNotFoundException.class)
    public void updateProduct_NotFound_ThrowsException(){
        ProductDTO inputProductDTO = new ProductDTO();
        inputProductDTO.setId(1L);
        Optional<Product> op = Optional.empty();

        when(productRepository.findById(anyLong())).thenReturn(op);

        productService.updateProduct(1L, inputProductDTO);

        verify(productRepository).findById(1L);
        fail("Did not throw ProductNotFoundException");
    }

    @Test
    public void getAllProducts() {

        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name ->
        {
            Product p = new Product();
            p.setProductName(name);
            productList.add(p);
        });


        when(productRepository.findAll()).thenReturn(productList);

        List<Product> productList1 = productService.getAllProducts();

        verify(productRepository).findAll();

        assertEquals(3, productList1.size());

    }

    @Test
    public void getProduct() {
        Product p = new Product();
        p.setId(1L);
        Optional<Product> op = Optional.of(p);


        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.getProduct(1l);

        assertEquals(p,product);
    }

    @Test
    public void deleteProduct() {
        Product p = new Product();
        p.setId(1L);
        Optional<Product> op = Optional.of(p);
        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.deleteProduct(1l);
        verify(productRepository).delete(p);

        assertEquals(p,product);
    }


    @Test(expected = ProductNotFoundException.class)
    public void deleteProductWithException() {
        Optional<Product> op = Optional.empty();

        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.deleteProduct(10l);

        verify(productRepository).findById(10l);
        fail("Didn't throw not found exception");
    }

    @Test
    public void convertToDTO() {
        Product product = new Product();
        product.setCategory("Hardware");
        product.setProductName("Seagate Baracuda 500GB");
        product.setNumberSold(BigInteger.valueOf(200));
        product.setUnitPrice(55.50);

        when(modelMapper.map(product, ProductDTO.class)).thenReturn(mm.map(product,ProductDTO.class));
        ProductDTO productDTO = productService.convertToDTO(product);
    }

    @Test
    public void convertToEntity() {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("Hardware");
        productDTO.setProductName("Seagate Baracuda 500GB");
        productDTO.setNumbersold(BigInteger.valueOf(200));
        productDTO.setUnitPrice(55.50);

        when(modelMapper.map(productDTO,Product.class)).thenReturn(mm.map(productDTO,Product.class));
        Product product = productService.convertToEntity(productDTO);

        assertEquals(product.getProductName(),productDTO.getProductName());
        assertEquals(product.getNumberSold(),productDTO.getNumberSold());
        assertEquals(product.getCategory(),productDTO.getCategory());

    }
}