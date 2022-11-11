package net.torrydev.microservices.appuserservice.service;

import net.torrydev.microservices.appuserservice.model.AppUser;
import net.torrydev.microservices.appuserservice.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(Parameterized.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;
    // Under Test
    @InjectMocks // i.e. Mocked Repo will be injected into our service
    private AppUserServiceImpl appUserService;

    @Captor
    private ArgumentCaptor<AppUser> appUserArgumentCaptor;

    private AppUser appUser;
    private String email;
    private String userName;
    private String unknownUserName;
    private String unknownEmail;
    private Long id;

    @BeforeEach
    void setUp() {
        id = 145L;
        email = "johnolga1@email.com";
        userName = "johnny1";
        appUser = AppUser.builder().name("Olga John").username(userName).email(email).Id(id).build();
        unknownUserName = "thwbd";
        unknownEmail = "unknown@email.com";
    }

    @Test
    void findByUsername() {
        Optional<AppUser> optionalAppUser = Optional.of(appUser);
        // When
        when(appUserRepository.findByUsername(userName)).thenReturn(optionalAppUser);
        //Then
        AppUser expectedUser = appUserService.findByUsername(userName);

        assertThat(expectedUser).isNotNull();

        AppUser user = optionalAppUser.get();
        assertThat(user.getId()).isEqualTo(appUser.getId());

        // Verify repo is called
        verify(appUserRepository).findByUsername(userName);
    }

    @Test
    void findByUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> appUserService.findByUsername(null));
        assertThrows(IllegalArgumentException.class, () -> appUserService.findByUsername(""));
        assertThrows(IllegalArgumentException.class, () -> appUserService.findByUsername(" "));
    }

    @Parameterized.Parameters
    public static Collection<String> getParameters(){
        return Arrays.asList(null, "", " ");
    }

    @Test
    void registerNewUser() {
        Optional<AppUser> optionalAppUser = Optional.of(appUser);
        // When
        when(appUserRepository.save(appUser)).thenReturn(optionalAppUser.get());
        // Mockito
//        given(appUserRepository.save(appUser)).willReturn(optionalAppUser.get());
        //Then
        assertThat(appUserService.registerNewUser(appUser)).isNotNull();

        // Verify save method is called only ones in our repo and argument is of Type AppUser
        verify(appUserRepository, times(1)).save(ArgumentMatchers.any(AppUser.class));
    }

    @Test
    void findIdByUsername() {

        // When
        when(appUserRepository.findByUsername(userName)).thenReturn(Optional.of(appUser));
        // then
        AppUser user = appUserService.findByUsername(userName);

        verify(appUserRepository).findByUsername(userName);

        assertThat(user).isNotNull();
    }

    @Test
    void findById() {
        // When
        when(appUserRepository.findById(id)).thenReturn(Optional.of(appUser));
        // then
        AppUser user = appUserService.findById(id);

        verify(appUserRepository).findById(id);

        assertThat(user).isNotNull();
    }

    @Test
    void isExistsByUsername() {
        // When
        when(appUserRepository.existsByUsername(userName)).thenReturn(true);
        // then
        boolean exists = appUserService.isExistsByUsername(userName);

        verify(appUserRepository).existsByUsername(userName);

        assertThat(exists).isTrue();
    }

    @Test
    void isExistsById() {
        // When
        when(appUserRepository.existsById(id)).thenReturn(true);
        // then
        boolean exists = appUserService.isExistsById(id);

        verify(appUserRepository).existsById(id);

        assertThat(exists).isTrue();
    }

    @Test
    void allUsers() {

        // When
        when(appUserRepository.save(any())).thenReturn(appUser);
        appUserService.registerNewUser(appUser);
        when(appUserRepository.findAll()).thenReturn(List.of(appUser));
        // then
       List<AppUser> appUserList = appUserService.allUsers().getAppUserList();

       verify(appUserRepository).findAll();

       assertThat(appUserList.size()).isGreaterThan(0);
    }
}