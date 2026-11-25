package com.unsis.legalhub.superadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperadminRepository extends JpaRepository<Superadmin, Long> {

    Optional<Superadmin> findByUsername(String username);
}
