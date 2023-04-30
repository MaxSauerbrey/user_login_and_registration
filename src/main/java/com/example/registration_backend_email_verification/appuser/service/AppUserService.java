package com.example.registration_backend_email_verification.appuser.service;

import com.example.registration_backend_email_verification.appuser.AppUser;
import com.example.registration_backend_email_verification.appuser.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    // This is how we find the users once we try to log in
    private final static String USER_NOT_FOUND_MSG = "Usuario con el email %s no encontrado";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }
    public String signUpUser (AppUser appUser){
       boolean userExists = appUserRepository
               .findByEmail(appUser.getEmail())
                .isPresent();
       if (userExists) throw new IllegalStateException("Email already taken");
       String encodedPassword = bCryptPasswordEncoder
               .encode(appUser.getPassword());
       appUser.setPassword(encodedPassword);

       appUserRepository.save(appUser);
       //TODO: Send confirmation token
        return "it works";
    }

}
