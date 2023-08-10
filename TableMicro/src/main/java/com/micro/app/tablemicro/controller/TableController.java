package com.micro.app.tablemicro.controller;

import com.micro.app.tablemicro.model.Table;
import com.micro.app.tablemicro.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
@CrossOrigin
public class TableController {
    private final TableRepository tableRepository;
    @GetMapping()
    ResponseEntity<List<Table>> getAllTable() {
        return ResponseEntity.ok().body(tableRepository.findAll());
    }
    @GetMapping("/{tableId}")
    ResponseEntity<Table> getTableById(@PathVariable(value = "tableId") Integer tableId) {
        Table table = tableRepository.findById(tableId).orElse(null);
        if (table == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(table);
        }
    }
    @GetMapping("/status/{status-name}")
    ResponseEntity<List<Table>> getTableByStatus(@PathVariable(value = "status-name") String status) {
        List<Table> table = tableRepository.findByStatus(status);
        if (table == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(tableRepository.findByStatus(status));
        }
    }
    @GetMapping("/capacity/{number}")
    ResponseEntity<List<Table>> getTableByCapacity(@PathVariable Integer number) {
        List<Table> table = tableRepository.findByCapacity(number);
        if (table == null) {
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok().body(tableRepository.findByCapacity(number));
        }
    }
    @PostMapping
    ResponseEntity<Table> addTable(@RequestBody Table table) {
        Table tab = new Table(table.getId(), table.getName(), "PENDING", table.getCapacity());
        tableRepository.save(tab);
        return ResponseEntity.ok().body(table);
    }
}
