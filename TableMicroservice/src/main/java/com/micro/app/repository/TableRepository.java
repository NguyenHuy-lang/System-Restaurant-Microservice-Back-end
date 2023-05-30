package com.micro.app.repository;

import com.micro.app.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Integer> {
    List<Table> findByStatus(String status);
    List<Table> findByCapacity(Integer capacity);
}
