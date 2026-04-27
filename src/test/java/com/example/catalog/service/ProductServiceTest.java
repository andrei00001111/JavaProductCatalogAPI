package com.example.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.catalog.dto.product.ProductRequest;
import com.example.catalog.dto.product.ProductResponse;
import com.example.catalog.dto.product.ProductSearchCriteria;
import com.example.catalog.entity.Category;
import com.example.catalog.entity.Product;
import com.example.catalog.entity.Seller;
import com.example.catalog.repository.CategoryRepository;
import com.example.catalog.repository.ProductRepository;
import com.example.catalog.repository.SellerRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Seller seller;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        seller = new Seller();
        seller.setId(10L);
        seller.setExternalId("SELLER-001");
        seller.setName("BestSeller BV");
    }

    @Test
    void shouldRejectSearchWhenMinPriceIsGreaterThanMaxPrice() {
        ProductSearchCriteria criteria = new ProductSearchCriteria(
                "laptop",
                1L,
                10L,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(100),
                true,
                true
        );

        assertThatThrownBy(() -> productService.search(criteria, PageRequest.of(0, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("minPrice");
    }

    @Test
    void shouldCreateProductSuccessfully() {
        ProductRequest request = new ProductRequest(
                "SKU-123",
                "Gaming Laptop",
                "High-end laptop",
                BigDecimal.valueOf(1499.99),
                25,
                true,
                1L,
                10L
        );

        when(productRepository.findBySku("SKU-123")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(sellerRepository.findById(10L)).thenReturn(Optional.of(seller));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            saved.setId(50L);
            return saved;
        });

        ProductResponse response = productService.create(request);

        assertThat(response.id()).isEqualTo(50L);
        assertThat(response.sku()).isEqualTo("SKU-123");
        assertThat(response.categoryName()).isEqualTo("Electronics");
        assertThat(response.sellerName()).isEqualTo("BestSeller BV");
    }

    @Test
    void shouldReturnPagedSearchResults() {
        Product product = new Product();
        product.setId(99L);
        product.setSku("SKU-XYZ");
        product.setName("Wireless Mouse");
        product.setDescription("Ergonomic mouse");
        product.setPrice(BigDecimal.valueOf(39.99));
        product.setStockQuantity(50);
        product.setActive(true);
        product.setCategory(category);
        product.setSeller(seller);

        Page<Product> page = new PageImpl<>(java.util.List.of(product));
        when(productRepository.findAll(anySpecification(), any(Pageable.class)))
                .thenReturn(page);

        ProductSearchCriteria criteria = new ProductSearchCriteria(
                "mouse", null, null, null, null, true, true
        );

        Page<ProductResponse> result = productService.search(criteria, PageRequest.of(0, 20));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Wireless Mouse");
    }

    @SuppressWarnings("unchecked")
    private static Specification<Product> anySpecification() {
        return (Specification<Product>) any(Specification.class);
    }
}
