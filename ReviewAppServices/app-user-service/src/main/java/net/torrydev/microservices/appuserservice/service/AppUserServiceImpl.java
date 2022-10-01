package net.torrydev.microservices.appuserservice.service;

import net.torrydev.microservices.appuserservice.dto.UsersListDto;
import net.torrydev.microservices.appuserservice.interfaces.IAppUserService;
import net.torrydev.microservices.appuserservice.model.AppUser;
import net.torrydev.microservices.appuserservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements IAppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUser findByUsername(String username) throws IllegalArgumentException {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Please provide a valid userName");
        return appUserRepository.findByUsername(username).orElse(null);
    }

    @Override
    public AppUser registerNewUser(AppUser appUser) {
        if (!appUserRepository.existsByEmail(appUser.getEmail()) && !appUserRepository.existsByUsername(appUser.getUsername()))
            return appUserRepository.save(appUser);
        else return null;
    }

    @Override
    public Long findIdByUsername(String username) {
        if (username.isBlank())
            throw new IllegalArgumentException("Unknown UserName, Please enter valid username");
        return appUserRepository.findByUsername(username).map(AppUser::getId).orElse(0L);
    }

    @Override
    public AppUser findById(Long Id) {
        if (Id <= 0)
            throw new IllegalArgumentException("Invalid Id");
        return appUserRepository.findById(Id).orElse(null);
    }

    @Override
    public Boolean isExistsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }

    @Override
    public Boolean isExistsById(Long Id) {
        return appUserRepository.existsById(Id);
    }

    @Override
    public UsersListDto allUsers(){
        return UsersListDto.builder()
                .appUserList(appUserRepository.findAll())
                .build();
    }

}
