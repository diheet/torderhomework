package com.homework.torder.repository;

import com.homework.torder.domain.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository <FoodMenu, Long>{

}

