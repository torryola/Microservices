package net.torrydev.microservices.productsservice.interfaces;

import net.torrydev.microservices.productsservice.model.Product;

import java.util.List;

public interface IProductService {
    // create New Product
    Product createNewProduct(Product product);
    // find product by id
    Product findById(Long Id);
    // find product by category
    List<Product> findByCategory(String category);
    // find product by brand
    List<Product> findByBrand(String brand);
    // find product by brand and category
    List<Product> findByBrandAndCategory(String brand, String category);
    // find products by brand or category
    List<Product> findByBrandOrCategory(String brand, String category);

}
