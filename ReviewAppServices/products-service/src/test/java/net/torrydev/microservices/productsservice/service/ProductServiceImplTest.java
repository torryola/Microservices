package net.torrydev.microservices.productsservice.service;

import net.torrydev.microservices.productsservice.model.Product;
import net.torrydev.microservices.productsservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;
    private Product product, expectedProduct, productParam;
    private List<Product> productList;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        product = Product.builder().Id(100L).brand("Adidas").category("Trainers").description("Lightweight Trainers")
                .picurl(null).price(new BigDecimal("65.0")).name("TrainFeather").build();
        productList = List.of(product);
    }

    @Test
    void createNewProduct() {
        //when
        when(repository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);
        //then
        expectedProduct = productService.createNewProduct(product);
        // verify
        verify(repository).save(ArgumentMatchers.any(Product.class));
        //asserts
        assertThat(expectedProduct.getBrand()).isEqualTo(product.getBrand());
    }

    @Test
    void findById() {
        Long id = 100L;
        when(repository.findById(100L)).thenReturn(Optional.of(product));

        expectedProduct = productService.findById(100L);
        verify(repository).findById(id);
        assertThat(expectedProduct).isNotNull();
        assertThat(expectedProduct.getDescription()).isEqualTo(product.getDescription());

        Stream.of(null, -1L).forEach(i -> assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> productService.findById(i))
                .withMessage("Invalid id, please enter valid id"));
    }

    @Test
    void findByCategory() {
        //given
        var category ="Trainers";
        //when
        when(repository.findByCategory(category)).thenReturn(productList);
        //then
        expectedProduct = productService.findByCategory(category).get(0);
        // verify
        verify(repository).findByCategory(any());
        // asserts
        assertThat(expectedProduct.getCategory()).isEqualTo(category);
        // given
        category = "Basket";
        assertThat(productService.findByCategory(category).size()).isZero();
        // Failed
        Stream.of(null, "", " ").forEach(s -> assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> productService.findByCategory(s))
                .withMessage("Invalid Category"));
    }

    @Test
    void findByBrand() {
        //given
        var brand = "Adidas";
        // when
        when(repository.findByBrand(brand)).thenReturn(productList);
        // then
        expectedProduct = productService.findByBrand(brand).get(0);
        // verify
        verify(repository).findByBrand(any());
        // asserts
        assertThat(expectedProduct.getBrand()).isEqualTo(brand);
        brand = "Bask";
        assertThat(productService.findByBrand(brand).size()).isZero();

        // assert Exceptions
        Stream.of(""," ",null).forEach(s -> assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.findByBrand(s)).withMessage("Unknown Brand"));
    }

    @Test
    void findByBrandAndCategory() {
        // given
         String brand = "Adidas", category = "Trainers";
         // when
        when(repository.findByBrandAndCategory(brand, category)).thenReturn(productList);
        // then
        expectedProduct = productService.findByBrandAndCategory(brand, category).get(0);
        // verify
        verify(repository).findByBrandAndCategory(any(), any());
        // asserts
        assertThat(expectedProduct.getId()).isEqualTo(100);
        // Exception
        Stream.of(null, "", " ").forEach(s -> assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.findByBrandAndCategory(s,s)).withMessage("Invalid Brand or Category")
        );
    }

    @Test
    void findByBrandOrCategory() {
        // given
        String brand = "Adidas", category = "Trainers";
        // when
        when(repository.findByBrandOrCategory(brand, category)).thenReturn(productList);
        // then
        expectedProduct = productService.findByBrandOrCategory(brand, category).get(0);
        // verify
        verify(repository).findByBrandOrCategory(any(), any());
        // asserts
        assertThat(expectedProduct.getId()).isEqualTo(100);
        // Exception
        Stream.of(null, "", " ").forEach(s -> assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.findByBrandOrCategory(s,s)).withMessage("Invalid Brand or Category")
        );
    }
}