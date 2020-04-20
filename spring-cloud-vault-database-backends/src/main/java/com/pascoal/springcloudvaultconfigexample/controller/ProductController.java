package com.pascoal.springcloudvaultconfigexample.controller;

import com.pascoal.springcloudvaultconfigexample.entity.Product;
import com.pascoal.springcloudvaultconfigexample.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody final Product product) {
        final String id = productService.createProduct(product);

        URI uriLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/")
                .path(id)
                .buildAndExpand()
                .toUri();

        return ResponseEntity.created(uriLocation).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable final String id){
        Product foundProduct = productService.getProductById(id);
        return ResponseEntity.ok(foundProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable final String id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
