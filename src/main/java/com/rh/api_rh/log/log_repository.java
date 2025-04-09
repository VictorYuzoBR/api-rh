package com.rh.api_rh.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface log_repository extends JpaRepository<log_model, Long> {
}
