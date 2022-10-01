package net.torrydev.microservices.productsservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.productsservice.model.Product;
import net.torrydev.microservices.productsservice.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import static net.torrydev.microservices.productsservice.constants.ServiceConstants.REST_API;

@Slf4j
@RestController
@RequestMapping(path = ProductController.PRODUCTS_BASE_URI)
public class ProductController{

    public static final String PRODUCTS_BASE_URI = REST_API + "/products/product";
    @Value("${service.welcome.msg}")
    String msg;

    @Autowired
    ProductServiceImpl productService;

    @PostMapping("/")
    public ResponseEntity<?> createNewProduct(@RequestBody Product product, HttpServletRequest request) {
        Product product1 = productService.createNewProduct(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(product1.getId()).toUri();
        log.trace("Client : {} New product created uri {}", uri, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(uri);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id, HttpServletRequest request) {
        Product product = productService.findById(id);
        log.info("Client {}, Product query {}",id, request.getRemoteAddr());
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> findByCategory(@PathVariable("category") String category) {
        List<Product> products = productService.findByCategory(category);
        if (products ==  null)
            return ResponseEntity.ok().body("Product not Found");
        else
            return ResponseEntity.ok().body(products);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<?> findByBrand(@PathVariable("brand") String brand) {
        List<Product> products = productService.findByBrand(brand);
        if (products ==  null)
            return ResponseEntity.ok().body("Product not Found");
        else
            return ResponseEntity.ok().body(products);
    }

    @GetMapping("/brand/{brand}/category/{category}")
    public ResponseEntity<?> findByBrandAndCategory(@PathVariable("brand") String brand, @PathVariable("category") String category) {
        List<Product> products = productService.findByBrandAndCategory(brand, category);
        return ResponseEntity.ok().body(Objects.requireNonNullElse(products, "Product not Found"));
    }

    @GetMapping("/category/{category}/brand/{brand}")
    public ResponseEntity<?> findByBrandOrCategory(@PathVariable("brand") String brand, @PathVariable("category") String category) {
        List<Product> products = productService.findByBrandOrCategory(brand, category);
        return ResponseEntity.ok().body(Objects.requireNonNullElse(products, "Product not Found"));
    }
}
