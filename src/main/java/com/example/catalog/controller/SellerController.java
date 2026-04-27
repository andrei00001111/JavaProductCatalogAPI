package com.example.catalog.controller;

import com.example.catalog.dto.seller.SellerRequest;
import com.example.catalog.dto.seller.SellerResponse;
import com.example.catalog.service.SellerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    public List<SellerResponse> getAll() {
        return sellerService.getAll();
    }

    @GetMapping("/{id}")
    public SellerResponse getById(@PathVariable Long id) {
        return sellerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SellerResponse create(@Valid @RequestBody SellerRequest request) {
        return sellerService.create(request);
    }

    @PutMapping("/{id}")
    public SellerResponse update(@PathVariable Long id, @Valid @RequestBody SellerRequest request) {
        return sellerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sellerService.delete(id);
    }
}
