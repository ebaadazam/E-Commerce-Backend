package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.model.Product;
import com.ebaad.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req);

    public String deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product req) throws ProductException;

    public Product findProductById(Long id) throws ProductException;

    public List<Product> findProductByCategory(String category);

    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes,
                                       Integer minPrice, Integer maxPrice, Integer minDiscount, String sort,
                                       String stock, Integer pageNumber, Integer pageSize);

    public List<Product> findAllProduct();

}
