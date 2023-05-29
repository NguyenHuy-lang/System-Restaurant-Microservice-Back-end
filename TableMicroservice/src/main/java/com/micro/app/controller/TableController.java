package com.micro.app.controller;

import com.micro.app.model.Table;
import com.micro.app.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class TableController {
    private final TableRepository tableRepository;
    @GetMapping("/")
    ResponseEntity<List<Table>> getAllTable() {
        return ResponseEntity.ok().body(tableRepository.findAll());
    }
}
