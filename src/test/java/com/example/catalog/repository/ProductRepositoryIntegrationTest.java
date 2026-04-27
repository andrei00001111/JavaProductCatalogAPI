package com.example.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.catalog.dto.product.ProductSearchCriteria;
import com.example.catalog.entity.Category;
import com.example.catalog.entity.Product;
import com.example.catalog.entity.Seller;
import com.example.catalog.specification.ProductSpecification;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @BeforeEach
    void setupData() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        sellerRepository.deleteAll();

        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setDescription("Electronic products");
        electronics = categoryRepository.save(electronics);

        Seller seller = new Seller();
        seller.setExternalId("SELLER-123");
        seller.setName("Seller One");
        seller = sellerRepository.save(seller);

        Product laptop = new Product();
        laptop.setSku("LAP-1");
        laptop.setName("Laptop Pro");
        laptop.setDescription("Fast laptop");
        laptop.setPrice(BigDecimal.valueOf(1200));
        laptop.setStockQuantity(20);
        laptop.setActive(true);
        laptop.setCategory(electronics);
        laptop.setSeller(seller);
        productRepository.save(laptop);

        Product mouse = new Product();
        mouse.setSku("MOU-1");
        mouse.setName("Mouse Basic");
        mouse.setDescription("Wireless mouse");
        mouse.setPrice(BigDecimal.valueOf(25));
        mouse.setStockQuantity(0);
        mouse.setActive(true);
        mouse.setCategory(electronics);
        mouse.setSeller(seller);
        productRepository.save(mouse);
    }

    @Test
    void shouldFilterByQueryPriceAndInStock() {
        ProductSearchCriteria criteria = new ProductSearchCriteria(
                "laptop",
                null,
                null,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(2000),
                true,
                true
        );

        Page<Product> result = productRepository.findAll(
                ProductSpecification.build(criteria),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getSku()).isEqualTo("LAP-1");
    }
}
