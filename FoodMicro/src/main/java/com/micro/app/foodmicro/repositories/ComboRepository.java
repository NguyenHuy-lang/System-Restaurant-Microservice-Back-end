package com.micro.app.foodmicro.repositories;

import com.micro.app.foodmicro.model.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Integer> {
}
