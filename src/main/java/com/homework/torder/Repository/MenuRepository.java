package com.homework.torder.Repository;

import com.homework.torder.Domain.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository <FoodMenu, Long>{

}

