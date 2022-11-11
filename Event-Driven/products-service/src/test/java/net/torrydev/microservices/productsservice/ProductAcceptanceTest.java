package net.torrydev.microservices.productsservice;

import com.google.gson.Gson;
import net.torrydev.microservices.productsservice.model.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static net.torrydev.microservices.productsservice.controller.ProductController.PRODUCTS_BASE_URI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductAcceptanceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    private static Product product;
    private static final String BASE_URL = PRODUCTS_BASE_URI;

    @BeforeAll
    static void setUp() {
        product = Product.builder().brand("Adidas").category("Trainers").description("Lightweight Trainers")
                .picurl(null).price(new BigDecimal("65.0")).name("TrainFeather").build();

    }

    @Test
    @Order(1)
    void createNewProduct() throws Exception {
        mockMvc.perform(post(BASE_URL+"/").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(product))).andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.is("http://localhost/api/v1/products/product/1")));
    }

    @Test
    @Order(2)
    void findById() throws Exception {
        //given
        var id = 1001;
        //then
        mockMvc.perform(get(BASE_URL+"/{id}", id).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(id)))
                .andExpect(jsonPath("$.description", Matchers.is(product.getDescription())));
    }

    @Test
    @Order(3)
    void findByCategory() throws Exception {
        //given
        var category = "Trainers";
        // then
        mockMvc.perform(get(BASE_URL+"/category/{category}", category)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)));
    }

    @Test
    @Order(4)
    void findByBrand() {
        //given
        Stream.of("adidas", "Adidas").forEach(s -> {
            // then
            try {
                mockMvc.perform(get(BASE_URL+"/brand/{brand}", s)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()", Matchers.is(2)))
                        .andExpect(jsonPath("$[0].id", Matchers.is(1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    @Order(5)
    void findByBrandAndCategory() throws Exception {
        // given
        String brand = "adidas", category = "trainers";
        // then
        mockMvc.perform(get(BASE_URL+"/brand/{brand}/category/{category}", brand, category)
                        .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$.[0].brand", Matchers.is(product.getBrand())))
                .andExpect(jsonPath("$.[0].description", Matchers.is(product.getDescription())));
    }

    @Test
    @Order(6)
    void findByBrandOrCategory() throws Exception {
        // given
        String brand = "adidas", category = "trainers";

        mockMvc.perform(get(BASE_URL+"/category/{category}/brand/{brand}", category, brand)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$.[0].brand", Matchers.is(product.getBrand())))
                .andExpect(jsonPath("$.[0].description", Matchers.is(product.getDescription())));
    }
}