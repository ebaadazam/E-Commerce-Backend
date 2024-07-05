package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.model.Category;
import com.ebaad.ecommerce.model.Product;
import com.ebaad.ecommerce.repository.CategoryRepository;
import com.ebaad.ecommerce.repository.ProductRepository;
import com.ebaad.ecommerce.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService{

    private final ProductRepository productRepository;
//    private final UserService userService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository,

                                        CategoryRepository categoryRepository){
        this.productRepository=productRepository;
//        this.userService=userService;
        this.categoryRepository=categoryRepository;
    }


    @Override
    public Product createProduct(CreateProductRequest req) {
        // If the category is already present
        Category firstLevel = categoryRepository.findByName(req.getFirstLevelCategory());

        // If null then create it
        if (firstLevel == null){
            Category firstLevelCategory = new Category();
            firstLevelCategory.setName(req.getFirstLevelCategory());
            firstLevelCategory.setLevel(1);
            firstLevel = categoryRepository.save(firstLevelCategory);
        }

        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(), firstLevel.getName());
        if (secondLevel == null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(firstLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(), secondLevel.getName());
        if (thirdLevel == null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        // Setting the values
        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountedPercent(req.getDiscountPercent());
        product.setBrand(req.getBrand());
        product.setImageUrl(req.getImageUrl());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        // Saving the product
        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product has been deleted successfully!";
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product product = findProductById(productId);
        if (req.getQuantity() != 0){
            product.setQuantity(req.getQuantity());
        }
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new ProductException("Product not found with id: " + id);
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return List.of();
    }

    // Important method
    // chat gpt code
    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        // Validate pageNumber and pageSize
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 10;  // Set a default page size

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        // Apply color filtering
        if (colors != null && !colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }

        // Apply stock filtering
        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        // Validate indices
        if (startIndex >= products.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<Product> pageContent = products.subList(startIndex, endIndex);
        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());

        return filteredProducts;
    }



    @Override
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }
}
