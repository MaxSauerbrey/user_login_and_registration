package com.example.registration_backend_email_verification.appuser.service;

import com.example.registration_backend_email_verification.appuser.AppUser;
import com.example.registration_backend_email_verification.appuser.repository.AppUserRepository;
import com.example.registration_backend_email_verification.registration.token.ConfirmationToken;
import com.example.registration_backend_email_verification.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    // This is how we find the users once we try to log in
    private final static String USER_NOT_FOUND_MSG = "Usuario con el email %s no encontrado";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG,email)));
    }
    public String signUpUser (AppUser appUser){
       boolean userExists = appUserRepository
               .findByEmail(appUser.getEmail())
                .isPresent();
       if (userExists) {
           // TODO check of attributes are the same and
           // TODO if email not confirmed send confirmation email
           throw new IllegalStateException("Email already taken");
       }
       String encodedPassword = bCryptPasswordEncoder
               .encode(appUser.getPassword());
       appUser.setPassword(encodedPassword);

       appUserRepository.save(appUser);

       String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO: SEND EMAIL
        return token;
    }

    public int enableAppUser(String email){
        return appUserRepository.enableAppUser(email);
    }
}
