package com.homework.torder.repository;

import com.homework.torder.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
