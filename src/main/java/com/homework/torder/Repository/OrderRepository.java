package com.homework.torder.Repository;

import com.homework.torder.Domain.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderMenu,Long> {
}
