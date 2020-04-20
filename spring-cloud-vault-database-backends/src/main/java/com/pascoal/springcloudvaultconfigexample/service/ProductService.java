package com.pascoal.springcloudvaultconfigexample.service;

import com.pascoal.springcloudvaultconfigexample.entity.Product;
import com.pascoal.springcloudvaultconfigexample.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public String createProduct(final Product product){
        if (StringUtils.isEmpty(product.getDescription()) || product.getPrice() == null){
            throw new IllegalArgumentException("Product must have a price and a description");
        }
      return productRepository.save(product).getId();
    }

    public Product getProductById(final String id){
        return productRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("No product found by given id: "+id));
    }

    @Transactional
    public void deleteProductById(final String id){
        Product productFound = getProductById(id);
        productRepository.deleteById(productFound.getId());
    }
}
