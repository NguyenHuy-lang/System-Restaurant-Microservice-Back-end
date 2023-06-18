package com.micro.app.foodmicro.repositories;


import com.micro.app.foodmicro.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findAllByNameLike(String name);
}
