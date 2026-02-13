package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ProductService
 *
 * Class responsible of data manipulation between dto and entity
 *
 *
 */

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProduct(Long id) {

        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    }

    public Product deleteProduct(Long id) {

        Product product  = getProduct(id);

        productRepository.delete(product);

        return product;
    }


    public Product createProduct(ProductDTO inputProduct) {

        Product product = convertToEntity(inputProduct);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDTO inputProduct) {
        // Check if inputProduct is null
        if(inputProduct == null ){
            throw new IllegalArgumentException("ProductDTO cannot be null");
        }

        // Map new fields from dto to the existing product
        Product existingProduct = getProduct(id);
        modelMapper.map(inputProduct, existingProduct);
        return productRepository.save(existingProduct);
    }


    public ProductDTO convertToDTO(Product product) {

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    }

    public Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        return product;

    }


}
