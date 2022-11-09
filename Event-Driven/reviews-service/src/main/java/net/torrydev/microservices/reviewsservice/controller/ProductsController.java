package net.torrydev.microservices.reviewsservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.reviewsservice.dto.ProductDto;
import net.torrydev.microservices.reviewsservice.interfaces.ProductServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.API_VERSION_WITH_FEIGN;
import static net.torrydev.microservices.reviewsservice.controller.ProductsController.PRODUCTS_BASE_ENDPOINT;

/**
 * Controller for accessing External products-service endpoints instead of wrapping everything
 * in ReviewController class
 */
@Slf4j
@RestController
@RequestMapping(path = PRODUCTS_BASE_ENDPOINT)
public class ProductsController {

    static final String PRODUCTS_BASE_ENDPOINT = API_VERSION_WITH_FEIGN + "/products/product";
    @Autowired
    private ProductServiceProxy productServiceProxy;
    private Long productId;
    private String category;
    private String brand;

    /*
 Product-related EndPoints:
Product by id
 /products/product/{id} ==> Product
 Products by product category
/products/product/{category} ==> List<Product>
Products by product brand
/products/product/{brand} ==> List<Product>
 Products by product brand AND category
/products/product/brand/{brand}/category/{category} ==> List<Product>
Products by product category OR brand
/products/product/category/{category}/brand/{brand} ==> List<Product>
 */
    @GetMapping("/{id}")
    @CircuitBreaker(name = "comment-circuit-breaker", fallbackMethod = "getProductById_FromCache")
    ProductDto getProductById(@PathVariable("id") Long id) {
        this.productId = id;
        log.trace("ReviewService[getProductById] {} ", id);
        return productServiceProxy.getProductById(id);
    }

    @GetMapping("/{category}")
    @CircuitBreaker(name = "comment-circuit-breaker", fallbackMethod = "getProductById_FromCache")
    List<ProductDto> getListOfProductsByCategory(@PathVariable("category") String category) {
        this.category = category;
        log.trace("ReviewService[getListOfProductsByCategory] {} ", category);
        return productServiceProxy.getListOfProductsByCategory(category);
    }

    @GetMapping("/{brand}")
    @CircuitBreaker(name = "comment-circuit-breaker", fallbackMethod = "getListOfProductsByBrand_FromCache")
    List<ProductDto> getListOfProductsByBrand(@PathVariable("brand") String brand) {
        this.brand = brand;
        log.trace("ReviewService[getListOfProductsByBrand] {} ", brand);
        return productServiceProxy.getListOfProductsByBrand(brand);
    }

    @GetMapping("/brand/{brand}/category/{category}")
    @CircuitBreaker(name = "comment-circuit-breaker", fallbackMethod = "getListOfProductsByBrandAndCategory_FromCache")
    List<ProductDto> getListOfProductsByBrandAndCategory(@PathVariable("brand") String brand, @PathVariable("category") String category) {
        this.brand = brand; this.category = category;
        log.trace("ReviewService[getListOfProductsByBrandAndCategory] {} and {}", brand, category);
        return productServiceProxy.getListOfProductsByBrandAndCategory(brand, category);
    }

    @GetMapping("/category/{category}/brand/{brand}")
    @CircuitBreaker(name = "comment-circuit-breaker", fallbackMethod = "getListOfProductsByCategoryOrBrand_FromCache")
    List<ProductDto> getListOfProductsByCategoryOrBrand(@PathVariable("category") String category, @PathVariable("brand") String brand) {
        log.trace("ReviewService[getListOfProductsByCategoryOrBrand] {} or {}", category, brand);
        return productServiceProxy.getListOfProductsByCategoryOrBrand(category, brand);
    }
    // FallBack Methods
    ProductDto getProductById_FromCache(Exception e){
        log.error("Error getting product, product will be pulled from cache {}", e.getMessage());
        return ProductDto.builder().id(this.productId).brand("Brand from cache").category("Category from Cache").name("Name from Cache")
                .price(BigDecimal.ONE).picurl(null).decs("Description from Cache")
                .build();
    }

    List<ProductDto> getListOfProductsByCategory_FromCache(Exception e){
        return List.of(ProductDto.builder().id(1L).brand("Brand from cache").category(this.category).name("Name from Cache")
                .price(BigDecimal.ONE).picurl(null).decs("Description from Cache")
                .build());
    }

    List<ProductDto> getListOfProductsByBrand_FromCache(Exception e){
        return List.of(ProductDto.builder().id(this.productId).brand(this.brand).category("Category from Cache").name("Name from Cache")
                .price(BigDecimal.ONE).picurl(null).decs("Description from Cache")
                .build());
    }

    List<ProductDto> getListOfProductsByBrandAndCategory_FromCache(Exception e){
        return List.of(ProductDto.builder().id(1L).brand(this.brand).category(this.category).name("Name from Cache")
                .price(BigDecimal.ONE).picurl(null).decs("Description from Cache")
                .build());
    }

    List<ProductDto> getListOfProductsByCategoryOrBrand_FromCache(Exception e){
        return List.of(ProductDto.builder().id(1L).brand(this.brand).category(this.category).name("Name from Cache")
                .price(BigDecimal.ONE).picurl(null).decs("Description from Cache")
                .build());
    }
}
