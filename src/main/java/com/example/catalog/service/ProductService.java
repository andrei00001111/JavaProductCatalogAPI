package com.example.catalog.service;

import com.example.catalog.dto.product.ProductRequest;
import com.example.catalog.dto.product.ProductResponse;
import com.example.catalog.dto.product.ProductSearchCriteria;
import com.example.catalog.entity.Category;
import com.example.catalog.entity.Product;
import com.example.catalog.entity.Seller;
import com.example.catalog.exception.ResourceNotFoundException;
import com.example.catalog.mapper.ProductMapper;
import com.example.catalog.repository.CategoryRepository;
import com.example.catalog.repository.ProductRepository;
import com.example.catalog.repository.SellerRepository;
import com.example.catalog.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SellerRepository sellerRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return ProductMapper.toResponse(findProduct(id));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(ProductSearchCriteria criteria, Pageable pageable) {
        if (criteria.minPrice() != null && criteria.maxPrice() != null
                && criteria.minPrice().compareTo(criteria.maxPrice()) > 0) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
        }

        return productRepository.findAll(ProductSpecification.build(criteria), pageable)
                .map(ProductMapper::toResponse);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        productRepository.findBySku(request.sku()).ifPresent(existing -> {
            throw new IllegalArgumentException("Product SKU already exists");
        });

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));

        Seller seller = sellerRepository.findById(request.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + request.sellerId()));

        Product product = new Product();
        ProductMapper.apply(request, product);
        product.setCategory(category);
        product.setSeller(seller);

        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProduct(id);

        productRepository.findBySku(request.sku())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Product SKU already exists");
                });

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));

        Seller seller = sellerRepository.findById(request.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + request.sellerId()));

        ProductMapper.apply(request, product);
        product.setCategory(category);
        product.setSeller(seller);

        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }
}
