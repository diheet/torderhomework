package com.homework.torder.Repository;

import com.homework.torder.Domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
