package com.micro.app.repository;

import com.micro.app.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TableRepository extends JpaRepository<Table, Integer> {

}
