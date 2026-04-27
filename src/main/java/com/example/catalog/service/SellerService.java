package com.example.catalog.service;

import com.example.catalog.dto.seller.SellerRequest;
import com.example.catalog.dto.seller.SellerResponse;
import com.example.catalog.entity.Seller;
import com.example.catalog.exception.ResourceNotFoundException;
import com.example.catalog.mapper.SellerMapper;
import com.example.catalog.repository.SellerRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Transactional(readOnly = true)
    public List<SellerResponse> getAll() {
        return sellerRepository.findAll().stream().map(SellerMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SellerResponse getById(Long id) {
        return SellerMapper.toResponse(findSeller(id));
    }

    @Transactional
    public SellerResponse create(SellerRequest request) {
        sellerRepository.findByExternalId(request.externalId()).ifPresent(existing -> {
            throw new IllegalArgumentException("Seller externalId already exists");
        });

        Seller seller = new Seller();
        SellerMapper.apply(request, seller);
        return SellerMapper.toResponse(sellerRepository.save(seller));
    }

    @Transactional
    public SellerResponse update(Long id, SellerRequest request) {
        Seller seller = findSeller(id);

        sellerRepository.findByExternalId(request.externalId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Seller externalId already exists");
                });

        SellerMapper.apply(request, seller);
        return SellerMapper.toResponse(sellerRepository.save(seller));
    }

    @Transactional
    public void delete(Long id) {
        Seller seller = findSeller(id);
        sellerRepository.delete(seller);
    }

    private Seller findSeller(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + id));
    }
}
