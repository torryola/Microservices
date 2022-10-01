package net.torrydev.microservices.appuserservice.repository;

import net.torrydev.microservices.appuserservice.model.AppUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/*
Set replace = AutoConfigureTestDatabase.Replace.NONE if you want to use specific Datasource config
   by default Spring will use H2 Database for testing
 */
class AppUserRepositoryWithTestContainer extends TestContainerBaseClass {

    // Under Test
    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser appUser;
    private String email;
    private String userName;
    private String unknownUserName;
    private String unknownEmail;

    // Create MySql Container
//    @Container
//    MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
//                .withUsername("root")
//                .withPassword("password")
//                .withDatabaseName("testuser");

    @BeforeEach
    void setUp() {
        email = "johnolga1@email.com";
        userName = "johnny1";
        appUser = AppUser.builder().name("Olga John").username(userName).email(email).build();
        unknownUserName = "thwbd";
        unknownEmail = "unknown@email.com";
    }

    @Test
    @Sql("classpath:test-data.sql")
    void populateTestData(){

    }

    @Test
    void findByUsername_Passed() {
        // When
        appUserRepository.save(appUser);
        AppUser expected = appUserRepository.findByUsername(userName).orElse(null);
        // Then
        assertThat(expected).isNotNull();
        assertThat(expected.getUsername()).isEqualTo(appUser.getUsername());
    }

    @Test
    void findBy_Unknown_Username() {
        // When
        AppUser expected = appUserRepository.findByUsername(unknownUserName).orElse(null);
        // Then
        assertThat(expected).isNull();
    }

    @Test
    void findByEmail_Passed() {
        // When
        appUserRepository.save(appUser);
        AppUser expectedUser = appUserRepository.findByEmail(email).orElse(null);
        // Then
        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getEmail()).isEqualTo(appUser.getEmail());
    }

    @Test
    void findBy_Unknown_Email() {
        // When
        appUserRepository.save(appUser);
        AppUser expectedUser = appUserRepository.findByEmail(unknownEmail).orElse(null);
        // Then
        assertThat(expectedUser).isNull();
    }

    @Test
    void existsByEmail() {
        // When
        appUserRepository.save(appUser);
        boolean exist = appUserRepository.existsByEmail(email);
        // Then
        assertThat(exist).isTrue();
    }

    @Test
    void notExistsByEmail() {
        // When
        boolean exist = appUserRepository.existsByEmail(unknownEmail);
        // Then
        assertThat(exist).isFalse();
    }

    @Test
    void existsByUsername() {
        // When
        appUserRepository.save(appUser);
        boolean exist = appUserRepository.existsByUsername(userName);
        // Then
        assertThat(exist).isTrue();
    }


    @Test
    void notExistsByUserName() {
        // Given
        // When
        boolean exist = appUserRepository.existsByUsername(unknownUserName);
        // Then
        assertThat(exist).isFalse();
    }

    @AfterEach
    void tearDown() {
        // CleanUp the DB
        appUserRepository.deleteAll();
    }
}