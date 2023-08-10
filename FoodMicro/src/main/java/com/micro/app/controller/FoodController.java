package com.micro.app.controller;


import com.micro.app.model.Food;
import com.micro.app.repositories.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
@CrossOrigin
public class FoodController {
    private final FoodRepository foodRepository;
    @GetMapping()
    ResponseEntity<List<Food>> getAllFood() {
        return ResponseEntity.ok().body(foodRepository.findAll());
    }
    @GetMapping("/{foodId}")
    ResponseEntity<Food> getFoodById(@PathVariable(value = "foodId") Integer foodId) {
        Food food = foodRepository.findById(foodId).orElse(null);
        if (food == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(food);
        }
    }
    @PostMapping()
    ResponseEntity<Food> createFood(@RequestBody Food food) {
        return ResponseEntity.ok().body(foodRepository.save(food));
    }
    @GetMapping("/name/{foodName}")
    ResponseEntity<List<Food>> getFoodByName(@PathVariable(value = "foodName") String foodName) {
        List<Food> foods = foodRepository.findAllByNameLike("%"+foodName+"%");
        if (foods.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(foods);
        }
    }

}
