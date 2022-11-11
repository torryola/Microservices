package net.torrydev.microservices.productsservice.repository;

import net.torrydev.microservices.productsservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByBrandAndCategory(String brand, String category);
    List<Product> findByBrandOrCategory(String brand, String category);
}
