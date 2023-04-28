package com.example.registration_backend_email_verification.appuser.repository;

import com.example.registration_backend_email_verification.appuser.AppUser;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
@Repository
@Transactional
public interface AppUserRepository {
    Optional<AppUser> findByEmail (String email);

}
