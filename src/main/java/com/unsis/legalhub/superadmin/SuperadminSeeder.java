package com.unsis.legalhub.superadmin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class SuperadminSeeder implements ApplicationRunner {

    private final SuperadminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;
    private final boolean syncPassword;

    public SuperadminSeeder(
            SuperadminRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.superadmin.username}") String username,
            @Value("${app.superadmin.password}") String password,
            @Value("${app.superadmin.sync-password:false}") boolean syncPassword) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
        this.syncPassword = syncPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Optional<Superadmin> existing = repository.findByUsername(username);

        if (existing.isEmpty()) {
            repository.save(new Superadmin(username, passwordEncoder.encode(password)));
            return;
        }

        if (syncPassword) {
            Superadmin admin = existing.get();
            admin.setPasswordHash(passwordEncoder.encode(password));
        }
    }
}
