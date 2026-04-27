package com.example.catalog.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalog.entity.Category;
import com.example.catalog.entity.Product;
import com.example.catalog.entity.Seller;
import com.example.catalog.repository.CategoryRepository;
import com.example.catalog.repository.ProductRepository;
import com.example.catalog.repository.SellerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private Category category;
    private Seller seller;

    @BeforeEach
    void setupData() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        sellerRepository.deleteAll();

        category = new Category();
        category.setName("Electronics");
        category = categoryRepository.save(category);

        seller = new Seller();
        seller.setExternalId("SELLER-1");
        seller.setName("Main Seller");
        seller = sellerRepository.save(seller);

        Product product = new Product();
        product.setSku("SKU-1");
        product.setName("Smartphone X");
        product.setDescription("Flagship phone");
        product.setPrice(BigDecimal.valueOf(899));
        product.setStockQuantity(5);
        product.setActive(true);
        product.setCategory(category);
        product.setSeller(seller);
        productRepository.save(product);
    }

    @Test
    void shouldReturnFilteredProductsWithPagination() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .param("query", "smart")
                        .param("size", "5")
                        .param("sort", "price,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].sku", is("SKU-1")));
    }

    @Test
    void shouldRejectInvalidProductCreateRequest() throws Exception {
        Map<String, Object> payload = Map.of(
                "sku", "",
                "name", "",
                "price", -1,
                "stockQuantity", -5,
                "active", true,
                "categoryId", category.getId(),
                "sellerId", seller.getId()
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }
}
