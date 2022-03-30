package com.homework.torder.repository;

import com.homework.torder.domain.OrderMenu;
import com.homework.torder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface OrderRepository extends JpaRepository<OrderMenu,Long> {

    @Transactional
    void deleteAllByUser(User user);
}
