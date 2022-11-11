package net.torrydev.microservices.reviewsservice.interfaces;

import net.torrydev.microservices.reviewsservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static net.torrydev.microservices.reviewsservice.interfaces.ProductServiceProxy.PRODUCT_API_VERSION;

/**
 * an interface to simplify the use of Rest Client calls
 * Feign will eliminate few boilerplate codes that comes with making service-to-service calls via
 * Rest Template/WebClient
 *
 * This will be a facade for all Product-Service related-calls
 */
@FeignClient(name = "PRODUCTS-SERVICE", url = "${api.gateway.url}"+PRODUCT_API_VERSION) // Using Product-ServiceApi-Gateway
//@FeignClient(name = "PRODUCTS-SERVICE") // Api-Gateway
//@FeignClient(name = "PRODUCTS-SERVICE", url = PRODUCT_API_GATEWAY) // Using Product-ServiceApi-Gateway
//@FeignClient(name = "PRODUCTS-SERVICE", url = "localhost:8381/") // Using the service url directly
//@FeignClient(name = "PRODUCTS-SERVICE") // This is LoadBalanced with the help of Eureka Service Discovery Running
public interface ProductServiceProxy {

    static String PRODUCT_API_VERSION = "api/v1/products/product";
    @GetMapping("/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);

    @GetMapping("/{category}")
    List<ProductDto> getListOfProductsByCategory(@PathVariable("category") String category);

    @GetMapping("/{brand}")
    List<ProductDto> getListOfProductsByBrand(@PathVariable("brand") String brand);

    @GetMapping("/brand/{brand}/category/{category}")
    List<ProductDto> getListOfProductsByBrandAndCategory(@PathVariable("brand") String brand, @PathVariable("category") String category);

    @GetMapping("/category/{category}/brand/{brand}")
    List<ProductDto> getListOfProductsByCategoryOrBrand(@PathVariable("category") String category, @PathVariable("brand") String brand);

    /*
     Product-related EndPoints:
    Product by id
     /products/product/{id} ==> Product
     Products by product category
    /products/product/{category} ==> List<Product>
    Products by product brand
    /products/product/{brand} ==> List<Product>
     Products by product brand AND category
    /products/product/brand/{brand}/category/{category} ==> List<Review>
    Products by product category OR brand
    /products/product/category/{category}/brand/{brand} ==> List<Review>
     */

    /*
     List of users who rated a product
     /ratings/product/{prodId}/users -> Pagination List<AppUsers>
     */
}
