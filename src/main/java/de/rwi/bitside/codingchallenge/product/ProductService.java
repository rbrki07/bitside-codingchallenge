package de.rwi.bitside.codingchallenge.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
    }

    public Product getProductByCode(String code) {
        return productRepository.findByCode(code).orElseThrow(() -> new ProductNotFoundException());
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        var existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
        existingProduct.setCode(product.getCode());
        existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
