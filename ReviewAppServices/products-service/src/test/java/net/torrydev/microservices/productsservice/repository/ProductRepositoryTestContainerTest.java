package net.torrydev.microservices.productsservice.repository;

import net.torrydev.microservices.productsservice.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTestContainerTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product, prod;

    @BeforeEach
    void setUp() {
        product = Product.builder().brand("adidas").category("Trainers").description("Lightweight Trainers")
                .picurl(null).price(new BigDecimal("65.0")).name("TrainFeather").build();
        prod = productRepository.save(product);
    }

    @Test
    void findByCategory() {
        String cat = "Trainers";
        assertThat(productRepository.findByCategory(cat)).isNotNull();
        assertThat(productRepository.findByCategory(cat).size()).isGreaterThan(0);
        cat = "Basket";
        assertThat(productRepository.findByCategory(cat).size()).isZero();
    }

    @Test
    void findByBrand() {
        String brand = "adidas";
        assertThat(productRepository.findByBrand(brand)).isNotNull();
        assertThat(productRepository.findByBrand(brand).size()).isGreaterThan(0);
        brand = "Nike";
        assertThat(productRepository.findByBrand(brand).size()).isZero();
    }

    @Test
    void findByBrandAndCategory() {
        String brand = "adidas", cat = "Trainers";
        assertThat(productRepository.findByBrandAndCategory(brand, cat)).isNotNull();
        assertThat(productRepository.findByBrandAndCategory(brand, cat).size()).isGreaterThan(0);
        brand = "Nike"; cat = "Trainers";
        assertThat(productRepository.findByBrandAndCategory(brand, cat).size()).isZero();
    }

    @Test
    void findByBrandOrCategory() {
        String brand = "adidas", cat = "Trainers";
        assertThat(productRepository.findByBrandOrCategory(brand, cat)).isNotNull();
        assertThat(productRepository.findByBrandOrCategory(brand, cat).size()).isGreaterThan(0);
        brand = "Nike";
        assertThat(productRepository.findByBrandOrCategory(brand, cat).size()).isNotZero();
        brand = "Kegglo"; cat = "Basket";
        assertThat(productRepository.findByBrandOrCategory(brand, cat).size()).isZero();
    }

    @Test
    void createNewProduct(){
        assertEquals(product.getBrand(), prod.getBrand());
        assertThat(prod.getId()).isNotZero();
    }
}